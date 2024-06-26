package tech.intellispaces.framework.core.annotation.processor;

import tech.intellispaces.framework.annotationprocessor.AnnotatedTypeProcessor;
import tech.intellispaces.framework.core.annotation.AutoGeneration;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

import javax.lang.model.element.ElementKind;
import java.lang.annotation.Annotation;
import java.util.Set;

public abstract class AbstractAnnotationProcessor extends AnnotatedTypeProcessor {

  public AbstractAnnotationProcessor(Class<? extends Annotation> annotation, Set<ElementKind> applicableKinds) {
    super(annotation, applicableKinds);
  }

  protected boolean isAutoGenerationEnabled(CustomType annotatedType) {
    return isAutoGenerationEnabled(annotatedType, "");
  }

  protected boolean isAutoGenerationEnabled(CustomType annotatedType, String target) {
    return annotatedType.selectAnnotation(AutoGeneration.class)
        .filter(a -> target.equals(a.target()))
        .map(AutoGeneration::enabled)
        .orElse(true);
  }
}
