package tech.intellispaces.jaquarius.annotation.processor.data;

import tech.intellispaces.jaquarius.annotation.Data;
import tech.intellispaces.jaquarius.annotation.processor.AnnotationProcessorFunctions;
import tech.intellispaces.jaquarius.validation.DataValidator;
import tech.intellispaces.java.annotation.AnnotatedTypeProcessor;
import tech.intellispaces.java.annotation.generator.Generator;
import tech.intellispaces.java.annotation.validator.AnnotatedTypeValidator;
import tech.intellispaces.java.reflection.customtype.CustomType;

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
  public List<Generator> makeGenerators(CustomType initiatorType, CustomType dataType, RoundEnvironment roundEnv) {
    return AnnotationProcessorFunctions.makeDataArtifactGenerators(initiatorType, dataType);
  }
}
