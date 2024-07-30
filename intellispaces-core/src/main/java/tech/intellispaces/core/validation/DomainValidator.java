package tech.intellispaces.core.validation;

import tech.intellispaces.annotations.validator.AnnotatedTypeValidator;
import tech.intellispaces.core.annotation.Transition;
import tech.intellispaces.core.exception.IntelliSpacesException;
import tech.intellispaces.core.object.ObjectFunctions;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.method.MethodStatement;

/**
 * Domain type validator.
 */
public class DomainValidator implements AnnotatedTypeValidator {

  @Override
  public void validate(CustomType domainType) {
    for (MethodStatement method : domainType.declaredMethods()) {
      if (method.isDefault() && !ObjectFunctions.isDefaultObjectHandleType(method.returnType().orElseThrow())) {
        throw IntelliSpacesException.withMessage("Domain class can only contain default methods " +
            "that return the default object handle type. See method '{}' in class {}",
            method.name(), domainType.canonicalName());
      }
      if (!method.isPublic()) {
        throw IntelliSpacesException.withMessage("Domain class can't contain private methods." +
            "But method '{}' in class {} is private", method.name(), domainType.canonicalName());
      }
      if (!method.hasAnnotation(Transition.class) &&
          method.overrideMethods().stream().noneMatch(m -> m.hasAnnotation(Transition.class))
      ) {
        throw IntelliSpacesException.withMessage("Domain class methods should be marked with annotation @{}. " +
                "But method '{}' in class {} doesn't marked",
            Transition.class.getSimpleName(), method.name(), domainType.canonicalName());
      }
    }
  }
}
