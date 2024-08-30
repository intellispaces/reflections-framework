package intellispaces.core.annotation.validator;

import intellispaces.annotations.AnnotatedTypeProcessor;
import intellispaces.annotations.generator.GenerationTask;
import intellispaces.annotations.validator.AnnotatedTypeValidator;
import intellispaces.javastatements.customtype.CustomType;

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
  public List<GenerationTask> makeTasks(
      CustomType initiatorType, CustomType annotatedType, RoundEnvironment roundEnv
  ) {
    return List.of();
  }
}
