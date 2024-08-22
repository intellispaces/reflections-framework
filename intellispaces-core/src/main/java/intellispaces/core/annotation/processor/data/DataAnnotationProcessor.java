package intellispaces.core.annotation.processor.data;

import com.google.auto.service.AutoService;
import intellispaces.annotations.AnnotatedTypeProcessor;
import intellispaces.annotations.generator.ArtifactGenerator;
import intellispaces.annotations.validator.AnnotatedTypeValidator;
import intellispaces.core.annotation.Data;
import intellispaces.core.annotation.processor.AnnotationProcessorFunctions;
import intellispaces.core.validation.DataValidator;
import intellispaces.javastatements.customtype.CustomType;

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
