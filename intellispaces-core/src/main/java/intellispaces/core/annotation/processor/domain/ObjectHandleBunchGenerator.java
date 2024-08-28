package intellispaces.core.annotation.processor.domain;

import intellispaces.actions.common.string.StringActions;
import intellispaces.actions.runner.Runner;
import intellispaces.core.annotation.Transition;
import intellispaces.core.common.NameConventionFunctions;
import intellispaces.core.object.ObjectHandleTypes;
import intellispaces.core.space.domain.DomainFunctions;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.method.MethodStatement;
import intellispaces.javastatements.reference.CustomTypeReference;
import intellispaces.javastatements.reference.TypeReference;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ObjectHandleBunchGenerator extends AbstractDomainObjectHandleGenerator {
  private String extendDeclaration;

  public ObjectHandleBunchGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  public String getArtifactName() {
    return NameConventionFunctions.getBunchObjectHandleTypename(annotatedType.className());
  }

  @Override
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Bunch;
  }

  @Override
  protected String templateName() {
    return "/object_handle_bunch.template";
  }

  protected Map<String, Object> templateVariables() {
    Map<String, Object> vars = new HashMap<>();
    vars.put("generatedAnnotation", makeGeneratedAnnotation());
    vars.put("packageName", context.packageName());
    vars.put("sourceClassName", sourceClassCanonicalName());
    vars.put("sourceClassSimpleName", sourceClassSimpleName());
    vars.put("classSimpleName", context.generatedClassSimpleName());
    vars.put("movableClassSimpleName", movableClassSimpleName());
    vars.put("domainTypeParamsFull", domainTypeParamsFull);
    vars.put("domainTypeParamsBrief", domainTypeParamsBrief);
    vars.put("extendDeclaration", extendDeclaration);
    vars.put("domainMethods", methods);
    vars.put("importedClasses", context.getImports());
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(getArtifactName());
    context.addImport(sourceClassCanonicalName());

    domainTypeParamsFull = annotatedType.typeParametersFullDeclaration();
    domainTypeParamsBrief = annotatedType.typeParametersBriefDeclaration();
    extendDeclaration = buildExtendDeclaration();
    analyzeObjectHandleMethods(annotatedType, roundEnv);
    return true;
  }

  @Override
  protected Stream<MethodStatement> getObjectHandleMethods(
      CustomType customType, RoundEnvironment roundEnv
  ) {
    return buildActualType(customType, roundEnv)
        .actualMethods().stream()
        .filter(this::isNotGetDomainMethod);
  }

  private String buildExtendDeclaration() {
    List<CustomTypeReference> parents = annotatedType.parentTypes().stream()
        .filter(DomainFunctions::isDomainType)
        .toList();
    if (parents.isEmpty()) {
      return "";
    }

    var sb = new StringBuilder();
    sb.append("extends ");
    Runner commaAppender = StringActions.skippingFirstTimeCommaAppender(sb);
    for (CustomTypeReference parent : parents) {
      commaAppender.run();
      sb.append(context.addToImportAndGetSimpleName(
          NameConventionFunctions.getBunchObjectHandleTypename(parent.targetType().className()))
      );
      sb.append(parent.typeArgumentsDeclaration(this::typeConverter));
    }
    return sb.toString();
  }

  @Override
  protected void appendMethodReturnHandleType(StringBuilder sb, MethodStatement method) {
    TypeReference domainReturnType = method.returnType().orElseThrow();
    sb.append(getObjectHandleDeclaration(domainReturnType, ObjectHandleTypes.Bunch));
  }

  @Override
  protected Map<String, String> buildMethod(MethodStatement method) {
    if (method.hasAnnotation(Transition.class)) {
      return super.buildMethod(method);
    } else {
      return buildAdditionalMethod(method);
    }
  }

  private Map<String, String> buildAdditionalMethod(MethodStatement method) {
    var sb = new StringBuilder();
    appendMethodTypeParameters(sb, method);
    appendMethodReturnType(sb, method);
    sb.append(" ");
    sb.append(method.name());
    sb.append("(");
    appendMethodParameters(sb, method);
    sb.append(")");
    appendMethodExceptions(sb, method);
    return Map.of(
        "javadoc", buildGeneratedMethodJavadoc(method.owner().canonicalName(), method),
        "declaration", sb.toString()
    );
  }
}
