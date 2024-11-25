package tech.intellispaces.jaquarius.annotation.processor.unit;

import tech.intellispaces.jaquarius.annotation.AutoGuide;
import tech.intellispaces.jaquarius.annotation.Inject;
import tech.intellispaces.jaquarius.annotation.Ordinal;
import tech.intellispaces.jaquarius.annotation.Projection;
import tech.intellispaces.jaquarius.annotation.ProjectionDefinition;
import tech.intellispaces.jaquarius.annotation.Wrapper;
import tech.intellispaces.jaquarius.annotation.processor.AbstractGenerator;
import tech.intellispaces.jaquarius.annotation.processor.GuideProcessorFunctions;
import tech.intellispaces.jaquarius.common.NameConventionFunctions;
import tech.intellispaces.jaquarius.exception.ConfigurationExceptions;
import tech.intellispaces.jaquarius.guide.GuideFunctions;
import tech.intellispaces.jaquarius.object.ObjectFunctions;
import tech.intellispaces.jaquarius.system.Injection;
import tech.intellispaces.jaquarius.system.Modules;
import tech.intellispaces.jaquarius.system.ProjectionInjection;
import tech.intellispaces.jaquarius.system.ProjectionTargetSupplier;
import tech.intellispaces.jaquarius.system.UnitWrapper;
import tech.intellispaces.jaquarius.system.injection.AutoGuideInjections;
import tech.intellispaces.jaquarius.system.injection.GuideInjections;
import tech.intellispaces.jaquarius.system.injection.ProjectionInjections;
import tech.intellispaces.jaquarius.system.kernel.KernelUnit;
import tech.intellispaces.jaquarius.system.kernel.ProjectionRegistry;
import tech.intellispaces.jaquarius.system.projection.ProjectionDefinitionBasedOnMethodActions;
import tech.intellispaces.jaquarius.system.projection.ProjectionDefinitionBasedOnProviderClasses;
import tech.intellispaces.jaquarius.system.projection.ProjectionFunctions;
import tech.intellispaces.jaquarius.system.projection.ProjectionReferences;
import tech.intellispaces.action.Action;
import tech.intellispaces.action.Actions;
import tech.intellispaces.action.functional.FunctionActions;
import tech.intellispaces.action.runnable.RunnableAction;
import tech.intellispaces.action.supplier.ResettableSupplierAction;
import tech.intellispaces.action.text.StringActions;
import tech.intellispaces.entity.exception.UnexpectedExceptions;
import tech.intellispaces.java.annotation.context.AnnotationProcessingContext;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.instance.AnnotationInstance;
import tech.intellispaces.java.reflection.instance.ClassInstance;
import tech.intellispaces.java.reflection.instance.Instance;
import tech.intellispaces.java.reflection.method.MethodParam;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.NamedReference;
import tech.intellispaces.java.reflection.reference.TypeReference;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UnitWrapperGenerator extends AbstractGenerator {
  private final List<Map<String, Object>> injections = new ArrayList<>();
  private final List<String> projectionDefinitions = new ArrayList<>();
  private final List<Map<String, Object>> injectionMethods = new ArrayList<>();
  private List<MethodStatement> methods;
  private List<Map<String, Object>> overrideGuideMethods;
  private final List<Map<String, String>> guideActionMethods = new ArrayList<>();
  private final List<String> guideActions = new ArrayList<>();
  private String typeParamsFullDeclaration;
  private String typeParamsBriefDeclaration;

  public UnitWrapperGenerator(CustomType initiatorType, CustomType annotatedType) {
    super(initiatorType, annotatedType);
  }

  @Override
  public boolean isRelevant(AnnotationProcessingContext context) {
    return true;
  }

  @Override
  public String artifactName() {
    return NameConventionFunctions.getUnitWrapperCanonicalName(annotatedType.className());
  }

  @Override
  protected String templateName() {
    return "/unit_wrapper.template";
  }

  @Override
  protected Map<String, Object> templateVariables() {
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", makeGeneratedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("typeParamsFullDeclaration", typeParamsFullDeclaration);
    vars.put("typeParamsBriefDeclaration", typeParamsBriefDeclaration);
    vars.put("importedClasses", context.getImports());
    vars.put("projectionDefinitions", projectionDefinitions);
    vars.put("injections", injections);
    vars.put("injectionMethods", injectionMethods);
    vars.put("guideActions", guideActions);
    vars.put("overrideGuideMethods", overrideGuideMethods);
    vars.put("guideActionMethods", guideActionMethods);
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(artifactName());
    if (annotatedType.isNested()) {
      context.addImport(sourceClassCanonicalName());
    }

    context.addImport(List.class);
    context.addImport(Collections.class);
    context.addImport(ArrayList.class);
    context.addImport(Action.class);
    context.addImport(Actions.class);
    context.addImport(FunctionActions.class);
    context.addImport(ResettableSupplierAction.class);
    context.addImport(KernelUnit.class);
    context.addImport(Wrapper.class);
    context.addImport(Ordinal.class);
    context.addImport(Modules.class);
    context.addImport(Injection.class);
    context.addImport(ProjectionRegistry.class);
    context.addImport(UnitWrapper.class);
    context.addImport(ProjectionReferences.class);
    context.addImport(ProjectionInjections.class);
    context.addImport(GuideInjections.class);
    context.addImport(AutoGuideInjections.class);

    methods = annotatedType.actualMethods();
    analyzeTypeParams();
    analyzeMethods();
    analyzeGuideMethods();
    analyzeGuideActions();
    return true;
  }

  private void analyzeTypeParams() {
    typeParamsFullDeclaration = annotatedType.typeParametersFullDeclaration();
    typeParamsBriefDeclaration = annotatedType.typeParametersBriefDeclaration();
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
    methods.stream()
        .filter(GuideFunctions::isGuideMethod)
        .forEach(method -> {
          guideActions.add(GuideProcessorFunctions.buildGuideAction(artifactName(), method, context));
          guideActionMethods.add((GuideProcessorFunctions.buildGuideActionMethod(method, context)));
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
    return context.addToImportAndGetSimpleName(
        type.asCustomTypeReferenceOrElseThrow().targetType().canonicalName()
    );
  }

  private void analyzeMethods() {
    for (MethodStatement method : methods) {
      if (method.isAbstract() && !method.isDefault()) {
        if (isProjectionMethod(method)) {
          if (!isReturnObjectHandle(method)) {
            throw ConfigurationExceptions.withMessage("Projection method '{0}' in class {1} must return object handle",
                method.name(), annotatedType.className()
            );
          }
          addProjectionInjectionAndImplementationMethod(method);
          addProjectionDefinitionForAbstractMethod(method);
        } else if (isInjectionMethod(method)) {
          if (isAutoGuideMethod(method)) {
            if (!isReturnGuide(method)) {
              throw ConfigurationExceptions.withMessage("Guide injection method '{0}' in class {1} must return guide",
                  method.name(), annotatedType.className()
              );
            }
            addAutoGuideInjectionAndImplementationMethod(method);
          } else if (isReturnGuide(method)) {
            addGuideInjectionAndImplementationMethod(method);
          } else {
            addProjectionInjectionAndImplementationMethod(method);
          }
        } else {
          throw ConfigurationExceptions.withMessage("Undefined abstract method '{0}' in class {1}",
              method.name(), annotatedType.className()
          );
        }
      } else {
        if (isProjectionMethod(method)) {
          addProjectionInjectionAndImplementationMethod(method);
          addProjectionDefinitionBasedOnMethodAction(method);
        }
      }
    }
  }

  private void addProjectionDefinitionForAbstractMethod(MethodStatement method) {
    for (AnnotationInstance annotation : method.annotations()) {
      Optional<AnnotationInstance> projectionDefinitionAnnotation = annotation.annotationStatement().selectAnnotation(
          ProjectionDefinition.class.getCanonicalName());
      if (projectionDefinitionAnnotation.isPresent()) {
        AnnotationInstance projectionDefinition = projectionDefinitionAnnotation.get();
        Optional<Instance> providerClass = projectionDefinition.valueOf("provider");
        if (providerClass.isPresent() && isProjectionProviderDefined(providerClass.get())) {
          addProjectionDefinitionBasedOnProviderClass(method, providerClass.get().asClass().orElseThrow());
          return;
        }
      }
    }
    throw UnexpectedExceptions.withMessage("Projection definition is not found. See method {0} in unit {1}",
        method.name(), method.owner().canonicalName());
  }

  private static boolean isProjectionProviderDefined(Instance providerClass) {
    return !ProjectionTargetSupplier.class.getCanonicalName().equals(
        providerClass.asClass().orElseThrow().type().canonicalName()
    );
  }

  private void addProjectionDefinitionBasedOnProviderClass(
      MethodStatement method, ClassInstance projectionProvider
  ) {
    var sb = new StringBuilder();
    sb.append(context.addToImportAndGetSimpleName(ProjectionDefinitionBasedOnProviderClasses.class)).append(".get(\n");
    sb.append("  ").append(annotatedType.simpleName()).append(".class,\n");
    sb.append("  \"").append(ProjectionFunctions.getProjectionName(method)).append("\",\n");
    sb.append("  ").append(buildTypeDeclaration(method.returnType().orElseThrow())).append(".class,\n");
    sb.append("  ").append(method.selectAnnotation(Projection.class).orElseThrow().lazy()).append(",\n");
    sb.append("  \"").append(projectionProvider.type().canonicalName()).append("\"");
    sb.append(")");
    projectionDefinitions.add(sb.toString());
  }

  private void addProjectionDefinitionBasedOnMethodAction(MethodStatement method) {
    var sb = new StringBuilder();
    sb.append(context.addToImportAndGetSimpleName(ProjectionDefinitionBasedOnMethodActions.class)).append(".get(\n");
    sb.append("  ").append(annotatedType.simpleName()).append(".class,\n");
    sb.append("  \"").append(ProjectionFunctions.getProjectionName(method)).append("\",\n");
    sb.append("  ").append(buildTypeDeclaration(method.returnType().orElseThrow())).append(".class,\n");
    sb.append("  ").append(method.selectAnnotation(Projection.class).orElseThrow().lazy()).append(",\n");
    sb.append("  ").append("Actions.get(super::").append(method.name()).append(", ");
    sb.append(buildTypeDeclaration(method.returnType().orElseThrow())).append(".class");

    for (MethodParam param : method.params()) {
      sb.append(", ");
      sb.append(buildTypeDeclaration(param.type())).append(".class");
    }
    sb.append(")");

    for (MethodParam param : method.params()) {
      sb.append(",\n  \"");
      sb.append(param.name());
      sb.append("\", ");
      sb.append(buildTypeDeclaration(param.type())).append(".class");
    }
    sb.append("\n)");
    projectionDefinitions.add(sb.toString());
  }

  private void addProjectionInjectionAndImplementationMethod(MethodStatement method) {
    context.addImport(Modules.class);
    context.addImport(ProjectionInjection.class);

    String injectionName = method.name();
    String injectionType = method.returnType().orElseThrow().actualDeclaration();

    Map<String, Object> injection = new HashMap<>();
    injection.put("kind", "projection");
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
    return "return (" + injectionType + ") this.$unit.injection(" + injectionIndex + ").value();";
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
    return ObjectFunctions.isObjectHandleType(method.returnType().orElseThrow());
  }

  private boolean isReturnGuide(MethodStatement method) {
    return GuideFunctions.isGuideType(method.returnType().orElseThrow());
  }
}
