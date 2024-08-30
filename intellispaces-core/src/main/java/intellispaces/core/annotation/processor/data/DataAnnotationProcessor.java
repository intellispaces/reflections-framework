package intellispaces.core.annotation.processor.data;

import intellispaces.annotations.AnnotatedTypeProcessor;
import intellispaces.annotations.generator.GenerationTask;
import intellispaces.annotations.validator.AnnotatedTypeValidator;
import intellispaces.core.annotation.Data;
import intellispaces.core.annotation.processor.AnnotationProcessorFunctions;
import intellispaces.core.validation.DataValidator;
import intellispaces.javastatements.customtype.CustomType;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.Set;

public class DataAnnotationProcessor extends AnnotatedTypeProcessor {

  public DataAnnotationProcessor() {
    super(Data.class, Set.of(ElementKind.INTERFACE));
  }

  @Override
  public boolean isApplicable(CustomType dataType) {
    return AnnotationProcessorFunctions.isAutoGenerationEnabled(dataType);
  }

  @Override
  public AnnotatedTypeValidator getValidator() {
    return new DataValidator();
  }

  @Override
  public List<GenerationTask> makeTasks(CustomType initiatorType, CustomType dataType, RoundEnvironment roundEnv) {
    return AnnotationProcessorFunctions.makeDataArtifactGenerators(initiatorType, dataType);
  }
}
