package tech.intellispaces.framework.core.validate;

import tech.intellispaces.framework.annotationprocessor.AnnotatedTypeProcessor;
import tech.intellispaces.framework.annotationprocessor.generator.ArtifactGenerator;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

import javax.lang.model.element.ElementKind;
import java.util.List;
import java.util.Set;

public class SampleAnnotatedTypeProcessor extends AnnotatedTypeProcessor {
  private CustomType annotatedType;

  public SampleAnnotatedTypeProcessor() {
    super(Sample.class, Set.of(ElementKind.INTERFACE, ElementKind.CLASS));
  }

  public CustomType getAnnotatedType() {
    return annotatedType;
  }

  @Override
  protected boolean isApplicable(CustomType annotatedType) {
    this.annotatedType = annotatedType;
    return false;
  }

  @Override
  protected List<ArtifactGenerator> makeArtifactGenerators(CustomType annotatedType) {
    return List.of();
  }
}
