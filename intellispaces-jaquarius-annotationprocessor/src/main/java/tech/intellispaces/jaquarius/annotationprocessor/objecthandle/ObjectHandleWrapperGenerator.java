package tech.intellispaces.jaquarius.annotationprocessor.objecthandle;

import tech.intellispaces.action.runnable.RunnableAction;
import tech.intellispaces.action.text.StringActions;
import tech.intellispaces.general.exception.NotImplementedExceptions;
import tech.intellispaces.general.exception.UnexpectedExceptions;
import tech.intellispaces.general.text.StringFunctions;
import tech.intellispaces.general.type.ClassFunctions;
import tech.intellispaces.general.type.ClassNameFunctions;
import tech.intellispaces.general.type.PrimitiveTypes;
import tech.intellispaces.jaquarius.annotationprocessor.AbstractObjectHandleGenerator;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationGeneratorFunctions;
import tech.intellispaces.jaquarius.engine.description.ObjectHandleMethodPurposes;
import tech.intellispaces.jaquarius.exception.ConfigurationExceptions;
import tech.intellispaces.jaquarius.guide.GuideFunctions;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleMethodForm;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleMethodForms;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleTypes;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodParam;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.CustomTypeReference;
import tech.intellispaces.java.reflection.reference.NamedReference;
import tech.intellispaces.java.reflection.reference.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

abstract class ObjectHandleWrapperGenerator extends AbstractObjectHandleGenerator {
  protected String domainSimpleClassName;
  protected String typeParamsFull;
  protected String typeParamsBrief;
  protected boolean isAlias;
  protected String primaryDomainSimpleName;
  protected String primaryDomainTypeArguments;
  protected boolean implRelease;
  protected CustomType domainType;
  private List<MethodStatement> domainMethods;
  protected final List<Map<String, Object>> generatedConstructors = new ArrayList<>();
  protected final List<Map<String, String>> generatedDomainMethods = new ArrayList<>();
  protected final List<Map<String, String>> generatedGuideMethods = new ArrayList<>();
  protected final List<Map<String, Object>> generatedInjectionMethods = new ArrayList<>();
  protected final List<Map<String, String>> generatedConversionMethods = new ArrayList<>();
  protected final List<Map<String, Object>> generatedMethodDescriptions = new ArrayList<>();

  ObjectHandleWrapperGenerator(CustomType objectHandleType) {
    super(objectHandleType);
  }

