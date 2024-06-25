package tech.intellispaces.framework.core.annotation.validator;

import tech.intellispaces.framework.annotationprocessor.AnnotatedTypeValidator;
import tech.intellispaces.framework.core.annotation.Transition;
import tech.intellispaces.framework.core.exception.IntelliSpacesException;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.custom.MethodStatement;

/**
 * Domain type validator.
 */
public class DomainValidator implements AnnotatedTypeValidator {

  @Override
  public void validate(CustomType domainType) {
    for (MethodStatement method : domainType.declaredMethods()) {
      if (method.isDefault()) {
        throw IntelliSpacesException.withMessage("Domain class can't contain default methods." +
                "But method {} in class {} is default", method.name(), domainType.canonicalName());
      }
      if (!method.isPublic()) {
        throw IntelliSpacesException.withMessage("Domain class can't contain private methods." +
            "But method {} in class {} is private", method.name(), domainType.canonicalName());
      }
      if (!method.hasAnnotation(Transition.class)) {
        throw IntelliSpacesException.withMessage("Domain class methods should be marked with annotation @{}. " +
                "But method {} in class {} doesn't marked",
            Transition.class.getSimpleName(), method.name(), domainType.canonicalName());
      }
    }
  }
}
