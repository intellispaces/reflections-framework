package tech.intellispaces.framework.core.annotation.processor.data;

import tech.intellispaces.framework.core.annotation.processor.AbstractGenerator;
import tech.intellispaces.framework.core.common.NameConventionFunctions;
import tech.intellispaces.framework.core.object.ObjectHandleTypes;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReference;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnmovableDataHandleGenerator extends AbstractGenerator {
  private final List<Map<String, String>> projectionProperties = new ArrayList<>();

  public UnmovableDataHandleGenerator(CustomType dataType) {
    super(dataType);
  }

  @Override
  public String getArtifactName() {
    return NameConventionFunctions.getDataClassName(annotatedType.className());
  }

  @Override
  protected String templateName() {
    return "/unmovable_data_handle.template";
  }

  @Override
  protected Map<String, Object> templateVariables() {
    return Map.of(
        "generatedAnnotation", makeGeneratedAnnotation(),
        "packageName", context.packageName(),
        "sourceClassName", sourceClassCanonicalName(),
        "objectHandleClassName", NameConventionFunctions.getUnmovableObjectHandleTypename(annotatedType.className()),
        "classSimpleName", context.generatedClassSimpleName(),
        "importedClasses", context.getImports(),
        "projections", projectionProperties
    );
  }

  @Override
  protected boolean analyzeAnnotatedType(RoundEnvironment roundEnv) {
    context.generatedClassCanonicalName(getArtifactName());
    if (annotatedType.isNested()) {
      context.addImport(sourceClassCanonicalName());
    }

    analyzeProjections();
    return true;
  }

  private void analyzeProjections() {
    for (MethodStatement method : annotatedType.declaredMethods()) {
      TypeReference type = method.returnType().orElseThrow();
      String handleType = getObjectHandleDeclaration(type, ObjectHandleTypes.Unmovable);

      Map<String, String> properties = new HashMap<>();
      properties.put("type", handleType);
      properties.put("name", method.name());
      projectionProperties.add(properties);
    }
  }
}
