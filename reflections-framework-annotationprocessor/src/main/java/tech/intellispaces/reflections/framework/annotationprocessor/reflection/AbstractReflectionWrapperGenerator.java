package tech.intellispaces.reflections.framework.annotationprocessor.reflection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import tech.intellispaces.actions.runnable.RunnableAction;
import tech.intellispaces.actions.text.StringActions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.commons.type.ClassNameFunctions;
import tech.intellispaces.commons.type.PrimitiveTypes;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.method.MethodParam;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.javareflection.reference.CustomTypeReference;
import tech.intellispaces.javareflection.reference.NamedReference;
import tech.intellispaces.javareflection.reference.TypeReference;
import tech.intellispaces.reflections.framework.annotationprocessor.AnnotationGeneratorFunctions;
import tech.intellispaces.reflections.framework.annotationprocessor.domain.AbstractReflectionFormGenerator;
import tech.intellispaces.reflections.framework.exception.ConfigurationExceptions;
import tech.intellispaces.reflections.framework.guide.GuideFunctions;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.reflection.MovabilityTypes;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;
import tech.intellispaces.reflections.framework.reflection.ReflectionRealizationMethodPurposes;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;
import tech.intellispaces.reflections.framework.space.domain.DomainFunctions;

abstract class AbstractReflectionWrapperGenerator extends AbstractReflectionFormGenerator {
  protected String domainSimpleClassName;
  protected String typeParamsFull;
  protected String typeParamsBrief;
  protected String domainTypeParamsBrief;
  protected boolean isAlias;
  protected String primaryDomainSimpleName;
  protected String primaryDomainTypeArguments;
  protected boolean implRelease;
  protected String domainTypeDeclaration;
  protected String primaryDomainTypeDeclaration;
  private List<MethodStatement> domainMethods;
  protected final List<Map<String, Object>> constructors = new ArrayList<>();
  protected final List<Map<String, String>> traverseMethods = new ArrayList<>();
  protected final List<Map<String, String>> guideMethods = new ArrayList<>();
  protected final List<Map<String, Object>> injectionMethods = new ArrayList<>();
  protected final List<Map<String, Object>> methodDescriptions = new ArrayList<>();

  AbstractReflectionWrapperGenerator(CustomType reflectionType) {
    super(reflectionType);
  }

  protected void analyzeDomain() {
    domainType = ReflectionFunctions.getDomainOfObjectFormOrElseThrow(sourceArtifact());
    domainRid = DomainFunctions.getDomainId(domainType);
    domainType = ReflectionFunctions.getDomainOfObjectFormOrElseThrow(sourceArtifact());
    addImport(domainType.canonicalName());
    domainSimpleClassName = simpleNameOf(domainType.canonicalName());
    domainTypeParamsBrief = sourceArtifact().typeParametersBriefDeclaration();

    domainMethods = domainType.actualMethods().stream()
        .filter(m -> !m.isDefault())
        .filter(m -> excludeDeepConversionMethods(m, domainType))
        .filter(DomainFunctions::isNotDomainClassGetter)
        .filter(this::isReflectionReturnType)
        .toList();
  }

  private boolean isReflectionReturnType(MethodStatement method) {
    TypeReference returnType = method.returnType().orElseThrow();
    if (returnType.isCustomTypeReference()) {
      return DomainFunctions.isDomainType(returnType.asCustomTypeReferenceOrElseThrow().targetType());
    }
    return true;
  }

  @SuppressWarnings("unchecked,rawtypes")
  protected void analyzeAlias() {
    domainTypeDeclaration = buildDomainType(domainType, (List) domainType.typeParameters());

    List<CustomTypeReference> equivalentDomains = DomainFunctions.getEquivalentDomains(domainType);
    isAlias = !equivalentDomains.isEmpty();
    if (isAlias) {
      CustomTypeReference nearEquivalentDomain = equivalentDomains.get(0);
      CustomTypeReference mainEquivalentDomain = equivalentDomains.get(equivalentDomains.size() - 1);

      primaryDomainTypeArguments = getDomainTypeParamsBrief(nearEquivalentDomain);
      primaryDomainSimpleName = addImportAndGetSimpleName(mainEquivalentDomain.targetType().canonicalName());
      primaryDomainTypeDeclaration = buildDomainType(mainEquivalentDomain.targetType(), mainEquivalentDomain.typeArguments());
    } else {
      primaryDomainTypeArguments = domainTypeParamsBrief;
      primaryDomainSimpleName = addImportAndGetSimpleName(domainType.canonicalName());
      primaryDomainTypeDeclaration = domainTypeDeclaration;
    }
  }

