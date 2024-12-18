package tech.intellispaces.jaquarius.annotationprocessor;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.java.reflection.customtype.CustomType;

import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.Set;

public class SampleAnnotatedTypeProcessor extends ArtifactProcessor {
  private CustomType annotatedType;

  public SampleAnnotatedTypeProcessor() {
    super(Set.of(ElementKind.INTERFACE, ElementKind.CLASS), Sample.class, JaquariusArtifactProcessor.SOURCE_VERSION);
  }

  public CustomType getAnnotatedType() {
    return annotatedType;
  }

  @Override
  public boolean isApplicable(CustomType annotatedType) {
    this.annotatedType = annotatedType;
    return false;
  }

  @Override
  public ArtifactValidator validator() {
    return null;
  }

  @Override
  public List<ArtifactGenerator> makeGenerators(CustomType source, ArtifactGeneratorContext context) {
    return List.of();
  }
}
