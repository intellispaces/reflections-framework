package tech.intellispaces.jaquarius.annotation.processor.objecthandle;

import tech.intellispaces.jaquarius.annotation.AutoGuide;
import tech.intellispaces.jaquarius.annotation.Inject;
import tech.intellispaces.jaquarius.annotation.processor.AbstractObjectHandleGenerator;
import tech.intellispaces.jaquarius.annotation.processor.GuideProcessorFunctions;
import tech.intellispaces.jaquarius.common.NameConventionFunctions;
import tech.intellispaces.jaquarius.exception.ConfigurationExceptions;
import tech.intellispaces.jaquarius.guide.GuideFunctions;
import tech.intellispaces.jaquarius.object.ObjectHandleFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleTypes;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.jaquarius.system.Modules;
import tech.intellispaces.jaquarius.system.ProjectionInjection;
import tech.intellispaces.action.runnable.RunnableAction;
import tech.intellispaces.action.text.StringActions;
import tech.intellispaces.entity.exception.UnexpectedExceptions;
import tech.intellispaces.entity.text.StringFunctions;
import tech.intellispaces.entity.type.ClassFunctions;
import tech.intellispaces.entity.type.ClassNameFunctions;
import tech.intellispaces.entity.type.PrimitiveTypes;
import tech.intellispaces.java.annotation.context.JavaArtifactContext;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodParam;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.CustomTypeReference;
import tech.intellispaces.java.reflection.reference.CustomTypeReferences;
import tech.intellispaces.java.reflection.reference.NamedReference;
import tech.intellispaces.java.reflection.reference.TypeReference;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

abstract class AbstractObjectHandleWrapperGenerator extends AbstractObjectHandleGenerator {
  protected String domainSimpleClassName;
  protected String typeParamsFull;
  protected String typeParamsBrief;
  protected boolean isAlias;
  protected String primaryDomainSimpleName;
  protected String primaryDomainTypeArguments;
  protected boolean implRelease;
  private final List<MethodStatement> domainMethods;
  protected final CustomType domainType;
  protected final List<Object> constructors = new ArrayList<>();
  protected final List<String> guideActions = new ArrayList<>();
  protected final List<Map<String, Object>> objectHandleMethods = new ArrayList<>();
  protected final List<Map<String, String>> guideMethods = new ArrayList<>();
  protected final List<Map<String, Object>> injections = new ArrayList<>();
  protected final List<Map<String, Object>> injectionMethods = new ArrayList<>();

  AbstractObjectHandleWrapperGenerator(CustomType initiatorType, CustomType objectHandleType) {
    super(initiatorType, objectHandleType);

    this.domainType = ObjectHandleFunctions.getDomainTypeOfObjectHandle(objectHandleType);
    this.domainMethods = domainType.actualMethods().stream()
        .filter(m -> !m.isDefault())
        .filter(m -> excludeDeepConversionMethods(m, domainType))
        .filter(this::isNotDomainClassGetter)
        .toList();
  }

  protected void analyzeReleaseMethod(CustomType objectHandleType) {
    Optional<MethodStatement> releaseMethod = objectHandleType.declaredMethod("release", List.of());
    implRelease = releaseMethod.isPresent() && !releaseMethod.get().isAbstract();
  }

  @Override
  protected Stream<MethodStatement> getObjectHandleMethods(
      CustomType objectHandleType, RoundEnvironment roundEnv
  ) {
    return domainMethods.stream();
  }

  protected void analyzeDomain() {
    CustomType domainType = ObjectHandleFunctions.getDomainTypeOfObjectHandle(annotatedType);
    context.addImport(domainType.canonicalName());
    domainSimpleClassName = context.simpleNameOf(domainType.canonicalName());

    List<CustomTypeReference> equivalentDomains = DomainFunctions.getEquivalentDomains(domainType);
    isAlias = !equivalentDomains.isEmpty();
    if (isAlias) {
      CustomTypeReference nearEquivalentDomain = equivalentDomains.get(0);
      CustomTypeReference mainEquivalentDomain = equivalentDomains.get(equivalentDomains.size() - 1);

      primaryDomainSimpleName = context.addToImportAndGetSimpleName(mainEquivalentDomain.targetType().canonicalName());
      primaryDomainTypeArguments = nearEquivalentDomain.typeArgumentsDeclaration(context::addToImportAndGetSimpleName);
    }
  }

