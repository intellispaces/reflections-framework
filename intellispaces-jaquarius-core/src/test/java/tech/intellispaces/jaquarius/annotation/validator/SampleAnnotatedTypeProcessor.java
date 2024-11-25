package tech.intellispaces.jaquarius.annotation.validator;

import tech.intellispaces.java.annotation.AnnotatedTypeProcessor;
import tech.intellispaces.java.annotation.generator.Generator;
import tech.intellispaces.java.annotation.validator.AnnotatedTypeValidator;
import tech.intellispaces.java.reflection.customtype.CustomType;

import javax.annotation.processing.RoundEnvironment;
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
  public boolean isApplicable(CustomType annotatedType) {
    this.annotatedType = annotatedType;
    return false;
  }

  @Override
  public AnnotatedTypeValidator getValidator() {
    return null;
  }

  @Override
  public List<Generator> makeGenerators(
      CustomType initiatorType, CustomType annotatedType, RoundEnvironment roundEnv
  ) {
    return List.of();
  }
}
