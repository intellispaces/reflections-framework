package intellispaces.jaquarius.annotation.processor.preprocessing;

import com.google.auto.service.AutoService;
import intellispaces.common.annotationprocessor.AnnotatedTypeProcessor;
import intellispaces.common.annotationprocessor.generator.Generator;
import intellispaces.common.annotationprocessor.validator.AnnotatedTypeValidator;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.jaquarius.annotation.Preprocessing;
import intellispaces.jaquarius.annotation.processor.AnnotationProcessorFunctions;

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
  public List<Generator> makeGenerators(CustomType initiatorType, CustomType customType, RoundEnvironment roundEnv) {
    return AnnotationProcessorFunctions.makePreprocessingArtifactGenerators(initiatorType, customType, roundEnv);
  }
}
