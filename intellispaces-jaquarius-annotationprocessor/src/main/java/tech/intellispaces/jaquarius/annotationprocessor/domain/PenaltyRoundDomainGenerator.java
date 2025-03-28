package tech.intellispaces.jaquarius.annotationprocessor.domain;

import tech.intellispaces.commons.annotation.processor.ArtifactGeneratorContext;
import tech.intellispaces.commons.reflection.customtype.CustomTypes;
import tech.intellispaces.jaquarius.annotation.Domain;
import tech.intellispaces.jaquarius.annotation.Ignore;
import tech.intellispaces.jaquarius.annotationprocessor.JaquariusArtifactGenerator;

import java.util.UUID;

public class PenaltyRoundDomainGenerator extends JaquariusArtifactGenerator {
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
    return "rounds.PenaltyRound" + index + "Domain";
  }

  @Override
  public boolean isRelevant(ArtifactGeneratorContext context) {
    return true;
  }

  @Override
  protected boolean analyzeSourceArtifact(ArtifactGeneratorContext context) {
    addImport(Domain.class);
    addImport(Ignore.class);
    addVariable("did", UUID.randomUUID().toString());
    return true;
  }
}
