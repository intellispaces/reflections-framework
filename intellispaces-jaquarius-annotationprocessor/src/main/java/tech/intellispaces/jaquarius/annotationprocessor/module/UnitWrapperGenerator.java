package tech.intellispaces.jaquarius.annotationprocessor.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import tech.intellispaces.actions.runnable.RunnableAction;
import tech.intellispaces.actions.text.StringActions;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.jaquarius.annotation.Ordinal;
import tech.intellispaces.jaquarius.annotation.Projection;
import tech.intellispaces.jaquarius.annotation.ProjectionSupplier;
import tech.intellispaces.jaquarius.annotation.Wrapper;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationGeneratorFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactGenerator;
import tech.intellispaces.jaquarius.engine.JaquariusEngines;
import tech.intellispaces.jaquarius.engine.UnitBroker;
import tech.intellispaces.jaquarius.engine.description.UnitMethodPurposes;
import tech.intellispaces.jaquarius.exception.ConfigurationExceptions;
import tech.intellispaces.jaquarius.guide.GuideFunctions;
import tech.intellispaces.jaquarius.guide.GuideKinds;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceFunctions;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.system.Modules;
import tech.intellispaces.jaquarius.system.ProjectionInjection;
import tech.intellispaces.jaquarius.system.UnitWrapper;
import tech.intellispaces.jaquarius.system.injection.InjectionKinds;
import tech.intellispaces.jaquarius.system.projection.ProjectionReferences;
import tech.intellispaces.reflection.customtype.CustomType;
import tech.intellispaces.reflection.instance.AnnotationInstance;
import tech.intellispaces.reflection.instance.ClassInstance;
import tech.intellispaces.reflection.instance.Instance;
import tech.intellispaces.reflection.method.MethodParam;
import tech.intellispaces.reflection.method.MethodStatement;
import tech.intellispaces.reflection.method.Methods;
import tech.intellispaces.reflection.reference.NamedReference;
import tech.intellispaces.reflection.reference.TypeReference;

public class UnitWrapperGenerator extends JaquariusArtifactGenerator {
  private String typeParamsFullDeclaration;
  private String typeParamsBriefDeclaration;
  private List<MethodStatement> methods;
  private final List<Map<String, Object>> generatedInjectionMethods = new ArrayList<>();
  private List<Map<String, Object>> generatedGuideMethods;
  private final List<String> generatedProjectionMethods = new ArrayList<>();
  private final List<Map<String, String>> generatedGuideActionMethods = new ArrayList<>();
  protected final List<Map<String, Object>> generatedMethodDescriptions = new ArrayList<>();
  private int injectionMethodCounter;

  public UnitWrapperGenerator(CustomType unitType) {
    super(unitType);
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getUnitWrapperCanonicalName(sourceArtifact().className());
  }

  @Override
  protected String templateName() {
    return "/unit_wrapper.template";
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    if (sourceArtifact().isNested()) {
      addImport(sourceArtifactName());
    }
    addImports(
        Wrapper.class,
        Ordinal.class,
        UnitWrapper.class,
        UnitBroker.class,
        JaquariusEngines.class,
        UnitMethodPurposes.class,
        InjectionKinds.class,
        ProjectionReferences.class,
        GuideKinds.class,
        ObjectReferenceForms.class
    );

    methods = sourceArtifact().actualMethods();
    analyzeTypeParams();
    analyzeMethods();
    analyzeGuideMethods();
    analyzeGuideActions();

    addVariable("typeParamsFullDeclaration", typeParamsFullDeclaration);
    addVariable("typeParamsBriefDeclaration", typeParamsBriefDeclaration);
    addVariable("injectionMethods", generatedInjectionMethods);
    addVariable("overrideGuideMethods", generatedGuideMethods);
    addVariable("overrideProjectionMethods", generatedProjectionMethods);
    addVariable("guideActionMethods", generatedGuideActionMethods);
    addVariable("methodDescriptions", generatedMethodDescriptions);
    return true;
  }

