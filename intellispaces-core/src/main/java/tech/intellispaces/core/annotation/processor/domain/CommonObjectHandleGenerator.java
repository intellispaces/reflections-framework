package tech.intellispaces.core.annotation.processor.domain;

import tech.intellispaces.core.annotation.processor.ArtifactTypes;
import tech.intellispaces.core.annotation.processor.PreprocessingAnnotationFunctions;
import tech.intellispaces.core.common.NameConventionFunctions;
import tech.intellispaces.core.object.ObjectHandle;
import tech.intellispaces.core.object.ObjectHandleTypes;
import tech.intellispaces.core.space.SpaceConstants;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.instance.AnnotationInstance;
import tech.intellispaces.javastatements.method.MethodStatement;
import tech.intellispaces.javastatements.type.Type;
import tech.intellispaces.javastatements.type.Types;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class CommonObjectHandleGenerator extends AbstractDomainObjectHandleGenerator {
  private List<Map<String, String>> additionalMethods;
  private String objectHandleBaseline;
  private boolean hasPointParent;

  public CommonObjectHandleGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  public String getArtifactName() {
    return NameConventionFunctions.getObjectHandleTypename(annotatedType.className(), ObjectHandleTypes.Common);
  }

  @Override
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Common;
  }

  @Override
  protected String templateName() {
    return "/common_object_handle.template";
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
    vars.put("objectHandleBranch", objectHandleBaseline);
    vars.put("domainMethods", methods);
    vars.put("additionalMethods", additionalMethods);
    vars.put("importedClasses", context.getImports());
    vars.put("hasPointParent", hasPointParent);
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(getArtifactName());
    context.addImport(sourceClassCanonicalName());
    context.addImport(ObjectHandle.class);
    context.addImport(Type.class);
    context.addImport(Types.class);

    domainTypeParamsFull = annotatedType.typeParametersFullDeclaration();
    domainTypeParamsBrief = annotatedType.typeParametersBriefDeclaration();
    objectHandleBaseline = context.addToImportAndGetSimpleName(
        NameConventionFunctions.getObjectHandleBaselineTypename(annotatedType.canonicalName())
    );
    hasPointParent = hasPointParent();
    analyzeObjectHandleMethods(annotatedType, roundEnv);
    analyzeAdditionalOMethods(annotatedType, roundEnv);
    return true;
  }

  private boolean hasPointParent() {
    return annotatedType.canonicalName().equals(SpaceConstants.POINT_DOMAIN_NAME) ||
        annotatedType.hasParent(SpaceConstants.POINT_DOMAIN_NAME);
  }

  @Override
  protected Stream<MethodStatement> getObjectHandleMethods(
      CustomType customType, RoundEnvironment roundEnv
  ) {
    return super.getObjectHandleMethods(customType, roundEnv)
        .filter(m -> !hasPointParent || !m.name().equals(SpaceConstants.POINT_TO_BASE_DOMAIN_TRANSITION_SIMPLE_NAME));
  }

  private void analyzeAdditionalOMethods(CustomType customType, RoundEnvironment roundEnv) {
    this.additionalMethods = getAdditionalOMethods(customType, roundEnv).stream()
        .map(this::buildAdditionalMethod)
        .toList();
  }

  private List<MethodStatement> getAdditionalOMethods(CustomType customType, RoundEnvironment roundEnv) {
    List<MethodStatement> methods = new ArrayList<>();
    List<AnnotationInstance> preprocessingAnnotations = PreprocessingAnnotationFunctions.findPreprocessingAnnotations(
        customType, ArtifactTypes.ObjectHandle, roundEnv
    );
    for (AnnotationInstance preprocessingAnnotation : preprocessingAnnotations) {
      Optional<CustomType> extensionType = PreprocessingAnnotationFunctions.getPreprocessingExtendWith(
          preprocessingAnnotation
      );
      if (extensionType.isPresent()) {
        methods.addAll(extensionType.orElseThrow().declaredMethods());
      }
    }
    return methods;
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