  protected void analyzeInjectedGuides(CustomType objectHandleType) {
    for (MethodStatement method : annotatedType.declaredMethods()) {
      if (method.isAbstract()) {
        if (isInjectionMethod(method)) {
          if (!isReturnGuide(method)) {
            throw ConfigurationExceptions.withMessage("Guide injection method '{0}' in class {1} must return guide",
                method.name(), annotatedType.className()
            );
          }
          if (isAutoGuideMethod(method)) {
            addAutoGuideInjectionAndImplementationMethod(method);
          } else {
            addGuideInjectionAndImplementationMethod(method);
          }
        } else {
          throw ConfigurationExceptions.withMessage("Undefined abstract method '{0}' in class {1}",
              method.name(), annotatedType.className()
          );
        }
      }
    }
  }

  private void addAutoGuideInjectionAndImplementationMethod(MethodStatement method) {
    context.addImport(Modules.class);
    context.addImport(ProjectionInjection.class);

    String injectionName = method.name();
    String injectionType = method.returnType().orElseThrow().actualDeclaration();

    Map<String, Object> injection = new HashMap<>();
    injection.put("kind", "autoguide");
    injection.put("name", injectionName);
    injection.put("type", injectionType);
    injections.add(injection);

    Map<String, Object> methodProperties = new HashMap<>();
    methodProperties.put("javadoc", "");
    methodProperties.put("annotations", List.of(Override.class.getSimpleName()));
    methodProperties.put("signature", buildMethodSignature(method));
    methodProperties.put("body", buildInjectionMethodBody(injectionType, injections.size() - 1));
    injectionMethods.add(methodProperties);
  }

  private void addGuideInjectionAndImplementationMethod(MethodStatement method) {
    context.addImport(Modules.class);
    context.addImport(ProjectionInjection.class);

    String injectionName = method.name();
    String injectionType = method.returnType().orElseThrow().actualDeclaration();

    Map<String, Object> injection = new HashMap<>();
    injection.put("kind", "guide");
    injection.put("name", injectionName);
    injection.put("type", injectionType);
    injections.add(injection);

    Map<String, Object> methodProperties = new HashMap<>();
    methodProperties.put("javadoc", "");
    methodProperties.put("annotations", List.of(Override.class.getSimpleName()));
    methodProperties.put("signature", buildMethodSignature(method));
    methodProperties.put("body", buildInjectionMethodBody(injectionType, injections.size() - 1));
    injectionMethods.add(methodProperties);
  }

  private String buildInjectionMethodBody(String injectionType, int injectionIndex) {
    return "return (" + injectionType + ") this.$innerHandle.injection(" + injectionIndex + ").value();";
  }

  private MethodStatement findGuideMethod(
      MethodStatement domainMethod, List<MethodStatement> objectHandleMethods, ObjectReferenceForm targetForm
  ) {
    for (MethodStatement objectHandleMethod : objectHandleMethods) {
      if (!GuideFunctions.isGuideMethod(objectHandleMethod)) {
        continue;
      }
      if (
          getMethodName(domainMethod, targetForm).equals(objectHandleMethod.name())
              && equalParams(domainMethod.params(), objectHandleMethod.params())
      ) {
        return objectHandleMethod;
      }
    }
    return null;
  }

  private boolean equalParams(List<MethodParam> domainMethodParams, List<MethodParam> guideMethodParams) {
    if (domainMethodParams.size() != guideMethodParams.size()) {
      return false;
    }

    boolean paramMatches = true;
    for (int i = 0; i < domainMethodParams.size(); i++) {
      MethodParam domainMethodParam = domainMethodParams.get(i);
      String domainMethodParamDeclaration = ObjectHandleFunctions.getObjectHandleDeclaration(
          domainMethodParam.type(), ObjectHandleTypes.Common, Function.identity()
      );

      MethodParam guideMethodParam = guideMethodParams.get(i);
      String guideMethodParamDeclaration = guideMethodParam.type().actualDeclaration(ClassNameFunctions::getShortenName);
      if (!domainMethodParamDeclaration.equals(guideMethodParamDeclaration)) {
        paramMatches = false;
        break;
      }
    }
    return paramMatches;
  }

