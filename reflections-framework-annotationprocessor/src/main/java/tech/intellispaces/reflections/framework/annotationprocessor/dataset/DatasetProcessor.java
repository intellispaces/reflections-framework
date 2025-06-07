package tech.intellispaces.reflections.framework.annotationprocessor.dataset;

import java.util.List;
import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;

import com.google.auto.service.AutoService;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.reflections.framework.annotation.Dataset;
import tech.intellispaces.reflections.framework.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.reflections.framework.annotationprocessor.ReflectionsArtifactProcessor;

@AutoService(Processor.class)
public class DatasetProcessor extends ArtifactProcessor {

  public DatasetProcessor() {
    super(ElementKind.INTERFACE, Dataset.class, ReflectionsArtifactProcessor.SOURCE_VERSION);
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
        new DatasetGenerator(domainType),
        new DatasetBuilderGenerator(domainType)
    );
  }
}
