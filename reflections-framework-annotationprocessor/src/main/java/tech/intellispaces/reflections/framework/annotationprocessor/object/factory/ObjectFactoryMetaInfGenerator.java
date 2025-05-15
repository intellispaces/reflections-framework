package tech.intellispaces.reflections.framework.annotationprocessor.object.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import tech.intellispaces.annotationprocessor.Artifact;
import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactImpl;
import tech.intellispaces.annotationprocessor.ArtifactKinds;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;

public class ObjectFactoryMetaInfGenerator implements ArtifactGenerator {
  private final List<String> objectFactories = new ArrayList<>();

  public ObjectFactoryMetaInfGenerator() {
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return context.isOverRound();
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getObjectFactoriesResourceName();
  }

  public void addObjectFactory(String objectFactory) {
    objectFactories.add(objectFactory);
  }

  @Override
  public Optional<Artifact> generate(ArtifactGeneratorContext context) {
    if (objectFactories.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(new ArtifactImpl(
        ArtifactKinds.ResourceFile,
        generatedArtifactName(),
        String.join("\n", objectFactories).toCharArray()
    ));
  }
}
