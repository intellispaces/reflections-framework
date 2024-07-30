package tech.intellispaces.core.validation;

import tech.intellispaces.annotations.validator.AnnotatedTypeValidator;
import tech.intellispaces.core.annotation.Data;
import tech.intellispaces.core.annotation.Domain;
import tech.intellispaces.core.exception.IntelliSpacesException;
import tech.intellispaces.javastatements.customtype.CustomType;

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
