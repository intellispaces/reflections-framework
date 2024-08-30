package intellispaces.core.annotation.processor.data;

import intellispaces.annotations.context.AnnotationProcessingContext;
import intellispaces.core.annotation.Name;
import intellispaces.core.annotation.ObjectHandle;
import intellispaces.core.annotation.processor.AbstractGenerationTask;
import intellispaces.core.common.NameConventionFunctions;
import intellispaces.core.object.ObjectHandleTypes;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.method.MethodStatement;
import intellispaces.javastatements.reference.TypeReference;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnmovableDataHandleGenerationTask extends AbstractGenerationTask {
  private final List<Map<String, String>> projectionProperties = new ArrayList<>();

  public UnmovableDataHandleGenerationTask(CustomType initiatorType, CustomType dataType) {
    super(initiatorType, dataType);
  }

  @Override
  public boolean isRelevant(AnnotationProcessingContext context) {
    return true;
  }

  @Override
  public String artifactName() {
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
    context.generatedClassCanonicalName(artifactName());
    if (annotatedType.isNested()) {
      context.addImport(sourceClassCanonicalName());
    }
    context.addImport(Name.class);
    context.addImport(ObjectHandle.class);

    analyzeProjections();
    return true;
  }

  private void analyzeProjections() {
    for (MethodStatement method : annotatedType.actualMethods()) {
      TypeReference type = method.returnType().orElseThrow();
      String handleType = getObjectHandleDeclaration(type, ObjectHandleTypes.Unmovable);

      Map<String, String> properties = new HashMap<>();
      properties.put("type", handleType);
      properties.put("name", method.name());
      projectionProperties.add(properties);
    }
  }
}
