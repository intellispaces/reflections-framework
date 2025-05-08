package tech.intellispaces.reflections.annotationprocessor.dataset;

import java.util.Optional;

import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.reflections.annotation.Dataset;
import tech.intellispaces.reflections.annotation.Domain;
import tech.intellispaces.reflections.annotation.Ontology;
import tech.intellispaces.reflections.exception.JaquariusExceptions;
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
      throw JaquariusExceptions.withMessage(
          "Annotation {0} should only be applied to types with the annotation {1}",
          Dataset.class.getSimpleName(), Domain.class.getSimpleName());
    }
  }

  private void validateEnclosingType(CustomType dataType) {
    Optional<CustomType> enclosingType = dataType.enclosingType();
    if (enclosingType.isPresent() && !enclosingType.get().hasAnnotation(Ontology.class)) {
      throw JaquariusExceptions.withMessage("Data domain interface can only be nested to ontology interface. " +
          "Check class {0}", dataType.canonicalName());
    }
  }
}
