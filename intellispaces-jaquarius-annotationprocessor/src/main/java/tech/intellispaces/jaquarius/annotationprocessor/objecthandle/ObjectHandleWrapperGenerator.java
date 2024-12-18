package tech.intellispaces.jaquarius.annotationprocessor.objecthandle;

import tech.intellispaces.action.runnable.RunnableAction;
import tech.intellispaces.action.text.StringActions;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.TemplatedJavaArtifactGenerator;
import tech.intellispaces.general.exception.UnexpectedExceptions;
import tech.intellispaces.general.text.StringFunctions;
import tech.intellispaces.general.type.ClassFunctions;
import tech.intellispaces.general.type.ClassNameFunctions;
import tech.intellispaces.general.type.PrimitiveTypes;
import tech.intellispaces.jaquarius.annotation.AutoGuide;
import tech.intellispaces.jaquarius.annotation.Inject;
import tech.intellispaces.jaquarius.annotation.Movable;
import tech.intellispaces.jaquarius.annotation.Unmovable;
import tech.intellispaces.jaquarius.annotationprocessor.GuideProcessorFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactGenerator;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleMethodPurposes;
import tech.intellispaces.jaquarius.exception.ConfigurationExceptions;
import tech.intellispaces.jaquarius.guide.GuideFunctions;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.ObjectHandleFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleType;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleTypes;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodParam;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.CustomTypeReference;
import tech.intellispaces.java.reflection.reference.CustomTypeReferences;
import tech.intellispaces.java.reflection.reference.NamedReference;
import tech.intellispaces.java.reflection.reference.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

abstract class ObjectHandleWrapperGenerator extends JaquariusArtifactGenerator {
  protected final List<Map<String, String>> methods = new ArrayList<>();
  protected final List<Map<String, String>> conversionMethods = new ArrayList<>();

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
  protected final List<Map<String, Object>> wrapperMethods = new ArrayList<>();
  protected final List<Map<String, String>> guideMethods = new ArrayList<>();
  protected final List<Map<String, Object>> injectionMethods = new ArrayList<>();

  ObjectHandleWrapperGenerator(CustomType objectHandleType) {
    super(objectHandleType);

    this.domainType = ObjectHandleFunctions.getDomainTypeOfObjectHandle(objectHandleType);
    this.domainMethods = domainType.actualMethods().stream()
        .filter(m -> !m.isDefault())
        .filter(m -> excludeDeepConversionMethods(m, domainType))
        .filter(DomainFunctions::isNotDomainClassGetter)
        .toList();
  }

  abstract protected ObjectHandleType getObjectHandleType();

  protected void analyzeObjectHandleMethods(CustomType type, ArtifactGeneratorContext context) {
    int methodOrdinal = 0;
    for (MethodStatement method : domainMethods) {
      analyzeMethod(method, ObjectReferenceForms.Common, methodOrdinal++);
      if (method.returnType().orElseThrow().isCustomTypeReference()) {
        CustomType returnType = method.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();
        if (ClassFunctions.isPrimitiveWrapperClass(returnType.canonicalName())) {
          analyzeMethod(method, ObjectReferenceForms.Primitive, methodOrdinal++);
        }
      }
    }
  }

  protected void analyzeConversionMethods(CustomType domainType) {
    analyzeConversionMethods(domainType, domainType);
  }

  private void analyzeConversionMethods(CustomType customType, CustomType effectiveCustomType) {
    Iterator<CustomTypeReference> parents = customType.parentTypes().iterator();
    Iterator<CustomTypeReference> effectiveParents = effectiveCustomType.parentTypes().iterator();
    while (parents.hasNext() && effectiveParents.hasNext()) {
      CustomTypeReference parent = parents.next();
      CustomTypeReference effectiveParent = effectiveParents.next();
      if (DomainFunctions.isAliasOf(parent, customType)) {
        analyzeConversionMethods(parent.targetType(), effectiveParent.effectiveTargetType());
      } else {
        conversionMethods.add(buildConversionMethod(CustomTypeReferences.get(effectiveParent.effectiveTargetType())));
      }
    }
  }

