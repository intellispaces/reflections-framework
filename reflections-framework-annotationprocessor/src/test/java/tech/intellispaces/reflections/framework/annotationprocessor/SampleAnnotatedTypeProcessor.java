package tech.intellispaces.reflections.framework.annotationprocessor;

import java.util.List;
import java.util.Set;
import javax.lang.model.element.ElementKind;

import tech.intellispaces.annotationprocessor.ArtifactGenerator;
import tech.intellispaces.annotationprocessor.ArtifactGeneratorContext;
import tech.intellispaces.annotationprocessor.ArtifactProcessor;
import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.jstatements.customtype.CustomType;

public class SampleAnnotatedTypeProcessor extends ArtifactProcessor {
  private CustomType annotatedType;

  public SampleAnnotatedTypeProcessor() {
    super(Set.of(ElementKind.INTERFACE, ElementKind.CLASS), Sample.class, ReflectionsArtifactProcessor.SOURCE_VERSION);
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
