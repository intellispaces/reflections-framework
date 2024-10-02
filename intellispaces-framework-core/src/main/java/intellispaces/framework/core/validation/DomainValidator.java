package intellispaces.framework.core.validation;

import intellispaces.common.annotationprocessor.validator.AnnotatedTypeValidator;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.CustomTypeReference;
import intellispaces.common.javastatement.reference.NotPrimitiveReference;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.framework.core.annotation.Channel;
import intellispaces.framework.core.common.NameConventionFunctions;
import intellispaces.framework.core.exception.IntelliSpacesException;
import intellispaces.framework.core.space.domain.DomainFunctions;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Domain type validator.
 */
public class DomainValidator implements AnnotatedTypeValidator {

  @Override
  public void validate(CustomType domainType) {
    validateBaseDomains(domainType);
    validateMethods(domainType);
  }

  private void validateBaseDomains(CustomType domainType) {
    domainType.parentTypes().forEach(baseDomain -> validateBaseDomain(domainType, baseDomain));
  }

  private void validateBaseDomain(CustomType domainType, CustomTypeReference baseDomain) {
    for (NotPrimitiveReference typeArgument : baseDomain.typeArguments()) {
      if (typeArgument.isCustomTypeReference()) {
        CustomType argumentType = typeArgument.asCustomTypeReferenceOrElseThrow().targetType();
        if (!argumentType.isFinal()) {
          throw IntelliSpacesException.withMessage("The value of a base domain type variable should be " +
              "a non-final class. See domain {0}, base domain {1} has type argument {2}",
              domainType.canonicalName(), baseDomain.targetType().simpleName(), argumentType.simpleName());
        }
      }
    }
  }

  private void validateMethods(CustomType domainType) {
    List<CustomTypeReference> primaryDomains = DomainFunctions.getAllPrimaryDomainOfAlias(domainType);
    List<String> primaryDomainDeclarations = primaryDomains.stream()
            .map(CustomTypeReference::actualDeclaration)
                .toList();
    domainType.declaredMethods().forEach(method -> validateMethod(domainType, method, primaryDomainDeclarations));
  }

  private void validateMethod(
      CustomType domainType, MethodStatement method, List<String> primaryDomainDeclarations
  ) {
    if (!method.isPublic()) {
      throw IntelliSpacesException.withMessage("Domain class could not contain private methods. " +
          "But method ''{0}'' in class {1} is private", method.name(), domainType.canonicalName());
    }
    if (!method.hasAnnotation(Channel.class)) {
      throw IntelliSpacesException.withMessage("Domain class methods should be marked with annotation @{0}. " +
              "But method ''{1}'' in class {2} doesn't marked",
          Channel.class.getSimpleName(), method.name(), domainType.canonicalName());
    }
    validateMethodReturnType(domainType, method, method.returnType(), primaryDomainDeclarations);
  }

  private void validateMethodReturnType(
      CustomType domainType,
      MethodStatement method,
      Optional<TypeReference> returnType,
      List<String> primaryDomainDeclarations
  ) {
    if (returnType.isEmpty()) {
      throw IntelliSpacesException.withMessage("Domain methods must return a value. " +
          "But method ''{0}'' in class 12} doesn't return value", method.name(), domainType.canonicalName());
    }
    if (returnType.get().isPrimitiveReference()) {
      throw IntelliSpacesException.withMessage("Domain methods should not return a primitive value. " +
          "But method ''{0}'' in class {1} returns primitive value", method.name(), domainType.canonicalName());
    }
    if (returnType.get().isArrayReference()) {
      throw IntelliSpacesException.withMessage("Domain methods should not return an array. " +
          "But method ''{0}'' in class {1} returns array", method.name(), domainType.canonicalName());
    }
    if (returnType.get().isCustomTypeReference()) {
      CustomTypeReference customTypeReference = returnType.get().asCustomTypeReferenceOrElseThrow();
      CustomType customType = customTypeReference.targetType();
      if (INVALID_RETURN_TYPES.contains(customType.canonicalName())) {
        throw IntelliSpacesException.withMessage("Domain methods should not return an array. " +
            "Domain method ''{0}'' in class {1} returns disabled type", method.name(), domainType.canonicalName());
      }
      if (
          NameConventionFunctions.isConversionMethod(method)
              && primaryDomainDeclarations.contains(customTypeReference.actualDeclaration())
      ) {
        throw IntelliSpacesException.withMessage("Domain conversion method should not return equivalent domains. " +
            "But method ''{0}'' in class {1} returns equivalent domain", method.name(), domainType.canonicalName());
      }
    }
  }

  private static final List<String> INVALID_RETURN_TYPES = List.of(
      Optional.class.getCanonicalName(),
      Future.class.getCanonicalName()
  );
}
