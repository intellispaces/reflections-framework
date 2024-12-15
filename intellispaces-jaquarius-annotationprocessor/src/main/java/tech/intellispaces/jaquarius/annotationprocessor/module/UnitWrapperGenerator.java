package tech.intellispaces.jaquarius.annotationprocessor.module;

import tech.intellispaces.action.Action;
import tech.intellispaces.action.Actions;
import tech.intellispaces.action.functional.FunctionActions;
import tech.intellispaces.action.runnable.RunnableAction;
import tech.intellispaces.action.supplier.ResettableSupplierAction;
import tech.intellispaces.action.text.StringActions;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.general.exception.UnexpectedExceptions;
import tech.intellispaces.jaquarius.annotation.AutoGuide;
import tech.intellispaces.jaquarius.annotation.Inject;
import tech.intellispaces.jaquarius.annotation.Ordinal;
import tech.intellispaces.jaquarius.annotation.Projection;
import tech.intellispaces.jaquarius.annotation.ProjectionSupplier;
import tech.intellispaces.jaquarius.annotation.Shutdown;
import tech.intellispaces.jaquarius.annotation.Startup;
import tech.intellispaces.jaquarius.annotation.Wrapper;
import tech.intellispaces.jaquarius.annotationprocessor.GuideProcessorFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactGenerator;
import tech.intellispaces.jaquarius.engine.JaquariusEngines;
import tech.intellispaces.jaquarius.engine.UnitBroker;
import tech.intellispaces.jaquarius.engine.descriptor.UnitMethodPurposes;
import tech.intellispaces.jaquarius.exception.ConfigurationExceptions;
import tech.intellispaces.jaquarius.guide.GuideFunctions;
import tech.intellispaces.jaquarius.guide.GuideKinds;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.object.ObjectHandleFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.system.Injection;
import tech.intellispaces.jaquarius.system.Modules;
import tech.intellispaces.jaquarius.system.ProjectionInjection;
import tech.intellispaces.jaquarius.system.UnitWrapper;
import tech.intellispaces.jaquarius.system.injection.AutoGuideInjections;
import tech.intellispaces.jaquarius.system.injection.GuideInjections;
import tech.intellispaces.jaquarius.system.injection.InjectionKinds;
import tech.intellispaces.jaquarius.system.injection.ProjectionInjections;
import tech.intellispaces.jaquarius.system.projection.ProjectionReferences;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.instance.AnnotationInstance;
import tech.intellispaces.java.reflection.instance.ClassInstance;
import tech.intellispaces.java.reflection.instance.Instance;
import tech.intellispaces.java.reflection.method.MethodParam;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.method.Methods;
import tech.intellispaces.java.reflection.reference.NamedReference;
import tech.intellispaces.java.reflection.reference.TypeReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class UnitWrapperGenerator extends JaquariusArtifactGenerator {
  private final List<Map<String, Object>> injectionMethods = new ArrayList<>();
  private List<MethodStatement> methods;
  private List<Map<String, Object>> overrideGuideMethods;
  private final List<String> overrideProjectionMethods = new ArrayList<>();
  private final List<Map<String, String>> guideActionMethods = new ArrayList<>();
  protected final List<Map<String, Object>> wrapperMethods = new ArrayList<>();
  private String typeParamsFullDeclaration;
  private String typeParamsBriefDeclaration;
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

    addImport(List.class);
    addImport(Collections.class);
    addImport(ArrayList.class);
    addImport(Action.class);
    addImport(Actions.class);
    addImport(FunctionActions.class);
    addImport(ResettableSupplierAction.class);
    addImport(Wrapper.class);
    addImport(Ordinal.class);
    addImport(Modules.class);
    addImport(Injection.class);
    addImport(UnitWrapper.class);
    addImport(ProjectionReferences.class);
    addImport(ProjectionInjections.class);
    addImport(GuideInjections.class);
    addImport(AutoGuideInjections.class);
    addImport(UnitBroker.class);
    addImport(JaquariusEngines.class);
    addImport(UnitMethodPurposes.class);
    addImport(InjectionKinds.class);
    addImport(ProjectionReferences.class);
    addImport(GuideKinds.class);
    addImport(ObjectReferenceForms.class);

    methods = sourceArtifact().actualMethods();
    analyzeTypeParams();
    analyzeMethods();
    analyzeGuideMethods();
    analyzeGuideActions();

    addVariable("generatedAnnotation", makeGeneratedAnnotation());
    addVariable("typeParamsFullDeclaration", typeParamsFullDeclaration);
    addVariable("typeParamsBriefDeclaration", typeParamsBriefDeclaration);
    addVariable("injectionMethods", injectionMethods);
    addVariable("overrideGuideMethods", overrideGuideMethods);
    addVariable("overrideProjectionMethods", overrideProjectionMethods);
    addVariable("guideActionMethods", guideActionMethods);
    addVariable("wrapperMethods", wrapperMethods);
    return true;
  }

  private void analyzeTypeParams() {
    typeParamsFullDeclaration = sourceArtifact().typeParametersFullDeclaration();
    typeParamsBriefDeclaration = sourceArtifact().typeParametersBriefDeclaration();
  }

  private void analyzeGuideMethods() {
    this.overrideGuideMethods = methods.stream()
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
          guideActionMethods.add((GuideProcessorFunctions.buildGuideActionMethod(method, this)));
          wrapperMethods.add(buildGuideMethodDescription(method, guideOrdinal.get()));
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
    return addToImportAndGetSimpleName(
        type.asCustomTypeReferenceOrElseThrow().targetType().canonicalName()
    );
  }

  private void analyzeMethods() {
    for (MethodStatement method : methods) {
      if (method.isAbstract() && !method.isDefault()) {
        if (isProjectionMethod(method)) {
          if (!isReturnObjectHandle(method)) {
            throw ConfigurationExceptions.withMessage("Projection method '{0}' in class {1} must return object handle",
                method.name(), sourceArtifact().className()
            );
          }
          addProjectionInjection(method);
          addProjectionDefinitionForAbstractMethod(method);
        } else if (isInjectionMethod(method)) {
          if (isAutoGuideMethod(method)) {
            if (!isReturnGuide(method)) {
              throw ConfigurationExceptions.withMessage("Guide injection method '{0}' in class {1} must return guide",
                  method.name(), sourceArtifact().className()
              );
            }
            addAutoGuideInjectionAndImplementationMethod(method);
          } else if (isReturnGuide(method)) {
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
        if (isProjectionMethod(method)) {
          addProjectionInjection(method);
          addGeneratedProjectionMethod(method);
        } else if (isStartupMethod(method)) {
          addStartupMethodDescription(method);
        } else if (isShutdownMethod(method)) {
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
    overrideProjectionMethods.add(buildGeneratedProjectionMethodBasedOnSupplier(method, projectionProvider));
    wrapperMethods.add(buildGeneratedProjectionMethodDescription(method));
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
    injectionMethods.add(methodProperties);

    wrapperMethods.add(buildProjectionInjectionMethodDescriptor(method, injectionMethodCounter));

    injectionMethodCounter++;
  }

  private void addGeneratedProjectionMethod(MethodStatement method) {
    overrideProjectionMethods.add(buildGeneratedProjectionMethod(method));
    wrapperMethods.add(buildGeneratedProjectionMethodDescription(method));
  }

  private Map<String, Object> buildGuideMethodDescription(
      MethodStatement method, int guideOrdinal
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", ObjectHandleFunctions.buildObjectHandleGuideMethodName(method));

    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : method.params()) {
      paramClasses.add(addToImportAndGetSimpleName(param.type().asCustomTypeReferenceOrElseThrow().targetType().canonicalName()));
    }
    map.put("params", paramClasses);
    map.put("purpose", UnitMethodPurposes.Guide.name());
    map.put("prototypeMethodName", method.name());
    map.put("type", "function");

    map.put("guideOrdinal", guideOrdinal);
    map.put("guideKind", GuideFunctions.getGuideKind(method).name());
    map.put("guideCid", ChannelFunctions.getUnitGuideCid(method));
    map.put("guideTargetForm", GuideFunctions.getTargetForm(method).name());
    return map;
  }

  private Map<String, Object> buildGeneratedProjectionMethodDescription(MethodStatement method) {
    var map = new HashMap<String, Object>();
    map.put("name", "$" + method.name());

    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : method.params()) {
      paramClasses.add(addToImportAndGetSimpleName(param.type().asCustomTypeReferenceOrElseThrow().targetType().canonicalName()));
    }
    map.put("params", paramClasses);
    map.put("purpose", UnitMethodPurposes.ProjectionDefinition.name());
    map.put("type", "function");

    map.put("projectionName", method.name());
    map.put("targetClass", addToImportAndGetSimpleName(getTargetClassName(method)));
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
            "class", addToImportAndGetSimpleName(param.type().asCustomTypeReferenceOrElseThrow().targetClass().getCanonicalName())))
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
    sb.append(addToImportAndGetSimpleName(projectionProvider.type().canonicalName()));
    sb.append("(");

    sb.append(addToImportAndGetSimpleName(Methods.class));
    sb.append(".of(");
    sb.append(sourceArtifact().simpleName());
    sb.append(".class, \"");
    sb.append(method.name());
    sb.append("\"");

    sb.append(")).get();\n");
    sb.append("}");
    return sb.toString();
  }

  private Map<String, Object> buildProjectionInjectionMethodDescriptor(
      MethodStatement method, int ordinal
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", method.name());
    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : method.params()) {
      paramClasses.add(addToImportAndGetSimpleName(param.type().asCustomTypeReferenceOrElseThrow().targetType().canonicalName()));
    }
    map.put("params", paramClasses);
    map.put("type", "function");

    map.put("purpose", UnitMethodPurposes.InjectionMethod.name());
    map.put("injectionKind", InjectionKinds.class.getSimpleName() + "." + InjectionKinds.Projection.name());
    map.put("injectionOrdinal", ordinal);
    map.put("injectionName", method.name());
    map.put("injectionClass", method.returnType().orElseThrow().actualDeclaration(this::addToImportAndGetSimpleName));
    return map;
  }

  private Map<String, Object> buildAutoGuideInjectionMethodDescriptor(
      MethodStatement method, int ordinal
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", method.name());
    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : method.params()) {
      paramClasses.add(addToImportAndGetSimpleName(param.type().asCustomTypeReferenceOrElseThrow().targetType().canonicalName()));
    }
    map.put("params", paramClasses);
    map.put("type", "function");

    map.put("purpose", UnitMethodPurposes.InjectionMethod.name());
    map.put("injectionKind", InjectionKinds.class.getSimpleName() + "." + InjectionKinds.AutoGuide.name());
    map.put("injectionOrdinal", ordinal);
    map.put("injectionName", method.name());
    map.put("injectionClass", method.returnType().orElseThrow().actualDeclaration(this::addToImportAndGetSimpleName));
    return map;
  }

  private Map<String, Object> buildSpecGuideInjectionMethodDescriptor(
      MethodStatement method, int ordinal
  ) {
    var map = new HashMap<String, Object>();
    map.put("name", method.name());
    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : method.params()) {
      paramClasses.add(addToImportAndGetSimpleName(param.type().asCustomTypeReferenceOrElseThrow().targetType().canonicalName()));
    }
    map.put("params", paramClasses);
    map.put("type", "function");

    map.put("purpose", UnitMethodPurposes.InjectionMethod.name());
    map.put("injectionKind", InjectionKinds.class.getSimpleName() + "." + InjectionKinds.SpecificGuide.name());
    map.put("injectionOrdinal", ordinal);
    map.put("injectionName", method.name());
    map.put("injectionClass", method.returnType().orElseThrow().actualDeclaration(this::addToImportAndGetSimpleName));
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
    injectionMethods.add(methodProperties);

    wrapperMethods.add(buildAutoGuideInjectionMethodDescriptor(method, injectionMethodCounter));

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
    injectionMethods.add(methodProperties);

    wrapperMethods.add(buildSpecGuideInjectionMethodDescriptor(method, injectionMethodCounter));

    injectionMethodCounter++;
  }

  private void addStartupMethodDescription(MethodStatement method) {
    var map = new HashMap<String, Object>();
    map.put("name", method.name());
    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : method.params()) {
      paramClasses.add(addToImportAndGetSimpleName(param.type().asCustomTypeReferenceOrElseThrow().targetType().canonicalName()));
    }
    map.put("params", paramClasses);
    map.put("type", "consumer");
    map.put("purpose", UnitMethodPurposes.StartupMethod.name());
    if (!method.params().isEmpty()) {
      map.put("requiredProjections", buildRequiredProjections(method));
    }
    wrapperMethods.add(map);
  }

  private void addShutdownMethodDescription(MethodStatement method) {
    var map = new HashMap<String, Object>();
    map.put("name", method.name());
    List<String> paramClasses = new ArrayList<>();
    for (MethodParam param : method.params()) {
      paramClasses.add(addToImportAndGetSimpleName(param.type().asCustomTypeReferenceOrElseThrow().targetType().canonicalName()));
    }
    map.put("params", paramClasses);
    map.put("type", "consumer");
    map.put("purpose", UnitMethodPurposes.ShutdownMethod.name());
    if (!method.params().isEmpty()) {
      map.put("requiredProjections", buildRequiredProjections(method));
    }
    wrapperMethods.add(map);
  }

  private String buildInjectionMethodBody(String injectionType, int injectionIndex) {
    return "return (" + injectionType + ") this.$broker.injection(" + injectionIndex + ").value();";
  }

  private boolean isStartupMethod(MethodStatement method) {
    return method.hasAnnotation(Startup.class);
  }

  private boolean isShutdownMethod(MethodStatement method) {
    return method.hasAnnotation(Shutdown.class);
  }

  private boolean isProjectionMethod(MethodStatement method) {
    return method.hasAnnotation(Projection.class);
  }

  private boolean isInjectionMethod(MethodStatement method) {
    return method.hasAnnotation(Inject.class) || method.hasAnnotation(AutoGuide.class);
  }

  private boolean isAutoGuideMethod(MethodStatement method) {
    return method.hasAnnotation(AutoGuide.class);
  }

  private boolean isReturnObjectHandle(MethodStatement method) {
    return ObjectHandleFunctions.isObjectHandleType(method.returnType().orElseThrow());
  }

  private boolean isReturnGuide(MethodStatement method) {
    return GuideFunctions.isGuideType(method.returnType().orElseThrow());
  }
}
