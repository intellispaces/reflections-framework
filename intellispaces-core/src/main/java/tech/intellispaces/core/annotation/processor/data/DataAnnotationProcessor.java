package tech.intellispaces.core.annotation.processor.data;

import com.google.auto.service.AutoService;
import tech.intellispaces.annotations.AnnotatedTypeProcessor;
import tech.intellispaces.annotations.generator.ArtifactGenerator;
import tech.intellispaces.annotations.validator.AnnotatedTypeValidator;
import tech.intellispaces.core.annotation.Data;
import tech.intellispaces.core.annotation.processor.AnnotationProcessorFunctions;
import tech.intellispaces.core.validation.DataValidator;
import tech.intellispaces.javastatements.customtype.CustomType;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class DataAnnotationProcessor extends AnnotatedTypeProcessor {

  public DataAnnotationProcessor() {
    super(Data.class, Set.of(ElementKind.INTERFACE));
  }

  @Override
  protected boolean isApplicable(CustomType dataType) {
    return AnnotationProcessorFunctions.isAutoGenerationEnabled(dataType);
  }

  @Override
  protected AnnotatedTypeValidator getValidator() {
    return new DataValidator();
  }

  @Override
  protected List<ArtifactGenerator> makeArtifactGenerators(CustomType dataType, RoundEnvironment roundEnv) {
    return AnnotationProcessorFunctions.makeDataArtifactGenerators(dataType);
  }
}
