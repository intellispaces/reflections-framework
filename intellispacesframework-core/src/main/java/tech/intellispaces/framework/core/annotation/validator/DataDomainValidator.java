package tech.intellispaces.framework.core.annotation.validator;

import tech.intellispaces.framework.annotationprocessor.AnnotatedTypeValidator;
import tech.intellispaces.framework.core.annotation.Data;
import tech.intellispaces.framework.core.annotation.Domain;
import tech.intellispaces.framework.core.exception.IntelliSpacesException;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;

/**
 * Data type validator.
 */
public class DataDomainValidator implements AnnotatedTypeValidator {

  @Override
  public void validate(CustomType dataType) {
    if (!dataType.hasAnnotation(Domain.class)) {
      throw IntelliSpacesException.withMessage("Annotation {} should only be used in conjunction with the annotation {}",
          Data.class.getSimpleName(), Domain.class.getSimpleName());
    }
  }
}
