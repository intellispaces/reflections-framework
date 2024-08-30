package intellispaces.core.validation;

import intellispaces.annotations.validator.AnnotatedTypeValidator;
import intellispaces.core.annotation.Transition;
import intellispaces.core.exception.IntelliSpacesException;
import intellispaces.core.object.ObjectFunctions;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.method.MethodStatement;

/**
 * Domain type validator.
 */
public class DomainValidator implements AnnotatedTypeValidator {

  @Override
  public void validate(CustomType domainType) {
    for (MethodStatement method : domainType.declaredMethods()) {
      if (method.isDefault() && !ObjectFunctions.isDefaultObjectHandleType(method.returnType().orElseThrow())) {
        throw IntelliSpacesException.withMessage("Domain class can only contain default methods " +
            "that return the default object handle type. See method ''{0}'' in class {1}",
            method.name(), domainType.canonicalName());
      }
      if (!method.isPublic()) {
        throw IntelliSpacesException.withMessage("Domain class could not contain private methods." +
            "But method ''{0}'' in class {1} is private", method.name(), domainType.canonicalName());
      }
      if (!method.hasAnnotation(Transition.class) &&
          method.overrideMethods().stream().noneMatch(m -> m.hasAnnotation(Transition.class))
      ) {
        throw IntelliSpacesException.withMessage("Domain class methods should be marked with annotation @{0}. " +
                "But method ''{1}'' in class {2} doesn't marked",
            Transition.class.getSimpleName(), method.name(), domainType.canonicalName());
      }
    }
  }
}