  protected String getMethodName(MethodStatement method, ObjectReferenceForm targetForm) {
    if (ObjectReferenceForms.Common.is(targetForm)) {
      return method.name();
    } else if (ObjectReferenceForms.Primitive.is(targetForm)) {
      return method.name() + "Primitive";
    } else {
      throw UnexpectedExceptions.withMessage("Unsupported guide form - {0}", targetForm);
    }
  }

  private void appendMethodReturnHandleType(StringBuilder sb, MethodStatement method, ObjectReferenceForm targetForm) {
    if (ObjectReferenceForms.Common.is(targetForm)) {
      appendMethodReturnHandleType(sb, method);
    } else if (ObjectReferenceForms.Primitive.is(targetForm)) {
      CustomType ct = method.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();
      sb.append(ClassFunctions.getPrimitiveTypeOfWrapper(ct.canonicalName()));
    } else {
      throw UnexpectedExceptions.withMessage("Unsupported guide form - {0}", targetForm);
    }
  }

  protected void appendMethodReturnHandleType(StringBuilder sb, MethodStatement method) {
    TypeReference domainReturnType = method.returnType().orElseThrow();
    if (NameConventionFunctions.isConversionMethod(method)) {
      sb.append(buildObjectHandleDeclaration(domainReturnType, getObjectHandleType()));
    } else {
      if (method.hasAnnotation(Movable.class)) {
        sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Movable));
      } else if (method.hasAnnotation(Unmovable.class)) {
        sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Unmovable));
      } else {
        sb.append(buildObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.General));
      }
    }
  }

  private void appendMethodTypeParameters(StringBuilder sb, MethodStatement method) {
    if (!method.typeParameters().isEmpty()) {
      sb.append("<");
      RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
      for (NamedReference namedTypeReference : method.typeParameters()) {
        commaAppender.run();
        sb.append(namedTypeReference.actualDeclaration());
      }
      sb.append("> ");
    }
  }

  private void appendMethodParameters(StringBuilder sb, MethodStatement method) {
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
    for (MethodParam param : GuideProcessorFunctions.rearrangementParams(method.params())) {
      commaAppender.run();
      sb.append(buildObjectHandleDeclaration(GuideProcessorFunctions.normalizeType(param.type()), ObjectHandleTypes.General));
      sb.append(" ");
      sb.append(param.name());
    }
  }

  private void appendMethodExceptions(StringBuilder sb, MethodStatement method) {
    String exceptions = method.exceptions().stream()
        .map(e -> e.asCustomTypeReference().orElseThrow().targetType())
        .peek(e -> addImport(e.canonicalName()))
        .map(e -> simpleNameOf(e.canonicalName()))
        .collect(Collectors.joining(", "));
    if (!exceptions.isEmpty()) {
      sb.append(" throws ").append(exceptions);
    }
  }

  private boolean excludeDeepConversionMethods(MethodStatement method, CustomType customType) {
    if (!NameConventionFunctions.isConversionMethod(method)) {
      return true;
    }
    for (CustomTypeReference parent : customType.parentTypes()) {
      if (DomainFunctions.isAliasOf(parent, customType)) {
        if (excludeDeepConversionMethods(method, parent.targetType())) {
          return true;
        }
      }
      if (NameConventionFunctions.getConversionMethodName(parent).equals(method.name())) {
        return true;
      }
    }
    return false;
  }

  protected void analyzeReleaseMethod() {
    Optional<MethodStatement> releaseMethod = sourceArtifact().declaredMethod("release", List.of());
    implRelease = releaseMethod.isPresent() && !releaseMethod.get().isAbstract();
  }

  protected void analyzeDomain() {
    CustomType domainType = ObjectHandleFunctions.getDomainTypeOfObjectHandle(sourceArtifact());
    addImport(domainType.canonicalName());
    domainSimpleClassName = simpleNameOf(domainType.canonicalName());

    List<CustomTypeReference> equivalentDomains = DomainFunctions.getEquivalentDomains(domainType);
    isAlias = !equivalentDomains.isEmpty();
    if (isAlias) {
      CustomTypeReference nearEquivalentDomain = equivalentDomains.get(0);
      CustomTypeReference mainEquivalentDomain = equivalentDomains.get(equivalentDomains.size() - 1);

      primaryDomainSimpleName = addToImportAndGetSimpleName(mainEquivalentDomain.targetType().canonicalName());
      primaryDomainTypeArguments = nearEquivalentDomain.typeArgumentsDeclaration(this::addToImportAndGetSimpleName);
    }
  }

  protected void analyzeInjectedGuides() {
    for (MethodStatement method : sourceArtifact().declaredMethods()) {
      if (method.isAbstract()) {
        if (isInjectionMethod(method)) {
          if (!isReturnGuide(method)) {
            throw ConfigurationExceptions.withMessage("Guide injection method '{0}' in class {1} must return guide",
                method.name(), sourceArtifact().className()
            );
          }
          if (isAutoGuideMethod(method)) {
            addAutoGuideInjectionMethod(method);
          } else {
            addSpecGuideInjectionMethod(method);
          }
        } else {
          throw ConfigurationExceptions.withMessage("Undefined abstract method '{0}' in class {1}",
              method.name(), sourceArtifact().className()
          );
        }
      }
    }
  }

  private void addAutoGuideInjectionMethod(MethodStatement method) {
    int injectionOrdinal = injectionMethods.size();
    String injectionType = method.returnType().orElseThrow().actualDeclaration();

    Map<String, Object> methodProperties = new HashMap<>();
    methodProperties.put("javadoc", "");
    methodProperties.put("annotations", List.of(Override.class.getSimpleName()));
    methodProperties.put("signature", buildMethodSignature(method));
    methodProperties.put("body", buildInjectionMethodBody(injectionType, injectionOrdinal));
    injectionMethods.add(methodProperties);

    this.wrapperMethods.add(buildAutoGuideInjectionMethodDescriptor(method, injectionOrdinal, this));
  }

  private void addSpecGuideInjectionMethod(MethodStatement method) {
    int injectionOrdinal = injectionMethods.size();
    String injectionType = method.returnType().orElseThrow().actualDeclaration();

    Map<String, Object> methodProperties = new HashMap<>();
    methodProperties.put("javadoc", "");
    methodProperties.put("annotations", List.of(Override.class.getSimpleName()));
    methodProperties.put("signature", buildMethodSignature(method));
    methodProperties.put("body", buildInjectionMethodBody(injectionType, injectionOrdinal));
    injectionMethods.add(methodProperties);

    this.wrapperMethods.add(buildSpecGuideInjectionMethodDescriptor(method, injectionOrdinal, this));
  }

  private String buildInjectionMethodBody(String injectionType, int injectionIndex) {
    return "return (" + injectionType + ") this.$broker.injection(" + injectionIndex + ").value();";
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
          domainMethodParam.type(), ObjectHandleTypes.General, Function.identity()
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
    return NameConventionFunctions.getObjectHandleWrapperCanonicalName(sourceArtifact());
  }

  protected void analyzeTypeParams() {
    if (sourceArtifact().typeParameters().isEmpty()) {
      typeParamsFull = typeParamsBrief = "";
      return;
    }

    var typeParamsFullBuilder = new StringBuilder();
    var typeParamsBriefBuilder = new StringBuilder();
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(typeParamsFullBuilder, typeParamsBriefBuilder);

    typeParamsFullBuilder.append("<");
    typeParamsBriefBuilder.append("<");
    for (NamedReference typeParam : sourceArtifact().typeParameters()) {
      commaAppender.run();
      typeParamsFullBuilder.append(typeParam.formalFullDeclaration());
      typeParamsBriefBuilder.append(typeParam.formalBriefDeclaration());
    }
    typeParamsFullBuilder.append(">");
    typeParamsBriefBuilder.append(">");
    typeParamsFull = typeParamsFullBuilder.toString();
    typeParamsBrief = typeParamsBriefBuilder.toString();
  }

  protected void analyzeConstructors() {
    List<MethodStatement> constructors;
    if (sourceArtifact().asClass().isPresent()) {
      constructors = sourceArtifact().asClass().get().constructors();
      for (MethodStatement constructor : constructors) {
        this.constructors.add(analyzeConstructor(constructor, getImportConsumer()));
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
          "type", type.actualDeclaration(this::simpleNameOf))
      );
      type.asCustomTypeReference().ifPresent(t -> imports.accept(t.targetType().canonicalName()));
    }
    constructorDescriptor.put("params", paramDescriptors);
    return constructorDescriptor;
  }

  private void analyzeMethod(MethodStatement method, ObjectReferenceForm targetForm, int methodOrdinal) {
    methods.add(generateMethod(method, targetForm, methodOrdinal));
    analyzeGuideMethod(method, targetForm, methodOrdinal);
  }

  private void analyzeGuideMethod(MethodStatement domainMethod, ObjectReferenceForm targetForm, int methodIndex) {
    if (domainMethod.isDefault()) {
      return;
    }
    if (NameConventionFunctions.isConversionMethod(domainMethod)) {
      this.wrapperMethods.add(buildTraverseMethodDescriptor(domainMethod, methodIndex, this));
      this.wrapperMethods.add(buildConversionGuideMethodDescriptor(domainMethod, methodIndex, this));
      return;
    }

    List<MethodStatement> objectHandleMethods = sourceArtifact().actualMethods();
    MethodStatement guideMethod = findGuideMethod(domainMethod, objectHandleMethods, targetForm);
    this.wrapperMethods.add(buildTraverseMethodDescriptor(domainMethod, methodIndex, this));
    if (guideMethod != null && !guideMethod.isAbstract()) {
      this.guideMethods.add(GuideProcessorFunctions.buildGuideActionMethod(guideMethod, this));
      this.wrapperMethods.add(buildGuideMethodDescriptor(guideMethod, methodIndex, this));
    }
  }

  private Map<String, Object> buildTraverseMethodDescriptor(
      MethodStatement domainMethod, int ordinal, TemplatedJavaArtifactGenerator generator
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", domainMethod.name());

    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : domainMethod.params()) {
      paramClasses.add(buildGuideParamClassName(param.type(), generator));
    }
    map.put("params", paramClasses);
    map.put("purpose", ObjectHandleMethodPurposes.TraverseMethod.name());
    map.put("traverseOrdinal", ordinal);
    map.put("channelClass", generator.addToImportAndGetSimpleName(NameConventionFunctions.getChannelClassCanonicalName(domainMethod)));
    map.put("traverseType", ChannelFunctions.getTraverseType(domainMethod).name());

    return map;
  }

  private Map<String, Object> buildGuideMethodDescriptor(
      MethodStatement guidMethod, int ordinal, TemplatedJavaArtifactGenerator generator
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", ObjectHandleFunctions.buildObjectHandleGuideMethodName(guidMethod));

    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : guidMethod.params()) {
      paramClasses.add(buildGuideParamClassName(param.type(), generator));
    }
    map.put("params", paramClasses);
    map.put("purpose", ObjectHandleMethodPurposes.GuideMethod.name());
    map.put("traverseOrdinal", ordinal);
    return map;
  }

  private Map<String, Object> buildConversionGuideMethodDescriptor(
      MethodStatement domainMethod, int ordinal, TemplatedJavaArtifactGenerator generator
  ) {
    var map = new HashMap<String, Object>();

    CustomTypeReference parentType = domainMethod.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow();
    String methodName = "_as" +
        StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(parentType.targetType().simpleName(), "Domain"));
    map.put("name", methodName);

    map.put("params", List.of());
    map.put("purpose", ObjectHandleMethodPurposes.GuideMethod.name());
    map.put("traverseOrdinal", ordinal);
    return map;
  }

  private Map<String, Object> buildAutoGuideInjectionMethodDescriptor(
      MethodStatement injectionMethod, int ordinal, TemplatedJavaArtifactGenerator generator
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", injectionMethod.name());
    map.put("params", List.of());
    map.put("purpose", ObjectHandleMethodPurposes.InjectionMethod.name());

    map.put("injectionKind", "autoguide");
    map.put("injectionOrdinal", ordinal);
    map.put("injectionName", injectionMethod.name());
    map.put("injectionType", injectionMethod.returnType().orElseThrow().actualDeclaration(generator::addToImportAndGetSimpleName));
    return map;
  }

  private Map<String, Object> buildSpecGuideInjectionMethodDescriptor(
      MethodStatement injectionMethod, int ordinal, TemplatedJavaArtifactGenerator generator
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", injectionMethod.name());
    map.put("params", List.of());
    map.put("purpose", ObjectHandleMethodPurposes.InjectionMethod.name());

    map.put("injectionKind", "specguide");
    map.put("injectionOrdinal", ordinal);
    map.put("injectionName", injectionMethod.name());
    map.put("injectionType", injectionMethod.returnType().orElseThrow().actualDeclaration(generator::addToImportAndGetSimpleName));
    return map;
  }

  private String buildGuideParamClassName(TypeReference type, TemplatedJavaArtifactGenerator generator) {
    return ObjectHandleFunctions.getObjectHandleDeclaration(
        GuideProcessorFunctions.normalizeType(type),
        ObjectHandleTypes.General,
        false,
        generator::addToImportAndGetSimpleName
    );
  }

  protected Map<String, String> generateMethod(
      MethodStatement domainMethod, ObjectReferenceForm targetForm, int methodOrdinal
  ) {
    var sb = new StringBuilder();
    sb.append("@Ordinal(").append(methodOrdinal).append(")\n");
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
    if (ObjectReferenceForms.Primitive.is(targetForm)) {
      CustomType ct = domainMethod.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();
      String typename = ClassFunctions.getPrimitiveTypeOfWrapper(ct.canonicalName());
      if (PrimitiveTypes.Boolean.typename().equals(typename)) {
        sb.append("PrimitiveFunctions.longToBoolean(");
        buildInvokeMethodAction(domainMethod, targetForm, methodOrdinal, sb);
        sb.append(")");
      } else {
        sb.append("(");
        appendMethodReturnHandleType(sb, domainMethod, targetForm);
        sb.append(") ");
        buildInvokeMethodAction(domainMethod, targetForm, methodOrdinal, sb);
      }
    } else {
      sb.append("(");
      appendMethodReturnHandleType(sb, domainMethod, targetForm);
      sb.append(") ");
      buildInvokeMethodAction(domainMethod, targetForm, methodOrdinal, sb);
    }
    sb.append(";\n}");
    return Map.of("declaration", sb.toString());
  }

  private void buildInvokeMethodAction(
      MethodStatement domainMethod, ObjectReferenceForm targetForm, int methodIndex, StringBuilder sb
  ) {
    sb.append("$broker.methodAction(");
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
    if (ObjectReferenceForms.Common.is(targetForm)) {
      return "execute";
    } else if (ObjectReferenceForms.Primitive.is(targetForm)) {
      CustomType ct = domainMethod.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();
      String typename = ClassFunctions.getPrimitiveTypeOfWrapper(ct.canonicalName());
      if (tech.intellispaces.general.object.ObjectFunctions.equalsAnyOf(
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

  private Map<String, String> buildConversionMethod(CustomTypeReference parent) {
    var sb = new StringBuilder();
    sb.append("private ");
    sb.append(buildObjectHandleDeclaration(parent, getObjectHandleType()));
    sb.append(" _");
    sb.append(NameConventionFunctions.getConversionMethodName(parent));
    sb.append("() {\n");
    sb.append("  return new ");

    Optional<CustomTypeReference> aliasBaseDomain = DomainFunctions.getAliasBaseDomain(domainType);
    CustomType actualDomain = aliasBaseDomain.isPresent() ? aliasBaseDomain.get().targetType() : domainType;
    sb.append(addToImportAndGetSimpleName(
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
