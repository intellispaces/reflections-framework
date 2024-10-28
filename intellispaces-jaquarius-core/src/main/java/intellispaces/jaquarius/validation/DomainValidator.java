package intellispaces.jaquarius.validation;

import intellispaces.common.annotationprocessor.validator.AnnotatedTypeValidator;
import intellispaces.common.base.text.TextFunctions;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.CustomTypeReference;
import intellispaces.common.javastatement.reference.NotPrimitiveReference;
import intellispaces.common.javastatement.reference.ReferenceBound;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.jaquarius.annotation.Channel;
import intellispaces.jaquarius.annotation.Domain;
import intellispaces.jaquarius.annotation.Ontology;
import intellispaces.jaquarius.common.NameConventionFunctions;
import intellispaces.jaquarius.exception.IntelliSpacesException;
import intellispaces.jaquarius.space.domain.DomainFunctions;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Domain type validator.
 */
public class DomainValidator implements AnnotatedTypeValidator {

  @Override
  public void validate(CustomType domainType) {
    validateName(domainType);
    validateEnclosingType(domainType);
    validateParentDomains(domainType);
    validateMethods(domainType);
  }

  private void validateName(CustomType domainType) {
    if (!domainType.simpleName().endsWith("Domain")) {
      throw IntelliSpacesException.withMessage("Domain interface name must end with ''Domain''. Check class {0}\"",
          domainType.canonicalName());
    }
  }

  private void validateEnclosingType(CustomType domainType) {
    Optional<CustomType> enclosingType = domainType.enclosingType();
    if (enclosingType.isPresent() && !enclosingType.get().hasAnnotation(Ontology.class)) {
      throw IntelliSpacesException.withMessage("Domain interface can only be nested to ontology interface. " +
          "Check class {0}", domainType.canonicalName());
    }
  }

  private void validateParentDomains(CustomType domainType) {
    boolean isAlias = false;
    for (CustomTypeReference parentDomain : domainType.parentTypes()) {
      if (isAlias) {
        throw IntelliSpacesException.withMessage("Alias domain must have a single base domain. Check class {0}",
            domainType.canonicalName());
      }
      if (isAliasDomain(parentDomain)) {
        isAlias = true;
        if (domainType.declaredMethod(NameConventionFunctions.getConversionMethodName(parentDomain), List.of()).isPresent()) {
          throw IntelliSpacesException.withMessage("Alias domain could not contain channel to base domain. See domain {0}",
              domainType.canonicalName());
        }
      } else {
        if (domainType.declaredMethod(NameConventionFunctions.getConversionMethodName(parentDomain), List.of()).isEmpty()) {
          throw IntelliSpacesException.withMessage("Could not find conversion channel to domain {0}. See domain {1}",
              parentDomain.targetType().canonicalName(), domainType.canonicalName());
        }
      }
      validateParentDomain(parentDomain, domainType);
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

  private void validateParentDomain(CustomTypeReference baseDomain, CustomType domainType) {
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
    validateMethodName(method, domainType);
    validateMethodReturnType(method, domainType);
    validateMethodParameters(method, domainType);
    if (NameConventionFunctions.isConversionMethod(method)) {
      validateConversionMethod(method, domainType);
    }
  }

  private void validateMethodName(MethodStatement method, CustomType domainType) {
    String name = method.selectAnnotation(Channel.class).orElseThrow().name();
    if (TextFunctions.isNotBlank(name) && !name.endsWith("Channel")) {
      throw IntelliSpacesException.withMessage("Channel method name must end with ''Channel''. " +
              "Check method {0} in class {1}",  method.name(), domainType.canonicalName());
    }
  }

  private void validateMethodReturnType(MethodStatement method, CustomType domainType) {
    Optional<TypeReference> returnType = method.returnType();
    if (returnType.isEmpty()) {
      throw IntelliSpacesException.withMessage("Domain methods must return a value. " +
          "Check method ''{0}'' in class 12}", method.name(), domainType.canonicalName());
    }
    if (returnType.get().isPrimitiveReference()) {
      throw IntelliSpacesException.withMessage("Domain method cannot return a primitive value. " +
          "Check method ''{0}'' in class {1}", method.name(), domainType.canonicalName());
    }
    if (returnType.get().isArrayReference()) {
      throw IntelliSpacesException.withMessage("Domain methods cannot return an array. " +
          "Check method ''{0}'' in class {1}", method.name(), domainType.canonicalName());
    }
    if (returnType.get().isCustomTypeReference()) {
      CustomTypeReference customTypeReference = returnType.get().asCustomTypeReferenceOrElseThrow();
      CustomType customType = customTypeReference.targetType();
      if (UNACCEPTABLE_TYPES.contains(customType.canonicalName())) {
        throw IntelliSpacesException.withMessage("Domain method returns unacceptable type. " +
            "Check method ''{0}'' in class {1}", method.name(), domainType.canonicalName());
      }
    }
  }

  private static void validateConversionMethod(MethodStatement method, CustomType domainType) {
    CustomTypeReference returnType = method.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow();
    if (DomainFunctions.isAliasOf(returnType, domainType)) {
      throw IntelliSpacesException.withMessage("Domain conversion method should not return equivalent domain. " +
          "Check method ''{0}'' in class {1}", method.name(), domainType.canonicalName());
    }

    if (returnType.typeArguments().isEmpty()) {
      String expectedMethodName = NameConventionFunctions.getConversionMethodName(returnType);
      if (!method.name().equals(expectedMethodName)) {
        throw IntelliSpacesException.withMessage("Invalid name of the domain conversion method. " +
            "Check method ''{0}'' in class {1}. Expected name ''{2}''",
            method.name(), domainType.canonicalName(), expectedMethodName);
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
