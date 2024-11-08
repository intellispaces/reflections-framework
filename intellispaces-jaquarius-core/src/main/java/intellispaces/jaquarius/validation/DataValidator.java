package intellispaces.jaquarius.validation;

import intellispaces.common.annotationprocessor.validator.AnnotatedTypeValidator;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.jaquarius.annotation.Data;
import intellispaces.jaquarius.annotation.Domain;
import intellispaces.jaquarius.annotation.Ontology;
import intellispaces.jaquarius.exception.IntelliSpacesExceptions;

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
      throw IntelliSpacesExceptions.withMessage(
          "Annotation {0} should only be applied to types with the annotation {1}",
          Data.class.getSimpleName(), Domain.class.getSimpleName());
    }
  }

  private void validateEnclosingType(CustomType dataType) {
    Optional<CustomType> enclosingType = dataType.enclosingType();
    if (enclosingType.isPresent() && !enclosingType.get().hasAnnotation(Ontology.class)) {
      throw IntelliSpacesExceptions.withMessage("Data domain interface can only be nested to ontology interface. " +
          "Check class {0}", dataType.canonicalName());
    }
  }
}
