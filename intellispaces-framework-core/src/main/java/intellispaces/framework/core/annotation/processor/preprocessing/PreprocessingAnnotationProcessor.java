package intellispaces.framework.core.annotation.processor.preprocessing;

import com.google.auto.service.AutoService;
import intellispaces.common.annotationprocessor.AnnotatedTypeProcessor;
import intellispaces.common.annotationprocessor.generator.GenerationTask;
import intellispaces.common.annotationprocessor.validator.AnnotatedTypeValidator;
import intellispaces.framework.core.annotation.Preprocessing;
import intellispaces.framework.core.annotation.processor.AnnotationProcessorFunctions;
import intellispaces.common.javastatement.customtype.CustomType;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class PreprocessingAnnotationProcessor extends AnnotatedTypeProcessor {

  public PreprocessingAnnotationProcessor() {
    super(Preprocessing.class, Set.of(ElementKind.CLASS, ElementKind.INTERFACE));
  }

  @Override
  public boolean isApplicable(CustomType customType) {
    return AnnotationProcessorFunctions.isAutoGenerationEnabled(customType);
  }

  @Override
  public AnnotatedTypeValidator getValidator() {
    return null;
  }

  @Override
  public List<GenerationTask> makeTasks(CustomType initiatorType, CustomType customType, RoundEnvironment roundEnv) {
    return AnnotationProcessorFunctions.makePreprocessingArtifactGenerators(initiatorType, customType, roundEnv);
  }
}
