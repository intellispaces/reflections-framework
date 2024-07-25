package tech.intellispaces.framework.core.annotation.processor.domain;

import tech.intellispaces.framework.core.annotation.processor.ArtifactTypes;
import tech.intellispaces.framework.core.annotation.processor.PreprocessingAnnotationFunctions;
import tech.intellispaces.framework.core.common.NameConventionFunctions;
import tech.intellispaces.framework.core.object.ObjectHandle;
import tech.intellispaces.framework.core.object.ObjectHandleTypes;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.instance.AnnotationInstance;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BaseObjectHandleGenerator extends AbstractDomainObjectHandleGenerator {
  private List<Map<String, String>> additionalMethods;

  public BaseObjectHandleGenerator(CustomType domainType) {
    super(domainType);
  }

  @Override
  public String getArtifactName() {
    return NameConventionFunctions.getBaseObjectHandleTypename(annotatedType.className());
  }

  @Override
  protected ObjectHandleTypes getObjectHandleType() {
    return ObjectHandleTypes.Base;
  }

  @Override
  protected String templateName() {
    return "/base_object_handle.template";
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
    vars.put("methods", methods);
    vars.put("additionalMethods", additionalMethods);
    vars.put("importedClasses", context.getImports());
    vars.put("objectHandleName", context.simpleNameOf(ObjectHandle.class));
    return vars;
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(getArtifactName());
    context.addImport(sourceClassCanonicalName());

    domainTypeParamsFull = annotatedType.typeParametersFullDeclaration();
    domainTypeParamsBrief = annotatedType.typeParametersBriefDeclaration();

    analyzeObjectHandleMethods(annotatedType, roundEnv);
    analyzeAdditionalOMethods(annotatedType, roundEnv);

    context.addImport(ObjectHandle.class);

    return true;
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
        "javadoc", buildGeneratedMethodJavadoc(method.owner().canonicalName(), method.name()),
        "declaration", sb.toString()
    );
  }
}
