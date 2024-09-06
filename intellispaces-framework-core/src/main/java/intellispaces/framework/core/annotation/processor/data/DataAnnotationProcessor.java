package intellispaces.framework.core.annotation.processor.data;

import intellispaces.common.annotationprocessor.AnnotatedTypeProcessor;
import intellispaces.common.annotationprocessor.generator.GenerationTask;
import intellispaces.common.annotationprocessor.validator.AnnotatedTypeValidator;
import intellispaces.framework.core.annotation.Data;
import intellispaces.framework.core.annotation.processor.AnnotationProcessorFunctions;
import intellispaces.framework.core.validation.DataValidator;
import intellispaces.common.javastatement.customtype.CustomType;

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
