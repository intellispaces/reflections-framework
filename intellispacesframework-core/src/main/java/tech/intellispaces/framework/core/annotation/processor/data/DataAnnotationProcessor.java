package tech.intellispaces.framework.core.annotation.processor.data;

import com.google.auto.service.AutoService;
import tech.intellispaces.framework.annotationprocessor.AnnotatedTypeProcessor;
import tech.intellispaces.framework.annotationprocessor.generator.ArtifactGenerator;
import tech.intellispaces.framework.annotationprocessor.validator.AnnotatedTypeValidator;
import tech.intellispaces.framework.core.annotation.Data;
import tech.intellispaces.framework.core.annotation.processor.AnnotationProcessorFunctions;
import tech.intellispaces.framework.core.validation.DataValidator;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

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