  private void analyzeTypeParams() {
    typeParamsFullDeclaration = sourceArtifact().typeParametersFullDeclaration();
    typeParamsBriefDeclaration = sourceArtifact().typeParametersBriefDeclaration();
  }

  private void analyzeGuideMethods() {
    this.generatedGuideMethods = methods.stream()
        .filter(GuideFunctions::isGuideMethod)
        .map(this::buildGuideMethod)
        .toList();
  }

  private Map<String, Object> buildGuideMethod(MethodStatement guideMethod) {
    var sb = new StringBuilder();
    sb.append("public ");
    sb.append(buildMethodSignature(guideMethod));
    sb.append(" {\n");
    sb.append("  return super.");
    sb.append(guideMethod.name());
    sb.append("(");
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
    for (MethodParam param : guideMethod.params()) {
      commaAppender.run();
      sb.append(param.name());
    }
    sb.append(");\n}");
    return Map.of("declaration", sb.toString());
  }

  private void analyzeGuideActions() {
    var guideOrdinal = new AtomicInteger();
    methods.stream()
        .filter(GuideFunctions::isGuideMethod)
        .forEach(method -> {
          generatedGuideActionMethods.add((AnnotationGeneratorFunctions.buildGuideActionMethod(method, this)));
          generatedMethodDescriptions.add(buildGuideMethodDescription(method, guideOrdinal.get()));
          guideOrdinal.incrementAndGet();
      });
  }

  private String buildTypeDeclaration(TypeReference type) {
    if (type.isPrimitiveReference()) {
      return type.asPrimitiveReferenceOrElseThrow().typename();
    } else if (type.isNamedReference()) {
      NamedReference namedType = type.asNamedReferenceOrElseThrow();
      if (namedType.extendedBounds().isEmpty()) {
        return "Object";
      } else {
        return buildTypeDeclaration(namedType.extendedBounds().get(0));
      }
    }
    return addImportAndGetSimpleName(
        type.asCustomTypeReferenceOrElseThrow().targetType().canonicalName()
    );
  }

  private void analyzeMethods() {
    for (MethodStatement method : methods) {
      if (method.isAbstract() && !method.isDefault()) {
        if (AnnotationGeneratorFunctions.isProjectionMethod(method)) {
          addProjectionInjection(method);
          addProjectionDefinitionForAbstractMethod(method);
        } else if (AnnotationGeneratorFunctions.isInjectionMethod(method)) {
          if (AnnotationGeneratorFunctions.isAutoGuideMethod(method)) {
            if (!AnnotationGeneratorFunctions.returnTypeIsGuide(method)) {
              throw ConfigurationExceptions.withMessage("Guide injection method '{0}' in class {1} must return guide",
                  method.name(), sourceArtifact().className()
              );
            }
            addAutoGuideInjectionAndImplementationMethod(method);
          } else if (AnnotationGeneratorFunctions.returnTypeIsGuide(method)) {
            addGuideInjectionAndImplementationMethod(method);
          } else {
            addProjectionInjection(method);
          }
        } else {
          throw ConfigurationExceptions.withMessage("Undefined abstract method '{0}' in class {1}",
              method.name(), sourceArtifact().className()
          );
        }
      } else {
        if (AnnotationGeneratorFunctions.isProjectionMethod(method)) {
          addProjectionInjection(method);
          addGeneratedProjectionMethod(method);
        } else if (AnnotationGeneratorFunctions.isStartupMethod(method)) {
          addStartupMethodDescription(method);
        } else if (AnnotationGeneratorFunctions.isShutdownMethod(method)) {
          addShutdownMethodDescription(method);
        }
      }
    }
  }