  protected String getGeneratedClassCanonicalName() {
    return NameConventionFunctions.getObjectHandleWrapperCanonicalName(annotatedType);
  }

  protected void analyzeTypeParams(CustomType objectHandleType) {
    if (objectHandleType.typeParameters().isEmpty()) {
      typeParamsFull = typeParamsBrief = "";
      return;
    }

    var typeParamsFullBuilder = new StringBuilder();
    var typeParamsBriefBuilder = new StringBuilder();
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(typeParamsFullBuilder, typeParamsBriefBuilder);

    typeParamsFullBuilder.append("<");
    typeParamsBriefBuilder.append("<");
    for (NamedReference typeParam : objectHandleType.typeParameters()) {
      commaAppender.run();
      typeParamsFullBuilder.append(typeParam.formalFullDeclaration());
      typeParamsBriefBuilder.append(typeParam.formalBriefDeclaration());
    }
    typeParamsFullBuilder.append(">");
    typeParamsBriefBuilder.append(">");
    typeParamsFull = typeParamsFullBuilder.toString();
    typeParamsBrief = typeParamsBriefBuilder.toString();
  }

  protected void analyzeConstructors(CustomType objectHandleType) {
    List<MethodStatement> constructors;
    if (objectHandleType.asClass().isPresent()) {
      constructors = objectHandleType.asClass().get().constructors();
      for (MethodStatement constructor : constructors) {
        this.constructors.add(analyzeConstructor(constructor, context.getImportConsumer()));
      }
    }
  }

  private Map<String, Object> analyzeConstructor(MethodStatement constructor, Consumer<String> imports) {
    Map<String, Object> constructorDescriptor = new HashMap<>();
    List<Map<String, String>> paramDescriptors = new ArrayList<>();
    for (MethodParam param : constructor.params()) {
      TypeReference type = param.type();
      type.dependencyTypenames().forEach(imports);
      paramDescriptors.add(Map.of(
          "name", param.name(),
          "type", type.actualDeclaration(context::simpleNameOf))
      );
      type.asCustomTypeReference().ifPresent(t -> imports.accept(t.targetType().canonicalName()));
    }
    constructorDescriptor.put("params", paramDescriptors);
    return constructorDescriptor;
  }

  @Override
  protected void analyzeMethod(MethodStatement method, ObjectReferenceForm targetForm, int methodIndex) {
    super.analyzeMethod(method, targetForm, methodIndex);
    analyzeGuideMethod(method, targetForm, methodIndex);
  }

  protected void analyzeGuideMethod(MethodStatement domainMethod, ObjectReferenceForm targetForm, int methodIndex) {
    if (domainMethod.isDefault()) {
      return;
    }
    if (NameConventionFunctions.isConversionMethod(domainMethod)) {
      this.guideActions.add(buildConversionGuideAction(domainMethod));
      this.objectHandleMethods.add(buildBaseMethodDescriptor(domainMethod, methodIndex, context));
      return;
    }

    List<MethodStatement> objectHandleMethods = annotatedType.actualMethods();
    MethodStatement guideMethod = findGuideMethod(domainMethod, objectHandleMethods, targetForm);
    this.objectHandleMethods.add(buildBaseMethodDescriptor(domainMethod, methodIndex, context));
    if (guideMethod == null || guideMethod.isAbstract()) {
      this.guideActions.add("null");
    } else {
      this.guideActions.add(
          GuideProcessorFunctions.buildGuideAction(getGeneratedClassCanonicalName(), guideMethod, context)
      );
      this.guideMethods.add(GuideProcessorFunctions.buildGuideActionMethod(guideMethod, context));
      this.objectHandleMethods.add(buildGuideMethodDescriptor(guideMethod, context));
    }
  }

