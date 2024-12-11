package tech.intellispaces.jaquarius.annotationprocessor.data;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.jaquarius.annotation.Data;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationProcessorConstants;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationProcessorFunctions;
import tech.intellispaces.java.reflection.customtype.CustomType;

import javax.lang.model.element.ElementKind;
import java.util.List;

public class DataProcessor extends ArtifactProcessor {

  public DataProcessor() {
    super(ElementKind.INTERFACE, Data.class, AnnotationProcessorConstants.SOURCE_VERSION);
  }

  @Override
  public boolean isApplicable(CustomType dataType) {
    return AnnotationProcessorFunctions.isAutoGenerationEnabled(dataType);
  }

  @Override
  public ArtifactValidator validator() {
    return new DataValidator();
  }

  @Override
  public List<ArtifactGenerator> makeGenerators(CustomType dataType, ArtifactGeneratorContext jobContext) {
    return AnnotationProcessorFunctions.makeDataArtifactGenerators(dataType);
  }
}
