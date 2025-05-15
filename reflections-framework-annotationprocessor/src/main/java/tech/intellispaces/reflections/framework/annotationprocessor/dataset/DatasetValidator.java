package tech.intellispaces.reflections.framework.annotationprocessor.dataset;

import java.util.Optional;

import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.reflections.framework.annotation.Dataset;
import tech.intellispaces.reflections.framework.annotation.Domain;
import tech.intellispaces.reflections.framework.annotation.Ontology;
import tech.intellispaces.reflections.framework.exception.ReflectionsExceptions;
import tech.intellispaces.jstatements.customtype.CustomType;

/**
 * The dataset type validator.
 */
public class DatasetValidator implements ArtifactValidator {

  @Override
  public void validate(CustomType dataType) {
    validateEnclosingType(dataType);
    validateDataAnnotation(dataType);
  }

  private static void validateDataAnnotation(CustomType dataType) {
    if (!dataType.hasAnnotation(Domain.class)) {
      throw ReflectionsExceptions.withMessage(
          "Annotation {0} should only be applied to types with the annotation {1}",
          Dataset.class.getSimpleName(), Domain.class.getSimpleName());
    }
  }

  private void validateEnclosingType(CustomType dataType) {
    Optional<CustomType> enclosingType = dataType.enclosingType();
    if (enclosingType.isPresent() && !enclosingType.get().hasAnnotation(Ontology.class)) {
      throw ReflectionsExceptions.withMessage("Data domain interface can only be nested to ontology interface. " +
          "Check class {0}", dataType.canonicalName());
    }
  }
}