  private void addProjectionDefinitionForAbstractMethod(MethodStatement method) {
    for (AnnotationInstance annotation : method.annotations()) {
      Optional<AnnotationInstance> projectionDefinitionAnnotation = annotation.annotationStatement().selectAnnotation(
          ProjectionSupplier.class.getCanonicalName());
      if (projectionDefinitionAnnotation.isPresent()) {
        AnnotationInstance projectionDefinition = projectionDefinitionAnnotation.get();
        Optional<Instance> supplierClass = projectionDefinition.valueOf("supplier");
        if (supplierClass.isPresent() && isProjectionSupplierDefined(supplierClass.get())) {
          addProjectionDefinitionBasedOnProviderClass(method, supplierClass.get().asClass().orElseThrow());
          return;
        }
      }
    }
    throw UnexpectedExceptions.withMessage("Projection definition is not found. See method {0} in unit {1}",
        method.name(), method.owner().canonicalName());
  }

  private static boolean isProjectionSupplierDefined(Instance supplierClass) {
    return !tech.intellispaces.jaquarius.system.ProjectionSupplier.class.getCanonicalName().equals(
        supplierClass.asClass().orElseThrow().type().canonicalName()
    );
  }

  private void addProjectionDefinitionBasedOnProviderClass(
      MethodStatement method, ClassInstance projectionProvider
  ) {
    generatedProjectionMethods.add(buildGeneratedProjectionMethodBasedOnSupplier(method, projectionProvider));
    generatedMethodDescriptions.add(buildGeneratedProjectionMethodDescription(method));
  }

  private void addProjectionInjection(MethodStatement method) {
    addImport(Modules.class);
    addImport(ProjectionInjection.class);

    String injectionType = method.returnType().orElseThrow().actualDeclaration();

    Map<String, Object> methodProperties = new HashMap<>();
    methodProperties.put("javadoc", "");
    methodProperties.put("annotations", List.of(Override.class.getSimpleName()));
    methodProperties.put("signature", buildMethodSignature(method));
    methodProperties.put("body", buildInjectionMethodBody(injectionType, injectionMethodCounter));
    generatedInjectionMethods.add(methodProperties);
    generatedMethodDescriptions.add(buildProjectionInjectionMethodDescription(method, injectionMethodCounter));
    injectionMethodCounter++;
  }

  private void addGeneratedProjectionMethod(MethodStatement method) {
    generatedProjectionMethods.add(buildGeneratedProjectionMethod(method));
    generatedMethodDescriptions.add(buildGeneratedProjectionMethodDescription(method));
  }

  private Map<String, Object> buildGuideMethodDescription(
      MethodStatement method, int guideOrdinal
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", ObjectReferenceFunctions.buildObjectHandleGuideMethodName(method));

    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : method.params()) {
      paramClasses.add(addImportAndGetSimpleName(param.type().asCustomTypeReferenceOrElseThrow().targetType().canonicalName()));
    }
    map.put("params", paramClasses);
    map.put("purpose", UnitMethodPurposes.Guide.name());
    map.put("prototypeMethodName", method.name());
    map.put("type", "function");

