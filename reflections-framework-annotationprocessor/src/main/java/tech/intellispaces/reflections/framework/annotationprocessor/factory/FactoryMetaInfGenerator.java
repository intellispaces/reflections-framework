package tech.intellispaces.reflections.framework.annotationprocessor.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import tech.intellispaces.annotationprocessor.Artifact;
import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactImpl;
import tech.intellispaces.annotationprocessor.ArtifactKinds;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;

public class FactoryMetaInfGenerator implements ArtifactGenerator {
  private final List<String> factories = new ArrayList<>();

  public FactoryMetaInfGenerator() {
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return context.isOverRound();
  }

  @Override
  public String generatedArtifactName() {
    return NameConventionFunctions.getFactoriesResourceName();
  }

  public void addObjectFactory(String objectFactory) {
    factories.add(objectFactory);
  }

  @Override
  public Optional<Artifact> generate(ArtifactGeneratorContext context) {
    if (factories.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(new ArtifactImpl(
        ArtifactKinds.ResourceFile,
        generatedArtifactName(),
        String.join("\n", factories).toCharArray()
    ));
  }
}
