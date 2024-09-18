package intellispaces.framework.core.validation;

import intellispaces.common.annotationprocessor.validator.AnnotatedTypeValidator;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.framework.core.annotation.Data;
import intellispaces.framework.core.annotation.Domain;
import intellispaces.framework.core.exception.IntelliSpacesException;

/**
 * Data type validator.
 */
public class DataValidator implements AnnotatedTypeValidator {

  @Override
  public void validate(CustomType dataType) {
    if (!dataType.hasAnnotation(Domain.class)) {
      throw IntelliSpacesException.withMessage(
          "Annotation {0} should only be applied to types with the annotation {1}",
          Data.class.getSimpleName(), Domain.class.getSimpleName());
    }
  }
}