    map.put("guideOrdinal", guideOrdinal);
    map.put("guideKind", GuideFunctions.getGuideKind(method).name());
    map.put("guideCid", ChannelFunctions.getUnitGuideChannelId(method));
    map.put("guideTargetForm", GuideFunctions.getTargetForm(method).name());
    return map;
  }

  private Map<String, Object> buildGeneratedProjectionMethodDescription(MethodStatement method) {
    var map = new HashMap<String, Object>();
    map.put("name", "$" + method.name());

    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : method.params()) {
      paramClasses.add(addImportAndGetSimpleName(param.type().asCustomTypeReferenceOrElseThrow().targetType().canonicalName()));
    }
    map.put("params", paramClasses);
    map.put("purpose", UnitMethodPurposes.ProjectionDefinition.name());
    map.put("type", "function");

    map.put("projectionName", method.name());
    map.put("targetClass", addImportAndGetSimpleName(getTargetClassName(method)));
    if (!method.params().isEmpty()) {
      map.put("requiredProjections", buildRequiredProjections(method));
    }
    map.put("lazyLoading", method.selectAnnotation(Projection.class).orElseThrow().lazy());
    return map;
  }

  private String getTargetClassName(MethodStatement method) {
    TypeReference returnType = method.returnType().orElseThrow();
    if (returnType.isCustomTypeReference()) {
      return returnType.asCustomTypeReferenceOrElseThrow().targetType().canonicalName();
    } else if (returnType.isPrimitiveReference()) {
      return returnType.asPrimitiveReferenceOrElseThrow().primitiveType().typename();
    } else {
      throw UnexpectedExceptions.withMessage("Not expected projection method target type");
    }
  }

  private List<Map<String, String>> buildRequiredProjections(MethodStatement method) {
    return method.params().stream()
        .map(param -> Map.of(
            "name", param.name(),
            "class", addImportAndGetSimpleName(param.type().asCustomTypeReferenceOrElseThrow().targetClass().getCanonicalName())))
        .toList();
  }

  private String buildGeneratedProjectionMethod(MethodStatement method) {
    var sb = new StringBuilder();
    sb.append("private ");
    sb.append(buildMethodSignature(method, "$" + method.name()));
    sb.append(" {\n");
    sb.append("  return super.").append(method.name()).append("(");
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
    for (MethodParam param : method.params()) {
      commaAppender.run();
      sb.append(param.name());
    }
    sb.append(");\n");
    sb.append("}");
    return sb.toString();
  }

  private String buildGeneratedProjectionMethodBasedOnSupplier(
      MethodStatement method, ClassInstance projectionProvider
  ) {
    var sb = new StringBuilder();
    sb.append("private ");
    sb.append(buildMethodSignature(method, "$" + method.name()));
    sb.append(" {\n");
    sb.append("  return (");
    sb.append(buildTypeDeclaration(method.returnType().orElseThrow()));
    sb.append(") new ");
    sb.append(addImportAndGetSimpleName(projectionProvider.type().canonicalName()));
    sb.append("(");

    sb.append(addImportAndGetSimpleName(Methods.class));
    sb.append(".of(");
    sb.append(sourceArtifact().simpleName());
    sb.append(".class, \"");
    sb.append(method.name());
    sb.append("\"");

    sb.append(")).get();\n");
    sb.append("}");
    return sb.toString();
  }

  private Map<String, Object> buildProjectionInjectionMethodDescription(
      MethodStatement method, int ordinal
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", method.name());
    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : method.params()) {
      paramClasses.add(addImportAndGetSimpleName(param.type().asCustomTypeReferenceOrElseThrow().targetType().canonicalName()));
    }
    map.put("params", paramClasses);
    map.put("type", "function");

    map.put("purpose", UnitMethodPurposes.InjectionMethod.name());
    map.put("injectionKind", InjectionKinds.class.getSimpleName() + "." + InjectionKinds.Projection.name());
    map.put("injectionOrdinal", ordinal);
    map.put("injectionName", method.name());
    map.put("injectionClass", method.returnType().orElseThrow().actualDeclaration(this::addImportAndGetSimpleName));
    return map;
  }

  private Map<String, Object> buildAutoGuideInjectionMethodDescription(
      MethodStatement method, int ordinal
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", method.name());
    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : method.params()) {
      paramClasses.add(addImportAndGetSimpleName(param.type().asCustomTypeReferenceOrElseThrow().targetType().canonicalName()));
    }
    map.put("params", paramClasses);
    map.put("type", "function");

    map.put("purpose", UnitMethodPurposes.InjectionMethod.name());
    map.put("injectionKind", InjectionKinds.class.getSimpleName() + "." + InjectionKinds.AutoGuide.name());
    map.put("injectionOrdinal", ordinal);
    map.put("injectionName", method.name());
    map.put("injectionClass", method.returnType().orElseThrow().actualDeclaration(this::addImportAndGetSimpleName));
    return map;
  }

  private Map<String, Object> buildSpecGuideInjectionMethodDescription(
      MethodStatement method, int ordinal
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", method.name());
    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : method.params()) {
      paramClasses.add(addImportAndGetSimpleName(param.type().asCustomTypeReferenceOrElseThrow().targetType().canonicalName()));
    }
    map.put("params", paramClasses);
    map.put("type", "function");

    map.put("purpose", UnitMethodPurposes.InjectionMethod.name());
    map.put("injectionKind", InjectionKinds.class.getSimpleName() + "." + InjectionKinds.SpecificGuide.name());
    map.put("injectionOrdinal", ordinal);
    map.put("injectionName", method.name());
    map.put("injectionClass", method.returnType().orElseThrow().actualDeclaration(this::addImportAndGetSimpleName));
    return map;
  }

  private void addAutoGuideInjectionAndImplementationMethod(MethodStatement method) {
    addImport(Modules.class);
    addImport(ProjectionInjection.class);

    String injectionName = method.name();
    String injectionType = method.returnType().orElseThrow().actualDeclaration();

    Map<String, Object> methodProperties = new HashMap<>();
    methodProperties.put("javadoc", "");
    methodProperties.put("annotations", List.of(Override.class.getSimpleName()));
    methodProperties.put("signature", buildMethodSignature(method));
    methodProperties.put("body", buildInjectionMethodBody(injectionType, injectionMethodCounter));
    generatedInjectionMethods.add(methodProperties);
    generatedMethodDescriptions.add(buildAutoGuideInjectionMethodDescription(method, injectionMethodCounter));
    injectionMethodCounter++;
  }

  private void addGuideInjectionAndImplementationMethod(MethodStatement method) {
    addImport(Modules.class);
    addImport(ProjectionInjection.class);

    String injectionName = method.name();
    String injectionType = method.returnType().orElseThrow().actualDeclaration();

    Map<String, Object> methodProperties = new HashMap<>();
    methodProperties.put("javadoc", "");
    methodProperties.put("annotations", List.of(Override.class.getSimpleName()));
    methodProperties.put("signature", buildMethodSignature(method));
    methodProperties.put("body", buildInjectionMethodBody(injectionType, injectionMethodCounter));
    generatedInjectionMethods.add(methodProperties);
    generatedMethodDescriptions.add(buildSpecGuideInjectionMethodDescription(method, injectionMethodCounter));
    injectionMethodCounter++;
  }

  private void addStartupMethodDescription(MethodStatement method) {
    var description = new HashMap<String, Object>();
    description.put("name", method.name());
    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : method.params()) {
      paramClasses.add(addImportAndGetSimpleName(param.type().asCustomTypeReferenceOrElseThrow().targetType().canonicalName()));
    }
    description.put("params", paramClasses);
    description.put("type", "consumer");
    description.put("purpose", UnitMethodPurposes.StartupMethod.name());
    if (!method.params().isEmpty()) {
      description.put("requiredProjections", buildRequiredProjections(method));
    }
    generatedMethodDescriptions.add(description);
  }

  private void addShutdownMethodDescription(MethodStatement method) {
    var description = new HashMap<String, Object>();
    description.put("name", method.name());
    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : method.params()) {
      paramClasses.add(addImportAndGetSimpleName(param.type().asCustomTypeReferenceOrElseThrow().targetType().canonicalName()));
    }
    description.put("params", paramClasses);
    description.put("type", "consumer");
    description.put("purpose", UnitMethodPurposes.ShutdownMethod.name());
    if (!method.params().isEmpty()) {
      description.put("requiredProjections", buildRequiredProjections(method));
    }
    generatedMethodDescriptions.add(description);
  }

  private String buildInjectionMethodBody(String injectionType, int injectionIndex) {
    return "return (" + injectionType + ") this.$broker.injection(" + injectionIndex + ").value();";
  }
}
