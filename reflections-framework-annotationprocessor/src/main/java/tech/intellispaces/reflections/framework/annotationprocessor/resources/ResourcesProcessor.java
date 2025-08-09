package tech.intellispaces.reflections.framework.annotationprocessor.resources;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Processor;
import javax.lang.model.element.ElementKind;

import com.google.auto.service.AutoService;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.reflections.framework.annotation.Channel;
import tech.intellispaces.reflections.framework.annotation.Domain;
import tech.intellispaces.reflections.framework.annotation.Factory;
import tech.intellispaces.reflections.framework.annotationprocessor.ReflectionsArtifactProcessor;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;

@AutoService(Processor.class)
public class ResourcesProcessor extends ArtifactProcessor {
  private final OntologyResourceGenerator ontologyResourceGenerator = new OntologyResourceGenerator();
  private final FactoriesResourceGenerator factoriesResourceGenerator = new FactoriesResourceGenerator();

  public ResourcesProcessor() {
    super(Map.of(
        Domain.class, Set.of(ElementKind.INTERFACE),
        Channel.class, Set.of(ElementKind.INTERFACE),
        Factory.class, Set.of(ElementKind.CLASS)),
        ReflectionsArtifactProcessor.SOURCE_VERSION
    );
  }

  @Override
  public boolean isApplicable(CustomType customType) {
    return true;
  }

  @Override
  public ArtifactValidator validator() {
    return null;
  }

  @Override
  protected void trigger(CustomType annotatedType, Class<? extends Annotation> annotation) {
    if (Factory.class == annotation) {
      factoriesResourceGenerator.addObjectFactory(
          NameConventionFunctions.getObjectFactoryWrapperClassName(annotatedType.canonicalName())
      );
    }
  }

  @Override
  public List<ArtifactGenerator> makeGenerators(CustomType annotatedType, ArtifactGeneratorContext context) {
    return List.of(
        ontologyResourceGenerator,
        factoriesResourceGenerator
    );
  }
}
