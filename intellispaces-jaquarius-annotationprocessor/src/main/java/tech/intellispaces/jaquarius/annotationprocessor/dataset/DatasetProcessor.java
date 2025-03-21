package tech.intellispaces.jaquarius.annotationprocessor.dataset;

import com.google.auto.service.AutoService;
import tech.intellispaces.commons.annotation.processor.ArtifactGenerator;
import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.annotation.processor.ArtifactProcessor;
import tech.intellispaces.commons.annotation.processor.ArtifactValidator;
import tech.intellispaces.commons.reflection.customtype.CustomType;
import tech.intellispaces.jaquarius.annotation.Dataset;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactProcessor;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.List;

@AutoService(Processor.class)
public class DatasetProcessor extends ArtifactProcessor {

  public DatasetProcessor() {
    super(ElementKind.INTERFACE, Dataset.class, JaquariusArtifactProcessor.SOURCE_VERSION);
  }

  @Override
  public boolean isApplicable(CustomType dataType) {
    return AnnotationFunctions.isAutoGenerationEnabled(dataType);
  }

  @Override
  public ArtifactValidator validator() {
    return new DatasetValidator();
  }

  @Override
  public List<ArtifactGenerator> makeGenerators(CustomType domainType, ArtifactGeneratorContext jobContext) {
    return List.of(
        new UnmovableDatasetGenerator(domainType),
        new DatasetBuilderGenerator(domainType)
    );
  }
}