  protected String getReflectionSimpleName() {
    final String canonicalName;
    if (ReflectionFunctions.isUnmovableReflection(sourceArtifact())) {
      canonicalName = NameConventionFunctions.getUnmovableReflectionTypeName(domainType.canonicalName(), true);
    } else if (ReflectionFunctions.isMovableReflection(sourceArtifact())) {
      canonicalName = NameConventionFunctions.getMovableReflectionTypeName(domainType.canonicalName(), true);
    } else {
      throw UnexpectedExceptions.withMessage("Could not define movable type of the reflection {0}",
          sourceArtifact().canonicalName());
    }
    return addImportAndGetSimpleName(canonicalName);
  }

  protected String getMovableReflectionSimpleName() {
    return addImportAndGetSimpleName(
        NameConventionFunctions.getMovableReflectionTypeName(domainType.canonicalName(), true)
    );
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

  protected void analyzeReflectionMethods() {
    int methodOrdinal = 0;
    for (MethodStatement method : domainMethods) {
      analyzeRawDomainMethod(method);
      if (hasMethodNormalForm(method)) {
        if (includeMethodForm(method, ReflectionForms.Reflection)) {
          analyzeMethod(method, ReflectionForms.Reflection, methodOrdinal++);
        }
      }
      if (includeMethodForm(method, ReflectionForms.Primitive)) {
        analyzeMethod(method, ReflectionForms.Primitive, methodOrdinal++);
      }
    }
  }

  private void analyzeMethod(
          MethodStatement method, ReflectionForm targetForm, int methodOrdinal
  ) {
    traverseMethods.add(generateTraverseMethod(method, targetForm, methodOrdinal));
    analyzeGuideMethod(method, targetForm, methodOrdinal);
  }

  private void analyzeGuideMethod(
          MethodStatement domainMethod, ReflectionForm targetForm, int methodOrdinal
  ) {
    if (domainMethod.isDefault()) {
      return;
    }

    List<MethodStatement> reflectionMethods = sourceArtifact().actualMethods();
    this.methodDescriptions.add(buildTraverseMethodDescriptions(domainMethod, targetForm, methodOrdinal));
    MethodStatement guideMethod = findGuideMethod(domainMethod, reflectionMethods, targetForm);
    if (guideMethod != null && !guideMethod.isAbstract()) {
      this.guideMethods.add(AnnotationGeneratorFunctions.buildGuideActionMethod(guideMethod, this));
      this.methodDescriptions.add(buildGuideMethodDescriptions(guideMethod, methodOrdinal));
    }
  }

  protected void analyzeInjectedGuides() {
    for (MethodStatement method : sourceArtifact().declaredMethods()) {
      if (method.isAbstract()) {
        if (AnnotationGeneratorFunctions.isInjectionMethod(method)) {
          if (!AnnotationGeneratorFunctions.returnTypeIsGuide(method)) {
            throw ConfigurationExceptions.withMessage("Guide injection method '{0}' in class {1} must return guide",
                method.name(), sourceArtifact().className()
            );
          }
          if (AnnotationGeneratorFunctions.isAutoGuideMethod(method)) {
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

  protected void analyzeUnbindMethod() {
    Optional<MethodStatement> unbindMethod = sourceArtifact().declaredMethod("unbind", List.of());
    implRelease = unbindMethod.isPresent() && !unbindMethod.get().isAbstract();
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
    this.methodDescriptions.add(buildAutoGuideInjectionMethodDescriptions(method, injectionOrdinal));
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
    this.methodDescriptions.add(buildSpecGuideInjectionMethodDescriptions(method, injectionOrdinal));
  }

  private String buildInjectionMethodBody(String injectionType, int injectionIndex) {
    return "return (" + injectionType + ") this.$handle.injection(" + injectionIndex + ").value();";
  }

  private MethodStatement findGuideMethod(
      MethodStatement domainMethod,
      List<MethodStatement> reflectionMethods,
      ReflectionForm targetForm
  ) {
    for (MethodStatement method : reflectionMethods) {
      if (!GuideFunctions.isGuideMethod(method)) {
        continue;
      }
      if (getObjectFormMethodName(domainMethod, targetForm).equals(method.name()) &&
          equalParams(domainMethod.params(), method.params())
      ) {
        return method;
      }
    }
    return null;
  }

  private boolean equalParams(
      List<MethodParam> domainMethodParams, List<MethodParam> guideMethodParams
  ) {
    if (domainMethodParams.size() != guideMethodParams.size()) {
      return false;
    }

    boolean paramMatches = true;
    for (int i = 0; i < domainMethodParams.size(); i++) {
      MethodParam domainParam = domainMethodParams.get(i);
      String domainMethodParamDeclaration = ReflectionFunctions.getObjectFormDeclaration(
          domainParam.type(), ReflectionForms.Reflection, MovabilityTypes.General, true, false, Function.identity()
      );

      MethodParam guideParam = guideMethodParams.get(i);
      String guideMethodParamDeclaration = guideParam.type().actualDeclaration(ClassNameFunctions::getShortenName);
      if (!domainMethodParamDeclaration.equals(guideMethodParamDeclaration)) {
        paramMatches = false;
        break;
      }
    }
    return paramMatches;
  }

  protected String getGeneratedClassCanonicalName() {
    return NameConventionFunctions.getReflectionWrapperCanonicalName(sourceArtifact());
  }

  private Map<String, Object> buildTraverseMethodDescriptions(
          MethodStatement domainMethod, ReflectionForm targetForm, int ordinal
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", getObjectFormMethodName(domainMethod, targetForm));

    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : domainMethod.params()) {
      paramClasses.add(buildGuideParamClassName(param.type()));
    }
    map.put("params", paramClasses);
    map.put("purpose", ReflectionRealizationMethodPurposes.TraverseMethod.name());
    map.put("traverseOrdinal", ordinal);
    map.put("channelClass", addImportAndGetSimpleName(NameConventionFunctions.getChannelClassCanonicalName(
        domainMethod)));
    map.put("traverseType", ChannelFunctions.getTraverseType(domainMethod).name());
    return map;
  }

  private Map<String, Object> buildGuideMethodDescriptions(
      MethodStatement guidMethod, int ordinal
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", ReflectionFunctions.buildReflectionGuideMethodName(guidMethod));

    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : guidMethod.params()) {
      paramClasses.add(buildGuideParamClassName(param.type()));
    }
    map.put("params", paramClasses);
    map.put("purpose", ReflectionRealizationMethodPurposes.GuideMethod.name());
    map.put("traverseOrdinal", ordinal);
    return map;
  }

  private Map<String, Object> buildAutoGuideInjectionMethodDescriptions(
      MethodStatement injectionMethod, int ordinal
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", injectionMethod.name());
    map.put("params", List.of());
    map.put("purpose", ReflectionRealizationMethodPurposes.InjectionMethod.name());

    map.put("injectionKind", "autoguide");
    map.put("injectionOrdinal", ordinal);
    map.put("injectionName", injectionMethod.name());
    map.put("injectionType", injectionMethod.returnType().orElseThrow().actualDeclaration(
        this::addImportAndGetSimpleName)
    );
    return map;
  }

  private Map<String, Object> buildSpecGuideInjectionMethodDescriptions(MethodStatement injectionMethod, int ordinal) {
    var map = new HashMap<String, Object>();
    map.put("name", injectionMethod.name());
    map.put("params", List.of());
    map.put("purpose", ReflectionRealizationMethodPurposes.InjectionMethod.name());

    map.put("injectionKind", "specguide");
    map.put("injectionOrdinal", ordinal);
    map.put("injectionName", injectionMethod.name());
    map.put("injectionType", injectionMethod.returnType().orElseThrow().actualDeclaration(
        this::addImportAndGetSimpleName)
    );
    return map;
  }

  private String buildGuideParamClassName(TypeReference type) {
    if (type.isNamedReference()) {
      return addImportAndGetSimpleName(Object.class.getCanonicalName());
    }
    return ReflectionFunctions.getObjectFormDeclaration(
        AnnotationGeneratorFunctions.normalizeType(type),
        ReflectionForms.Reflection,
        MovabilityTypes.General,
        false,
        true,
        this::addImportAndGetSimpleName
    );
  }

  protected Map<String, String> generateTraverseMethod(
          MethodStatement domainMethod, ReflectionForm targetForm, int methodOrdinal
  ) {
    var sb = new StringBuilder();
    sb.append("@Ordinal(").append(methodOrdinal).append(")\n");
    sb.append("public ");
    appendMethodTypeParameters(sb, domainMethod);
    appendMethodReturnTypeDeclaration(sb, domainMethod, targetForm);
    sb.append(" ");
    sb.append(getObjectFormMethodName(domainMethod, targetForm));
    sb.append("(");
    appendMethodParams(sb, domainMethod);
    sb.append(")");
    appendMethodExceptions(sb, domainMethod);
    sb.append(" {\n");
    sb.append("  return ");

    TypeReference returnType = domainMethod.returnType().orElseThrow();
    if (returnType.isPrimitiveReference()) {
      if (PrimitiveTypes.Boolean.is(returnType.asPrimitiveReferenceOrElseThrow().primitiveType())) {
        sb.append("PrimitiveFunctions.longToBoolean((int) ");
        appendInvokeMethodAction(sb, domainMethod, targetForm, methodOrdinal);
        sb.append(")");
      } else {
        sb.append("(");
        appendMethodReturnTypeDeclaration(sb, domainMethod, targetForm);
        sb.append(") ");
        appendInvokeMethodAction(sb, domainMethod, targetForm, methodOrdinal);
      }
    } else {
      sb.append("(");
      appendMethodCastReflectionType(sb, domainMethod, targetForm);
      sb.append(") ");
      appendInvokeMethodAction(sb, domainMethod, targetForm, methodOrdinal);
    }
    sb.append(";\n}");
    return Map.of("declaration", sb.toString());
  }

  private void appendMethodCastReflectionType(
      StringBuilder sb, MethodStatement method, ReflectionForm targetForm
  ) {
    if (ReflectionForms.Reflection.is(targetForm)) {
      appendObjectFormMethodReturnType(sb, method);
    } else if (ReflectionForms.Primitive.is(targetForm)) {
      CustomType returnType = method.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();
      sb.append(ClassFunctions.primitiveTypenameOfWrapper(returnType.canonicalName()));
    } else {
      throw UnexpectedExceptions.withMessage("Unsupported guide form - {0}", targetForm);
    }
  }

  private void appendInvokeMethodAction(
      StringBuilder sb,
      MethodStatement domainMethod,
      ReflectionForm targetForm,
      int methodOrdinal
  ) {
    var additionalCloseBracket = false;
    if (ReflectionForms.PrimitiveWrapper.is(targetForm) && isPrimitiveWrapper(domainMethod.returnType().orElseThrow())) {
      CustomType returnType = domainMethod.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();
      String typename = ClassFunctions.primitiveTypenameOfWrapper(returnType.canonicalName());
      if (PrimitiveTypes.Byte.typename().equals(typename)) {
        sb.append("PrimitiveFunctions.intToByte(");
        additionalCloseBracket = true;
      }
    }
    sb.append("$handle.methodAction(");
    sb.append(methodOrdinal);
    sb.append(").castToAction");
    sb.append(domainMethod.params().size() + 1);
    sb.append("().");
    sb.append(buildExecuteMethod(domainMethod, targetForm));
    sb.append("(this");
    for (MethodParam param : domainMethod.params()) {
      sb.append(", ");
      appendMethodParam(sb, param);
    }
    sb.append(")");
    if (additionalCloseBracket) {
      sb.append(")");
    }
  }

  private void appendMethodParam(StringBuilder sb, MethodParam param) {
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

  private String buildExecuteMethod(MethodStatement domainMethod, ReflectionForm targetForm) {
    if (ReflectionForms.Primitive.is(targetForm)) {
      if (isPrimitiveWrapper(domainMethod.returnType().orElseThrow())) {
        CustomType returnType = domainMethod.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();
        String typename = ClassFunctions.primitiveTypenameOfWrapper(returnType.canonicalName());
        if (tech.intellispaces.commons.object.ObjectFunctions.equalsAnyOf(
            typename,
            PrimitiveTypes.Boolean.typename(),
            PrimitiveTypes.Char.typename(),
            PrimitiveTypes.Byte.typename(),
            PrimitiveTypes.Short.typename(),
            PrimitiveTypes.Int.typename()
        )) {
          return "executeReturnInt";
        } else if (PrimitiveTypes.Float.typename().equals(typename) || PrimitiveTypes.Double.typename().equals(typename)) {
          return "executeReturnDouble";
        } else {
          return "execute";
        }
      } else {
        return "execute";
      }
    } else if (ReflectionForms.Reflection.is(targetForm)) {
        return "execute";
    } else {
      throw UnexpectedExceptions.withMessage("Unsupported guide form - {0}", targetForm);
    }
  }
}