  protected void analyzeDomain() {
    domainType = ObjectHandleFunctions.getDomainTypeOfObjectHandle(sourceArtifact());
    addImport(domainType.canonicalName());
    domainSimpleClassName = simpleNameOf(domainType.canonicalName());

    domainMethods = domainType.actualMethods().stream()
        .filter(m -> !m.isDefault())
        .filter(m -> excludeDeepConversionMethods(m, domainType))
        .filter(DomainFunctions::isNotDomainClassGetter)
        .toList();

    List<CustomTypeReference> equivalentDomains = DomainFunctions.getEquivalentDomains(domainType);
    isAlias = !equivalentDomains.isEmpty();
    if (isAlias) {
      CustomTypeReference nearEquivalentDomain = equivalentDomains.get(0);
      CustomTypeReference mainEquivalentDomain = equivalentDomains.get(equivalentDomains.size() - 1);

      primaryDomainSimpleName = addImportAndGetSimpleName(mainEquivalentDomain.targetType().canonicalName());
      primaryDomainTypeArguments = nearEquivalentDomain.typeArgumentsDeclaration(this::addImportAndGetSimpleName);
    }
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
        this.generatedConstructors.add(analyzeConstructor(constructor, getImportConsumer()));
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

  protected void analyzeObjectHandleMethods() {
    int methodOrdinal = 0;
    for (MethodStatement method : domainMethods) {
      analyzeMethod(method, ObjectHandleMethodForms.Object, ObjectReferenceForms.Object, methodOrdinal++);
      if (hasMethodNormalForm(method)) {
        analyzeMethod(method, ObjectHandleMethodForms.Normal, ObjectReferenceForms.Object, methodOrdinal++);
        if (isPrimitiveWrapper(method.returnType().orElseThrow())) {
          analyzeMethod(method, ObjectHandleMethodForms.Normal, ObjectReferenceForms.Primitive, methodOrdinal++);
        }
      }
    }
  }

  private void analyzeMethod(
      MethodStatement method, ObjectHandleMethodForm methodForm, ObjectReferenceForm targetForm, int methodOrdinal
  ) {
    generatedDomainMethods.add(generateMethod(method, methodForm, targetForm, methodOrdinal));
    analyzeGuideMethod(method, methodForm, targetForm, methodOrdinal);
  }

  private void analyzeGuideMethod(
      MethodStatement domainMethod, ObjectHandleMethodForm methodForm, ObjectReferenceForm targetForm, int methodOrdinal
  ) {
    if (domainMethod.isDefault()) {
      return;
    }
    if (NameConventionFunctions.isConversionMethod(domainMethod)) {
      this.generatedMethodDescriptions.add(buildTraverseMethodDescriptions(domainMethod, methodForm, targetForm, methodOrdinal));
      this.generatedMethodDescriptions.add(buildConversionGuideMethodDescriptions(domainMethod, methodOrdinal));
      return;
    }

    List<MethodStatement> objectHandleMethods = sourceArtifact().actualMethods();
    MethodStatement guideMethod = findGuideMethod(domainMethod, objectHandleMethods, methodForm);
    this.generatedMethodDescriptions.add(buildTraverseMethodDescriptions(domainMethod, methodForm, targetForm, methodOrdinal));
    if (guideMethod != null && !guideMethod.isAbstract()) {
      this.generatedGuideMethods.add(AnnotationGeneratorFunctions.buildGuideActionMethod(guideMethod, this));
      this.generatedMethodDescriptions.add(buildGuideMethodDescriptions(guideMethod, methodForm, methodOrdinal));
    }
  }

  protected void analyzeConversionMethods(CustomType domainType) {
    List<CustomTypeReference> parents = DomainFunctions.getEffectiveParents(domainType);
    parents.stream()
        .map(this::buildConversionMethod)
        .forEach(generatedConversionMethods::add);
  }

  protected void analyzeInjectedGuides() {
    for (MethodStatement method : sourceArtifact().declaredMethods()) {
      if (method.isAbstract()) {
        if (AnnotationGeneratorFunctions.isInjectionMethod(method)) {
          if (!AnnotationGeneratorFunctions.isReturnGuide(method)) {
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

  protected void analyzeReleaseMethod() {
    Optional<MethodStatement> releaseMethod = sourceArtifact().declaredMethod("release", List.of());
    implRelease = releaseMethod.isPresent() && !releaseMethod.get().isAbstract();
  }

  private void addAutoGuideInjectionMethod(MethodStatement method) {
    int injectionOrdinal = generatedInjectionMethods.size();
    String injectionType = method.returnType().orElseThrow().actualDeclaration();

    Map<String, Object> methodProperties = new HashMap<>();
    methodProperties.put("javadoc", "");
    methodProperties.put("annotations", List.of(Override.class.getSimpleName()));
    methodProperties.put("signature", buildMethodSignature(method));
    methodProperties.put("body", buildInjectionMethodBody(injectionType, injectionOrdinal));
    generatedInjectionMethods.add(methodProperties);
    this.generatedMethodDescriptions.add(buildAutoGuideInjectionMethodDescriptions(method, injectionOrdinal));
  }

  private void addSpecGuideInjectionMethod(MethodStatement method) {
    int injectionOrdinal = generatedInjectionMethods.size();
    String injectionType = method.returnType().orElseThrow().actualDeclaration();

    Map<String, Object> methodProperties = new HashMap<>();
    methodProperties.put("javadoc", "");
    methodProperties.put("annotations", List.of(Override.class.getSimpleName()));
    methodProperties.put("signature", buildMethodSignature(method));
    methodProperties.put("body", buildInjectionMethodBody(injectionType, injectionOrdinal));
    generatedInjectionMethods.add(methodProperties);
    this.generatedMethodDescriptions.add(buildSpecGuideInjectionMethodDescriptions(method, injectionOrdinal));
  }

  private String buildInjectionMethodBody(String injectionType, int injectionIndex) {
    return "return (" + injectionType + ") this.$broker.injection(" + injectionIndex + ").value();";
  }

  private MethodStatement findGuideMethod(
      MethodStatement domainMethod, List<MethodStatement> objectHandleMethods, ObjectHandleMethodForm methodForm
  ) {
    ObjectReferenceForm targetForm = getTargetForm(domainMethod, methodForm);
    for (MethodStatement objectHandleMethod : objectHandleMethods) {
      if (!GuideFunctions.isGuideMethod(objectHandleMethod)) {
        continue;
      }
      if (getObjectHandleMethodName(domainMethod, targetForm).equals(objectHandleMethod.name()) &&
          equalParams(domainMethod.params(), objectHandleMethod.params(), methodForm)
      ) {
        return objectHandleMethod;
      }
    }
    return null;
  }

  private boolean equalParams(
      List<MethodParam> domainMethodParams, List<MethodParam> guideMethodParams, ObjectHandleMethodForm methodForm
  ) {
    if (domainMethodParams.size() != guideMethodParams.size()) {
      return false;
    }

    boolean paramMatches = true;
    ObjectReferenceForm referenceForm = getReferenceForm(methodForm);
    for (int i = 0; i < domainMethodParams.size(); i++) {
      MethodParam domainParam = domainMethodParams.get(i);
      String domainMethodParamDeclaration = ObjectHandleFunctions.getObjectHandleDeclaration(
          domainParam.type(), ObjectHandleTypes.General, referenceForm, Function.identity()
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
    return NameConventionFunctions.getObjectHandleWrapperCanonicalName(sourceArtifact());
  }

  private Map<String, Object> buildTraverseMethodDescriptions(
      MethodStatement domainMethod, ObjectHandleMethodForm methodForm, ObjectReferenceForm targetForm, int ordinal
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", getObjectHandleMethodName(domainMethod, targetForm));

    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : domainMethod.params()) {
      paramClasses.add(buildGuideParamClassName(param.type(), methodForm));
    }
    map.put("params", paramClasses);
    map.put("purpose", ObjectHandleMethodPurposes.TraverseMethod.name());
    map.put("traverseOrdinal", ordinal);
    map.put("channelClass", addImportAndGetSimpleName(NameConventionFunctions.getChannelClassCanonicalName(
        domainMethod)));
    map.put("traverseType", ChannelFunctions.getTraverseType(domainMethod).name());
    return map;
  }

  private Map<String, Object> buildGuideMethodDescriptions(
      MethodStatement guidMethod, ObjectHandleMethodForm methodForm, int ordinal
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", ObjectHandleFunctions.buildObjectHandleGuideMethodName(guidMethod));

    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : guidMethod.params()) {
      paramClasses.add(buildGuideParamClassName(param.type(), methodForm));
    }
    map.put("params", paramClasses);
    map.put("purpose", ObjectHandleMethodPurposes.GuideMethod.name());
    map.put("traverseOrdinal", ordinal);
    return map;
  }

  private Map<String, Object> buildConversionGuideMethodDescriptions(
      MethodStatement domainMethod, int ordinal
  ) {
    var map = new HashMap<String, Object>();

    CustomTypeReference parentType = domainMethod.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow();
    String methodName = "$as" +
        StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(parentType.targetType().simpleName(), "Domain"));
    map.put("name", methodName);

    map.put("params", List.of());
    map.put("purpose", ObjectHandleMethodPurposes.GuideMethod.name());
    map.put("traverseOrdinal", ordinal);
    return map;
  }

  private Map<String, Object> buildAutoGuideInjectionMethodDescriptions(
      MethodStatement injectionMethod, int ordinal
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", injectionMethod.name());
    map.put("params", List.of());
    map.put("purpose", ObjectHandleMethodPurposes.InjectionMethod.name());

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
    map.put("purpose", ObjectHandleMethodPurposes.InjectionMethod.name());

    map.put("injectionKind", "specguide");
    map.put("injectionOrdinal", ordinal);
    map.put("injectionName", injectionMethod.name());
    map.put("injectionType", injectionMethod.returnType().orElseThrow().actualDeclaration(
        this::addImportAndGetSimpleName)
    );
    return map;
  }

  private String buildGuideParamClassName(TypeReference type, ObjectHandleMethodForm methodForm) {
    return ObjectHandleFunctions.getObjectHandleDeclaration(
        AnnotationGeneratorFunctions.normalizeType(type),
        ObjectHandleTypes.General,
        getReferenceForm(methodForm),
        false,
        this::addImportAndGetSimpleName
    );
  }

  protected Map<String, String> generateMethod(
      MethodStatement domainMethod, ObjectHandleMethodForm methodForm, ObjectReferenceForm targetForm, int methodOrdinal
  ) {
    var sb = new StringBuilder();
    sb.append("@Ordinal(").append(methodOrdinal).append(")\n");
    sb.append("public ");
    appendMethodTypeParameters(sb, domainMethod);
    appendMethodReturnHandleType(sb, domainMethod, targetForm);
    sb.append(" ");
    sb.append(getObjectHandleMethodName(domainMethod, targetForm));
    sb.append("(");
    appendMethodParams(sb, domainMethod, methodForm);
    sb.append(")");
    appendMethodExceptions(sb, domainMethod);
    sb.append(" {\n");
    sb.append("  return ");
    if (ObjectHandleMethodForms.Normal.is(methodForm) && isPrimitiveWrapper(domainMethod.returnType().orElseThrow())) {
      CustomType ct = domainMethod.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();
      String typename = ClassFunctions.primitiveTypenameOfWrapper(ct.canonicalName());
      if (PrimitiveTypes.Boolean.typename().equals(typename)) {
        sb.append("PrimitiveFunctions.longToBoolean(");
        appendInvokeMethodAction(sb, domainMethod, methodForm, methodOrdinal);
        sb.append(")");
      } else {
        sb.append("(");
        appendMethodReturnHandleType(sb, domainMethod, targetForm);
        sb.append(") ");
        appendInvokeMethodAction(sb, domainMethod, methodForm, methodOrdinal);
      }
    } else {
      sb.append("(");
      appendMethodReturnHandleType(sb, domainMethod, targetForm);
      sb.append(") ");
      appendInvokeMethodAction(sb, domainMethod, methodForm, methodOrdinal);
    }
    sb.append(";\n}");
    return Map.of("declaration", sb.toString());
  }

  private void appendInvokeMethodAction(
      StringBuilder sb, MethodStatement domainMethod, ObjectHandleMethodForm methodForm, int methodOrdinal
  ) {
    sb.append("$broker.methodAction(");
    sb.append(methodOrdinal);
    sb.append(").castToAction");
    sb.append(domainMethod.params().size() + 1);
    sb.append("().");
    sb.append(buildExecuteMethod(domainMethod, methodForm));
    sb.append("(this");
    for (MethodParam param : domainMethod.params()) {
      sb.append(", ");
      appendMethodParam(sb, param);
    }
    sb.append(")");
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

  private String buildExecuteMethod(MethodStatement domainMethod, ObjectHandleMethodForm methodForm) {
    if (ObjectHandleMethodForms.Normal.is(methodForm)) {
      if (isPrimitiveWrapper(domainMethod.returnType().orElseThrow())) {
        CustomType returnType = domainMethod.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow().targetType();
        String typename = ClassFunctions.primitiveTypenameOfWrapper(returnType.canonicalName());
        if (tech.intellispaces.general.object.ObjectFunctions.equalsAnyOf(
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
    } else if (ObjectHandleMethodForms.Object.is(methodForm)) {
        return "execute";
    } else {
      throw UnexpectedExceptions.withMessage("Unsupported guide form - {0}", methodForm);
    }
  }

  private Map<String, String> buildConversionMethod(CustomTypeReference parent) {
    var sb = new StringBuilder();
    sb.append("private ");
    sb.append(buildObjectHandleDeclaration(parent, getObjectHandleType()));
    sb.append(" $");
    sb.append(NameConventionFunctions.getConversionMethodName(parent));
    sb.append("() {\n");
    sb.append("  return new ");

    Optional<CustomTypeReference> aliasBaseDomain = DomainFunctions.getAliasBaseDomain(domainType);
    CustomType actualDomain = aliasBaseDomain.isPresent() ? aliasBaseDomain.get().targetType() : domainType;
    sb.append(addImportAndGetSimpleName(
        NameConventionFunctions.getDownwardObjectHandleTypename(actualDomain, parent.targetType(), getObjectHandleType())
    ));
    sb.append("(this);\n");
    sb.append("}");
    return Map.of(
        "declaration", sb.toString()
    );
  }
}
