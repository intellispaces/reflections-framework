package intellispaces.framework.core.validation;

import intellispaces.common.annotationprocessor.validator.AnnotatedTypeValidator;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.CustomTypeReference;
import intellispaces.common.javastatement.reference.NotPrimitiveReference;
import intellispaces.common.javastatement.reference.ReferenceBound;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.framework.core.annotation.Channel;
import intellispaces.framework.core.annotation.Domain;
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
    boolean isAlias = false;
    for (CustomTypeReference baseDomain : domainType.parentTypes()) {
      if (isAlias) {
        throw IntelliSpacesException.withMessage("Alias domain must have a single base domain. Check class {0}",
            domainType.canonicalName());
      }
      if (isAliasDomain(baseDomain)) {
        isAlias = true;
      }
      validateBaseDomain(baseDomain, domainType);
    }
  }

  private boolean isAliasDomain(CustomTypeReference baseDomain) {
    for (NotPrimitiveReference typeArgument : baseDomain.typeArguments()) {
      if (typeArgument.isCustomTypeReference()) {
        CustomType targetType = typeArgument.asCustomTypeReferenceOrElseThrow().targetType();
        return targetType.isFinal();
      }
      if (typeArgument.isNamedReference()) {
        List<ReferenceBound> extendedBounds = typeArgument.asNamedReferenceOrElseThrow().extendedBounds();
        if (!extendedBounds.isEmpty()) {
          return true;
        }
      }
    }
    return false;
  }

  private void validateBaseDomain(CustomTypeReference baseDomain, CustomType domainType) {
    for (NotPrimitiveReference typeArgument : baseDomain.typeArguments()) {
      if (typeArgument.isCustomTypeReference()) {
        CustomType argumentType = typeArgument.asCustomTypeReferenceOrElseThrow().targetType();
        if (!argumentType.isFinal()) {
          throw IntelliSpacesException.withMessage("The value of a base domain type variable should be " +
              "a non-final class. Check domain {0}. Base domain {1} has type argument {2}",
              domainType.canonicalName(), baseDomain.targetType().simpleName(), argumentType.simpleName());
        }
      }
    }
  }

  private void validateMethods(CustomType domainType) {
    domainType.declaredMethods().forEach(method -> validateMethod(method, domainType));
  }

  private void validateMethod(MethodStatement method, CustomType domainType) {
    if (!method.isPublic()) {
      throw IntelliSpacesException.withMessage("Domain class could not contain private methods. " +
          "Check method ''{0}'' in class {1}", method.name(), domainType.canonicalName());
    }
    if (!method.hasAnnotation(Channel.class)) {
      throw IntelliSpacesException.withMessage("Domain class methods should be marked with annotation @{0}. " +
              "Check method ''{1}'' in class {2}",
          Channel.class.getSimpleName(), method.name(), domainType.canonicalName());
    }
    validateMethodReturnType(method, domainType);
    validateMethodParameters(method, domainType);
  }

  private void validateMethodReturnType(MethodStatement method, CustomType domainType) {
    Optional<TypeReference> returnType = method.returnType();
    if (returnType.isEmpty()) {
      throw IntelliSpacesException.withMessage("Domain methods must return a value. " +
          "Check method ''{0}'' in class 12}", method.name(), domainType.canonicalName());
    }
    if (returnType.get().isPrimitiveReference()) {
      throw IntelliSpacesException.withMessage("Domain methods should not return a primitive value. " +
          "Check method ''{0}'' in class {1}", method.name(), domainType.canonicalName());
    }
    if (returnType.get().isArrayReference()) {
      throw IntelliSpacesException.withMessage("Domain methods should not return an array. " +
          "Check method ''{0}'' in class {1}", method.name(), domainType.canonicalName());
    }
    if (returnType.get().isCustomTypeReference()) {
      CustomTypeReference customTypeReference = returnType.get().asCustomTypeReferenceOrElseThrow();
      CustomType customType = customTypeReference.targetType();
      if (UNACCEPTABLE_TYPES.contains(customType.canonicalName())) {
        throw IntelliSpacesException.withMessage("Domain method returns unacceptable type. " +
            "Check method ''{0}'' in class {1}", method.name(), domainType.canonicalName());
      }
      if (NameConventionFunctions.isConversionMethod(method)) {
        if (DomainFunctions.isAliasOf(customTypeReference, domainType)) {
          throw IntelliSpacesException.withMessage("Domain conversion method should not return equivalent domain. " +
              "Check method ''{0}'' in class {1}", method.name(), domainType.canonicalName());
        }
      }
    }
  }

  private void validateMethodParameters(MethodStatement method, CustomType domainType) {
    method.params().forEach(p -> validateMethodParameter(p, method, domainType));
  }

  private void validateMethodParameter(MethodParam param, MethodStatement method, CustomType domainType) {
    if (param.type().isArrayReference()) {
      throw IntelliSpacesException.withMessage("It is not allowed to use array type in channel qualifiers. " +
          "Check method ''{0}'' in class {1}", method.name(), domainType.canonicalName());
    }
    if (param.type().isCustomTypeReference()) {
      CustomTypeReference customTypeReference = param.type().asCustomTypeReferenceOrElseThrow();
      if (TypeFunctions.isPrimitiveWrapperClass(customTypeReference.targetType().canonicalName())) {
        throw IntelliSpacesException.withMessage("It is not allowed to use primitive wrapper type in channel qualifiers. " +
            "Check parameter ''{0}'' in method ''{1}'' in class {2}",
            param.name(), method.name(), domainType.canonicalName());
      }
      CustomType customType = customTypeReference.targetType();
      if (UNACCEPTABLE_TYPES.contains(customType.canonicalName())) {
        throw IntelliSpacesException.withMessage("Channel method qualifier has unacceptable type. " +
            "Check parameter ''{0}'' in method ''{1}'' in class {2}",
            param.name(), method.name(), domainType.canonicalName());
      }
      if (customType.hasAnnotation(Domain.class) && DomainFunctions.hasEquivalentDomains(customType)) {
        throw IntelliSpacesException.withMessage("It is not allowed to use domain alias in channel method qualifier. " +
                "Check parameter ''{0}'' in method ''{1}'' in class {2}",
            param.name(), method.name(), domainType.canonicalName());
      }
    }
  }

  private static final List<String> UNACCEPTABLE_TYPES = List.of(
      Optional.class.getCanonicalName(),
      Future.class.getCanonicalName()
  );
}
