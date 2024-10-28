package intellispaces.framework.core.validation;

import intellispaces.common.annotationprocessor.validator.AnnotatedTypeValidator;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.framework.core.annotation.Data;
import intellispaces.framework.core.annotation.Domain;
import intellispaces.framework.core.annotation.Ontology;
import intellispaces.framework.core.exception.IntelliSpacesException;

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
      throw IntelliSpacesException.withMessage(
          "Annotation {0} should only be applied to types with the annotation {1}",
          Data.class.getSimpleName(), Domain.class.getSimpleName());
    }
  }

  private void validateEnclosingType(CustomType dataType) {
    Optional<CustomType> enclosingType = dataType.enclosingType();
    if (enclosingType.isPresent() && !enclosingType.get().hasAnnotation(Ontology.class)) {
      throw IntelliSpacesException.withMessage("Data domain interface can only be nested to ontology interface. " +
          "Check class {0}", dataType.canonicalName());
    }
  }
}
