package tech.intellispaces.jaquarius.annotationprocessor.dataset;

import tech.intellispaces.commons.annotation.processor.ArtifactValidator;
import tech.intellispaces.commons.reflection.customtype.CustomType;
import tech.intellispaces.jaquarius.annotation.Dataset;
import tech.intellispaces.jaquarius.annotation.Domain;
import tech.intellispaces.jaquarius.annotation.Ontology;
import tech.intellispaces.jaquarius.exception.JaquariusExceptions;

import java.util.Optional;

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
