package tech.intellispaces.jaquarius.annotationprocessor.objectprovider;

import com.google.auto.service.AutoService;
import tech.intellispaces.commons.annotation.processor.ArtifactGenerator;
import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.annotation.processor.ArtifactProcessor;
import tech.intellispaces.commons.annotation.processor.ArtifactValidator;
import tech.intellispaces.commons.reflection.customtype.CustomType;
import tech.intellispaces.jaquarius.annotation.ObjectProvider;
import tech.intellispaces.jaquarius.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactProcessor;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;
import java.util.List;

@AutoService(Processor.class)
public class ObjectProviderProcessor extends ArtifactProcessor {
  private final ObjectProviderMetaInfGenerator metaInfGenerator = new ObjectProviderMetaInfGenerator();

  public ObjectProviderProcessor() {
    super(ElementKind.CLASS, ObjectProvider.class, JaquariusArtifactProcessor.SOURCE_VERSION);
  }

  @Override
  public boolean isApplicable(CustomType objectProviderType) {
    return AnnotationFunctions.isAutoGenerationEnabled(objectProviderType);
  }

  @Override
  public ArtifactValidator validator() {
    return null;
  }

  @Override
  public List<ArtifactGenerator> makeGenerators(CustomType objectProviderType, ArtifactGeneratorContext context) {
    return List.of(
        metaInfGenerator,
        new ObjectProviderWrapperGenerator(objectProviderType, metaInfGenerator)
    );
  }
}
