package tech.intellispaces.reflections.framework.annotationprocessor.domain;

import java.util.UUID;

import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.javareflection.customtype.CustomTypes;
import tech.intellispaces.reflections.framework.annotation.Domain;
import tech.intellispaces.reflections.framework.annotation.Ignore;
import tech.intellispaces.reflections.framework.annotationprocessor.ReflectionsArtifactGenerator;

public class PenaltyRoundDomainGenerator extends ReflectionsArtifactGenerator {
  private final int index;

  public PenaltyRoundDomainGenerator(int index) {
    super(CustomTypes.of(Object.class));
    this.index = index;
  }

  @Override
  protected String templateName() {
    return "/penalty_round_domain.template";
  }

  @Override
  public String generatedArtifactName() {
    return "annotationprocessor.PenaltyRound" + index + "Domain";
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImport(Domain.class);
    addImport(Ignore.class);
    addVariable("id", UUID.randomUUID().toString());
    return true;
  }
}
