package tech.intellispaces.jaquarius.annotationprocessor.objectprovider;

import tech.intellispaces.commons.annotation.processor.Artifact;
import tech.intellispaces.commons.annotation.processor.ArtifactGenerator;
import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.annotation.processor.ArtifactImpl;
import tech.intellispaces.commons.annotation.processor.ArtifactTypes;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ObjectProviderMetaInfGenerator implements ArtifactGenerator {
  private final List<String> objectFactories = new ArrayList<>();

  public ObjectProviderMetaInfGenerator() {
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
        ArtifactTypes.ResourceFile,
        generatedArtifactName(),
        String.join("\n", objectFactories).toCharArray()
    ));
  }
}
