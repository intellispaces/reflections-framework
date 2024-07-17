package tech.intellispaces.framework.core.annotation.processor;

import tech.intellispaces.framework.annotationprocessor.AnnotatedTypeProcessor;
import tech.intellispaces.framework.commons.collection.ArraysFunctions;
import tech.intellispaces.framework.core.annotation.Preprocessing;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

import javax.lang.model.element.ElementKind;
import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractAnnotationProcessor extends AnnotatedTypeProcessor {

  public AbstractAnnotationProcessor(Class<? extends Annotation> annotation, Set<ElementKind> applicableKinds) {
    super(annotation, applicableKinds);
  }

  protected boolean isAutoGenerationEnabled(CustomType annotatedType) {
    return annotatedType.selectAnnotation(Preprocessing.class)
        .map(Preprocessing::enable).
        orElse(true);
  }

  protected boolean isAutoGenerationEnabled(CustomType annotatedType, String artifact) {
    Optional<Preprocessing> annotation = annotatedType.selectAnnotation(Preprocessing.class);
    if (annotation.isEmpty()) {
      return true;
    }
    if (!isProcessingAnnotationContainsAnnotatedType(annotatedType, annotation.get())) {
      return false;
    }
    if (!annotation.get().enable()) {
      return false;
    }
    return !ArraysFunctions.contains(annotation.get().excludedArtefacts(), artifact);
  }

  private boolean isProcessingAnnotationContainsAnnotatedType(CustomType annotatedType, Preprocessing annotation) {
    if (annotation.value().length == 0) {
      return true;
    }
    return ArraysFunctions.contains(annotation.value(), Class::getCanonicalName, annotatedType.canonicalName());
  }
}