  private Map<String, Object> buildBaseMethodDescriptor(
      MethodStatement domainMethod, int ordinal, JavaArtifactContext context
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", domainMethod.name());

    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : domainMethod.params()) {
      paramClasses.add(buildGuideParamClassName(param.type(), context));
    }
    map.put("params", paramClasses);
    map.put("purpose", "base");
    map.put("ordinal", ordinal);
    map.put("channelClass", context.addToImportAndGetSimpleName(NameConventionFunctions.getChannelClassCanonicalName(domainMethod)));
    map.put("traverseType", ChannelFunctions.getTraverseType(domainMethod).name());

    return map;
  }

  private Map<String, Object> buildGuideMethodDescriptor(
      MethodStatement guidMethod, JavaArtifactContext context
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", ObjectHandleFunctions.buildObjectHandleGuideMethodName(guidMethod));

    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : guidMethod.params()) {
      paramClasses.add(buildGuideParamClassName(param.type(), context));
    }
    map.put("params", paramClasses);
    map.put("purpose", "guide");
    return map;
  }

  private String buildGuideParamClassName(TypeReference type, JavaArtifactContext context) {
    return ObjectHandleFunctions.getObjectHandleDeclaration(
        GuideProcessorFunctions.normalizeType(type),
        ObjectHandleTypes.Common,
        false,
        context::addToImportAndGetSimpleName
    );
  }

  private String buildConversionGuideAction(MethodStatement method) {
    CustomTypeReference parentType = method.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow();
    CustomTypeReference rawParentType = CustomTypeReferences.get(parentType.targetType());

    var sb = new StringBuilder();
    sb.append("FunctionActions.ofFunction(");
    sb.append(context.addToImportAndGetSimpleName(getGeneratedClassCanonicalName()));
    sb.append("::_as");
    sb.append(StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(parentType.targetType().simpleName(), "Domain")));
    sb.append(", ");
    sb.append(getObjectHandleDeclaration(rawParentType, getObjectHandleType()));
    sb.append(".class, ");
    sb.append(context.addToImportAndGetSimpleName(getGeneratedClassCanonicalName()));
    sb.append(".class)");
    return sb.toString();
  }

  protected Map<String, String> generateMethod(
      MethodStatement domainMethod, ObjectReferenceForm targetForm, int methodIndex
  ) {
    if (isDisableMoving(domainMethod)) {
      return Map.of();
    }

    var sb = new StringBuilder();
    sb.append("@Ordinal(").append(methodIndex).append(")\n");
    sb.append("public ");
    appendMethodTypeParameters(sb, domainMethod);
    appendMethodReturnHandleType(sb, domainMethod, targetForm);
    sb.append(" ");
    sb.append(getMethodName(domainMethod, targetForm));
    sb.append("(");
    appendMethodParameters(sb, domainMethod);
    sb.append(")");
    appendMethodExceptions(sb, domainMethod);
    sb.append(" {\n");
    sb.append("  return ");
    if (targetForm == ObjectReferenceForms.Primitive) {
      CustomType ct = domainMethod.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();
      String typename = ClassFunctions.getPrimitiveTypeOfWrapper(ct.canonicalName());
      if (PrimitiveTypes.Boolean.typename().equals(typename)) {
        sb.append("PrimitiveFunctions.longToBoolean(");
        buildInvokeMethodAction(domainMethod, targetForm, methodIndex, sb);
        sb.append(")");
      } else {
        sb.append("(");
        appendMethodReturnHandleType(sb, domainMethod, targetForm);
        sb.append(") ");
        buildInvokeMethodAction(domainMethod, targetForm, methodIndex, sb);
      }
    } else {
      sb.append("(");
      appendMethodReturnHandleType(sb, domainMethod, targetForm);
      sb.append(") ");
      buildInvokeMethodAction(domainMethod, targetForm, methodIndex, sb);
    }
    sb.append(";\n}");
    return Map.of("declaration", sb.toString());
  }

  private void buildInvokeMethodAction(
      MethodStatement domainMethod, ObjectReferenceForm targetForm, int methodIndex, StringBuilder sb
  ) {
    sb.append("$instance.getMethodAction(");
    sb.append(methodIndex);
    sb.append(").castToAction");
    sb.append(domainMethod.params().size() + 1);
    sb.append("().");
    sb.append(buildExecuteMethod(domainMethod, targetForm));
    sb.append("(this");
    for (MethodParam param : domainMethod.params()) {
      sb.append(", ");
      addMethodParam(sb, param);
    }
    sb.append(")");
  }

  private void addMethodParam(StringBuilder sb, MethodParam param) {
    if (param.type().isPrimitiveReference()) {
      String typename = param.type().asPrimitiveReferenceOrElseThrow().typename();
      if (PrimitiveTypes.Boolean.typename().equals(typename)) {
        sb.append("PrimitiveFunctions.booleanToInt(").append(param.name()).append(")");
      } else if (PrimitiveTypes.Char.typename().equals(typename)) {
        sb.append("(int) ").append(param.name());
      } else if (PrimitiveTypes.Byte.typename().equals(typename)) {
        sb.append("(int) ").append(param.name());
      } else if (PrimitiveTypes.Short.typename().equals(typename)) {
        sb.append("(int) ").append(param.name());
      } else {
        sb.append(param.name());
      }
    } else {
      sb.append(param.name());
    }
  }

  private String buildExecuteMethod(MethodStatement domainMethod, ObjectReferenceForm targetForm) {
    if (targetForm == ObjectReferenceForms.Object) {
      return "execute";
    } else if (targetForm == ObjectReferenceForms.Primitive) {
      CustomType ct = domainMethod.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();
      String typename = ClassFunctions.getPrimitiveTypeOfWrapper(ct.canonicalName());
      if (tech.intellispaces.entity.object.ObjectFunctions.equalsAnyOf(
          typename,
          PrimitiveTypes.Boolean.typename(),
          PrimitiveTypes.Char.typename(),
          PrimitiveTypes.Byte.typename(),
          PrimitiveTypes.Short.typename(),
          PrimitiveTypes.Int.typename(),
          PrimitiveTypes.Long.typename()
      )) {
        return "executeReturnInt";
      } else if (PrimitiveTypes.Float.typename().equals(typename) || PrimitiveTypes.Double.typename().equals(typename)) {
        return "executeReturnDouble";
      } else {
        return "execute";
      }
    } else {
      throw UnexpectedExceptions.withMessage("Unsupported guide form - {0}", targetForm);
    }
  }

  @Override
  protected Map<String, String> buildConversionMethod(CustomTypeReference parent) {
    var sb = new StringBuilder();
    sb.append("private ");
    sb.append(getObjectHandleDeclaration(parent, getObjectHandleType()));
    sb.append(" _");
    sb.append(NameConventionFunctions.getConversionMethodName(parent));
    sb.append("() {\n");
    sb.append("  return new ");

    Optional<CustomTypeReference> aliasBaseDomain = DomainFunctions.getAliasBaseDomain(domainType);
    CustomType actualDomain = aliasBaseDomain.isPresent() ? aliasBaseDomain.get().targetType() : domainType;
    sb.append(context.addToImportAndGetSimpleName(
        NameConventionFunctions.getDownwardObjectHandleTypename(actualDomain, parent.targetType(), getObjectHandleType())
    ));
    sb.append("(this);\n");
    sb.append("}");
    return Map.of(
        "declaration", sb.toString()
    );
  }

  private boolean isInjectionMethod(MethodStatement method) {
    return method.hasAnnotation(Inject.class) || method.hasAnnotation(AutoGuide.class);
  }

  private boolean isAutoGuideMethod(MethodStatement method) {
    return method.hasAnnotation(AutoGuide.class);
  }

  private boolean isReturnGuide(MethodStatement method) {
    return GuideFunctions.isGuideType(method.returnType().orElseThrow());
  }
}
