package tech.intellispaces.jaquarius.validation;

import tech.intellispaces.jaquarius.annotation.Data;
import tech.intellispaces.jaquarius.annotation.Domain;
import tech.intellispaces.jaquarius.annotation.Ontology;
import tech.intellispaces.jaquarius.exception.JaquariusExceptions;
import tech.intellispaces.java.annotation.validator.AnnotatedTypeValidator;
import tech.intellispaces.java.reflection.customtype.CustomType;

import java.util.Optional;

/**
 * Data type validator.
 */
public class DataValidator implements AnnotatedTypeValidator {

  @Override
  public void validate(CustomType dataType) {
    validateEnclosingType(dataType);
    validateDataAnnotation(dataType);
  }

  private static void validateDataAnnotation(CustomType dataType) {
    if (!dataType.hasAnnotation(Domain.class)) {
      throw JaquariusExceptions.withMessage(
          "Annotation {0} should only be applied to types with the annotation {1}",
          Data.class.getSimpleName(), Domain.class.getSimpleName());
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
