package tech.intellispaces.reflections.framework.annotationprocessor.object.factory;

import java.util.List;
import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;

import com.google.auto.service.AutoService;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.reflections.framework.annotation.Factory;
import tech.intellispaces.reflections.framework.annotationprocessor.AnnotationFunctions;
import tech.intellispaces.reflections.framework.annotationprocessor.ReflectionsArtifactProcessor;
import tech.intellispaces.jstatements.customtype.CustomType;

@AutoService(Processor.class)
public class ObjectFactoryProcessor extends ArtifactProcessor {
  private final ObjectFactoryMetaInfGenerator metaInfGenerator = new ObjectFactoryMetaInfGenerator();

  public ObjectFactoryProcessor() {
    super(ElementKind.CLASS, Factory.class, ReflectionsArtifactProcessor.SOURCE_VERSION);
  }

  @Override
  public boolean isApplicable(CustomType objectFactoryType) {
    return AnnotationFunctions.isAutoGenerationEnabled(objectFactoryType);
  }

  @Override
  public ArtifactValidator validator() {
    return null;
  }

  @Override
  public List<ArtifactGenerator> makeGenerators(CustomType objectFactoryType, ArtifactGeneratorContext context) {
    return List.of(
        metaInfGenerator,
        new ObjectFactoryWrapperGenerator(objectFactoryType, metaInfGenerator)
    );
  }
}
