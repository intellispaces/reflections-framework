package tech.intellispaces.core.annotation.processor.unit;

import tech.intellispaces.actions.Action;
import tech.intellispaces.actions.Actions;
import tech.intellispaces.actions.common.string.StringActions;
import tech.intellispaces.actions.getter.ResettableGetter;
import tech.intellispaces.actions.runner.Runner;
import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.commons.type.TypeFunctions;
import tech.intellispaces.core.annotation.Ordinal;
import tech.intellispaces.core.annotation.Projection;
import tech.intellispaces.core.annotation.ProjectionDefinition;
import tech.intellispaces.core.annotation.Wrapper;
import tech.intellispaces.core.annotation.processor.AbstractGenerator;
import tech.intellispaces.core.common.NameConventionFunctions;
import tech.intellispaces.core.guide.GuideFunctions;
import tech.intellispaces.core.system.Injection;
import tech.intellispaces.core.system.Modules;
import tech.intellispaces.core.system.ProjectionInjection;
import tech.intellispaces.core.system.ProjectionRegistry;
import tech.intellispaces.core.system.UnitWrapper;
import tech.intellispaces.core.system.shadow.ProjectionInjectionImpl;
import tech.intellispaces.core.system.shadow.ShadowUnit;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.instance.AnnotationInstance;
import tech.intellispaces.javastatements.instance.ClassInstance;
import tech.intellispaces.javastatements.instance.Instance;
import tech.intellispaces.javastatements.method.MethodParam;
import tech.intellispaces.javastatements.method.MethodStatement;
import tech.intellispaces.javastatements.reference.NamedReference;
import tech.intellispaces.javastatements.reference.TypeReference;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class UnitWrapperGenerator extends AbstractGenerator {
  private final List<Map<String, Object>> methodsProperties = new ArrayList<>();
  private List<Map<String, Object>> guideMethods;
  private final List<Map<String, String>> projectionProvidersProperties = new ArrayList<>();
  private final List<Map<String, String>> projectionInjectionsProperties = new ArrayList<>();
  private List<String> actionGetters;

  public UnitWrapperGenerator(CustomType annotatedType) {
    super(annotatedType);
  }

  @Override
  public String getArtifactName() {
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
    vars.put("importedClasses", context.getImports());
    vars.put("projectionDefinitions", projectionProvidersProperties);
    vars.put("projectionInjections", projectionInjectionsProperties);
    vars.put("actionGetters", actionGetters);
    vars.put("methods", methodsProperties);
    vars.put("guideMethods", guideMethods);
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(getArtifactName());
    if (annotatedType.isNested()) {
      context.addImport(sourceClassCanonicalName());
    }

    context.addImport(List.class);
    context.addImport(Collections.class);
    context.addImport(ArrayList.class);
    context.addImport(Action.class);
    context.addImport(Actions.class);
    context.addImport(ResettableGetter.class);
    context.addImport(ShadowUnit.class);
    context.addImport(Wrapper.class);
    context.addImport(Ordinal.class);
    context.addImport(Modules.class);
    context.addImport(Injection.class);
    context.addImport(ProjectionRegistry.class);
    context.addImport(TypeFunctions.class);
    context.addImport(UnitWrapper.class);

    analyzeGuideMethods();
    analyzeActionGetters();
    analyzeMethods();
    return true;
  }

  private void analyzeGuideMethods() {
    this.guideMethods = annotatedType.declaredMethods().stream()
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
    Runner commaAppender = StringActions.skippingFirstTimeCommaAppender(sb);
    for (MethodParam param : guideMethod.params()) {
      commaAppender.run();
      sb.append(param.name());
    }
    sb.append(");\n}");
    return Map.of("declaration", sb.toString());
  }

  private void analyzeActionGetters() {
    this.actionGetters = annotatedType.declaredMethods().stream()
        .filter(GuideFunctions::isGuideMethod)
        .sorted(Comparator.comparingInt(m -> m.selectAnnotation(Ordinal.class).orElseThrow().value()))
        .map(this::buildGuideActionGetter)
        .toList();
  }

  private String buildGuideActionGetter(MethodStatement guideMethod) {
    String actionGetterMame = buildActionGetterName(guideMethod);

    var sb = new StringBuilder();
    sb.append("Actions.get(super::").append(guideMethod.name());
    sb.append(", ");
    sb.append(buildGuideActionTypeParameter(guideMethod.returnType().orElseThrow())).append(".class");
    for (MethodParam param : guideMethod.params()) {
      sb.append(", ");
      sb.append(buildGuideActionTypeParameter(param.type())).append(".class");
    }
    sb.append(")");
    return sb.toString();
  }

  private String buildGuideActionTypeParameter(TypeReference type) {
    if (type.isNamedReference()) {
      NamedReference namedType = type.asNamedReferenceOrElseThrow();
      if (namedType.extendedBounds().isEmpty()) {
        return "Object";
      }
    }
    return context.addToImportAndGetSimpleName(
        type.asCustomTypeReferenceOrElseThrow().targetType().canonicalName()
    );
  }

  private String buildActionGetterName(MethodStatement guideMethod) {
    var sb = new StringBuilder();
    sb.append("_");
    sb.append(NameConventionFunctions.joinMethodNameAndParameterTypes(guideMethod));
    sb.append("ActionGetter");
    return sb.toString();
  }

  private void analyzeMethods() {
    for (MethodStatement method : annotatedType.declaredMethods()) {
      if (isProjectionMethod(method)) {
        addProjectionOverrideMethod(method);
        if (method.isAbstract()) {
          addProjectionImplementationMethod(method);
        } else {
          addProjectionProxyMethod(method);
        }
      } else {
        if (method.isAbstract()) {
          addInjectionImplementationMethod(method);
        }
      }
    }
  }

  private void addProjectionImplementationMethod(MethodStatement method) {
    for (AnnotationInstance methodAnnotation : method.annotations()) {
      Optional<AnnotationInstance> projectionDefinitionAnnotation = methodAnnotation.annotationStatement().selectAnnotation(
          ProjectionDefinition.class.getCanonicalName());
      if (projectionDefinitionAnnotation.isPresent()) {
        AnnotationInstance projectionDefinition = projectionDefinitionAnnotation.get();
        Optional<Instance> providerClass = projectionDefinition.elementValue("provider");
        if (providerClass.isPresent()) {
          addProjectionImplementationMethodWhenProjectionProviderDefined(
              method, providerClass.get().asClass().orElseThrow());
           return;
        }
      }
    }
    throw UnexpectedViolationException.withMessage("Projection definition is not found. See method {} in unit {}",
        method.name(), method.owner().canonicalName());
  }

  private void addProjectionImplementationMethodWhenProjectionProviderDefined(
      MethodStatement method, ClassInstance projectionProvider
  ) {
    String projectionName = method.name();
    CustomType providerType = projectionProvider.type();

    context.addImport(providerType.canonicalName());
    String providerTypename = context.simpleNameOf(projectionProvider.type().canonicalName());

    String projectionType = method.returnType().orElseThrow().actualDeclaration();

    Map<String, String> projectionProviderProperties = new HashMap<>();
    projectionProviderProperties.put("name", projectionName);
    projectionProviderProperties.put("type", projectionType);
    projectionProviderProperties.put("providerType", providerTypename);
    projectionProvidersProperties.add(projectionProviderProperties);

    Map<String, Object> methodProperties = new HashMap<>();
    methodProperties.put("signature", buildMethodSignature(method, "_" + method.name()));
    methodProperties.put("body", buildProjectionProviderMethodBody(projectionName, projectionType));
    methodProperties.put("annotations", List.of(copyProjectionAnnotation(method)));
    methodProperties.put("javadoc", "");
    methodsProperties.add(methodProperties);
  }

  private void addInjectionImplementationMethod(MethodStatement method) {
    context.addImport(Modules.class);
    context.addImport(ProjectionInjection.class);
    context.addImport(ProjectionInjectionImpl.class);

    String injectionName = method.name();
    String injectionType = method.returnType().get().actualDeclaration();

    Map<String, String> injectionProperties = new HashMap<>();
    injectionProperties.put("name", injectionName);
    injectionProperties.put("type", injectionType);
    projectionInjectionsProperties.add(injectionProperties);

    Map<String, Object> methodProperties = new HashMap<>();
    methodProperties.put("signature", buildMethodSignature(method));
    methodProperties.put("body", buildInjectionMethodBody(injectionName, injectionType));
    methodProperties.put("annotations", List.of(Override.class.getSimpleName()));
    methodProperties.put("javadoc", "");
    methodsProperties.add(methodProperties);
  }

  private void addProjectionOverrideMethod(MethodStatement projectionMethod) {
    Map<String, Object> methodProperties = new HashMap<>();

    var signature = new StringBuilder();
    TypeReference returnType = projectionMethod.returnType().get();
    context.addImports(returnType.dependencyTypenames());
    signature.append(returnType.actualDeclaration());
    signature.append(" ");
    signature.append(projectionMethod.name());
    signature.append("(");

    Runner commaAppender = StringActions.skippingFirstTimeCommaAppender(signature);
    for (MethodParam param : projectionMethod.params()) {
      commaAppender.run();
      context.addImports(param.type().dependencyTypenames());
      signature.append(param.type().actualDeclaration());
      signature.append(" ");
      signature.append(param.name());
    }
    signature.append(")");

    String exceptions = projectionMethod.exceptions().stream()
        .map(e -> e.asCustomTypeReference().orElseThrow().targetType())
        .peek(e -> context.addImport(e.canonicalName()))
        .map(e -> context.simpleNameOf(e.canonicalName()))
        .collect(Collectors.joining(", "));
    if (!exceptions.isEmpty()) {
      signature.append(" throws ").append(exceptions);
    }

    var body = new StringBuilder();
    if (projectionMethod.params().isEmpty()) {
      String injectionName = projectionMethod.name();
      String injectionType = projectionMethod.returnType().get().actualDeclaration();

      context.addImport(ProjectionInjection.class);
      context.addImport(ProjectionInjectionImpl.class);

      Map<String, String> injectionProperties = new HashMap<>();
      injectionProperties.put("name", injectionName);
      injectionProperties.put("type", injectionType);
      projectionInjectionsProperties.add(injectionProperties);

      body.append(buildInjectionMethodBody(injectionName, injectionType));
    } else {
      context.addImport(UnexpectedViolationException.class.getCanonicalName());

      body.append("throw new ");
      body.append(UnexpectedViolationException.class.getSimpleName());
      body.append("();");
    }

    methodProperties.put("signature", signature.toString());
    methodProperties.put("body", body.toString());
    methodProperties.put("annotations", List.of(Override.class.getSimpleName()));
    methodProperties.put("javadoc", "");

    methodsProperties.add(methodProperties);
  }

  private void addProjectionProxyMethod(MethodStatement method) {
    Map<String, Object> methodProperties = new HashMap<>();

    var signature = new StringBuilder();
    var body = new StringBuilder();

    TypeReference returnType = method.returnType().get();
    context.addImports(returnType.dependencyTypenames());
    signature.append(returnType.actualDeclaration());
    signature.append(" ");
    signature.append("_").append(method.name());
    signature.append("(");

    body.append("return super.");
    body.append(method.name());
    body.append("(");

    Runner commaAppender = StringActions.skippingFirstTimeCommaAppender(signature, body);
    for (MethodParam param : method.params()) {
      commaAppender.run();

      context.addImports(param.type().dependencyTypenames());
      signature.append(param.type().actualDeclaration());
      signature.append(" ");
      signature.append(param.name());

      body.append(" ");
      body.append(param.name());
    }
    signature.append(")");
    body.append(");");

    String exceptions = method.exceptions().stream()
        .map(e -> e.asCustomTypeReference().orElseThrow().targetType())
        .peek(e -> context.addImport(e.canonicalName()))
        .map(e -> context.simpleNameOf(e.canonicalName()))
        .collect(Collectors.joining(", "));
    if (!exceptions.isEmpty()) {
      signature.append(" throws ").append(exceptions);
    }

    methodProperties.put("signature", signature.toString());
    methodProperties.put("body", body.toString());
    methodProperties.put("annotations", List.of(copyProjectionAnnotation(method)));
    methodProperties.put("javadoc", """
      /**
       * Proxy method for projection '%s'.
       */
      """.formatted(method.name()));

    methodsProperties.add(methodProperties);
  }

  private String copyProjectionAnnotation(MethodStatement method) {
    context.addImport(Projection.class.getCanonicalName());
    return context.simpleNameOf(Projection.class.getCanonicalName()) +
        "(" + makeProjectionAnnotationAttributes(method) + ")";
  }

  private String makeProjectionAnnotationAttributes(MethodStatement method) {
    Projection annotation = method.selectAnnotation(Projection.class).orElseThrow();

    var sb = new StringBuilder();
    sb.append("value = \"").append(!annotation.value().isEmpty() ? annotation.value() : method.name());
    sb.append("\", ");
    sb.append("lazy = ").append(annotation.lazy());
    return sb.toString();
  }

  private String buildProjectionProviderMethodBody(String projectionName, String projectionTypename) {
    return "return (" + projectionTypename + ") _" + projectionName + "Provider.get();";
  }

  private String buildInjectionMethodBody(String injectionName, String injectionType) {
    return "return (" + injectionType + ") _" + injectionName + "Injection.value();";
  }

  private boolean isProjectionMethod(MethodStatement method) {
    return method.selectAnnotation(Projection.class).isPresent();
  }
}
