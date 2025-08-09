package tech.intellispaces.reflections.framework.annotationprocessor.resources;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import tech.intellispaces.annotationprocessor.Artifact;
import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactImpl;
import tech.intellispaces.annotationprocessor.ArtifactKinds;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;

public class FactoriesResourceGenerator implements ArtifactGenerator {
  private final Set<String> factories = new HashSet<>();

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
        NameConventionFunctions.getFactoriesResourceName(),
        String.join("\n", factories).toCharArray()
    ));
  }
}
