package intellispaces.core.validation;

import intellispaces.annotations.validator.AnnotatedTypeValidator;
import intellispaces.core.annotation.Data;
import intellispaces.core.annotation.Domain;
import intellispaces.core.exception.IntelliSpacesException;
import intellispaces.javastatements.customtype.CustomType;

/**
 * Data type validator.
 */
public class DataValidator implements AnnotatedTypeValidator {

  @Override
  public void validate(CustomType dataType) {
    if (!dataType.hasAnnotation(Domain.class)) {
      throw IntelliSpacesException.withMessage(
          "Annotation {} should only be used in conjunction with the annotation {}",
          Data.class.getSimpleName(), Domain.class.getSimpleName());
    }
  }
}
