package tech.intellispaces.reflections.framework.annotationprocessor.domain;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

import tech.intellispaces.annotationprocessor.ArtifactValidator;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.method.MethodFunctions;
import tech.intellispaces.javareflection.method.MethodParam;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.javareflection.reference.CustomTypeReference;
import tech.intellispaces.javareflection.reference.NotPrimitiveReference;
import tech.intellispaces.javareflection.reference.ReferenceBound;
import tech.intellispaces.javareflection.reference.TypeReference;
import tech.intellispaces.reflections.framework.annotation.Channel;
import tech.intellispaces.reflections.framework.annotation.Domain;
import tech.intellispaces.reflections.framework.annotation.Ontology;
import tech.intellispaces.reflections.framework.exception.ReflectionsExceptions;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.space.domain.DomainFunctions;

/**
 * The domain type validator.
 */
public class DomainValidator implements ArtifactValidator {

  @Override
  public void validate(CustomType domainType) {
    validateName(domainType);
    validateEnclosingType(domainType);
    validateSuperDomains(domainType);
    validateMethods(domainType);
  }

  private void validateName(CustomType domainType) {
    if (!domainType.simpleName().endsWith("Domain")) {
      throw ReflectionsExceptions.withMessage("Domain interface name must end with 'Domain'. Check class {0}\"",
          domainType.canonicalName());
    }
  }

  private void validateEnclosingType(CustomType domainType) {
    Optional<CustomType> enclosingType = domainType.enclosingType();
    if (enclosingType.isPresent() && !enclosingType.get().hasAnnotation(Ontology.class)) {
      throw ReflectionsExceptions.withMessage("Domain interface can only be nested to ontology interface. " +
          "Check class {0}", domainType.canonicalName());
    }
  }

  private void validateSuperDomains(CustomType domainType) {
    boolean isAlias = false;
    for (CustomTypeReference superDomain : domainType.parentTypes()) {
      if (isAlias) {
        throw ReflectionsExceptions.withMessage("Alias domain must have a single base domain. Check class {0}",
            domainType.canonicalName());
      }
      if (isAliasDomain(superDomain)) {
        isAlias = true;
        if (domainType.declaredMethod(NameConventionFunctions.getConversionMethodName(superDomain), List.of()).isPresent()) {
          throw ReflectionsExceptions.withMessage("Alias domain could not contain channel to base domain. See domain {0}",
              domainType.canonicalName());
        }
      }
    }
  }

  private boolean isAliasDomain(CustomTypeReference superDomain) {
    for (NotPrimitiveReference typeArgument : superDomain.typeArguments()) {
      if (typeArgument.isCustomTypeReference()) {
        return true;
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

  private void validateMethods(CustomType domainType) {
    domainType.declaredMethods().forEach(method -> validateMethod(method, domainType));
  }

  private void validateMethod(MethodStatement method, CustomType domainType) {
    if (!method.isPublic()) {
      throw ReflectionsExceptions.withMessage("Domain class could not contain private methods. " +
          "Check method '{0}' in class {1}", method.name(), domainType.canonicalName());
    }
    if (!method.hasAnnotation(Channel.class)) {
      throw ReflectionsExceptions.withMessage("Domain class methods should be marked with annotation @{0}. " +
              "Check method '{1}' in class {2}",
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
    if (StringFunctions.isNotBlank(name) && !name.endsWith("Channel")) {
      throw ReflectionsExceptions.withMessage("Channel method name must end with 'Channel'. " +
              "Check method {0} in class {1}",  method.name(), domainType.canonicalName());
    }
  }

  private void validateMethodReturnType(MethodStatement method, CustomType domainType) {
    Optional<TypeReference> returnType = method.returnType();
    if (returnType.isEmpty()) {
      throw ReflectionsExceptions.withMessage("Domain methods must return a value. " +
          "Check method '{0}' in class 12}", method.name(), domainType.canonicalName());
    }
    if (returnType.get().isArrayReference()) {
      throw ReflectionsExceptions.withMessage("Domain methods cannot return an array. " +
          "Check method '{0}' in class {1}", method.name(), domainType.canonicalName());
    }
    if (returnType.get().isCustomTypeReference()) {
      CustomTypeReference customTypeReference = returnType.get().asCustomTypeReferenceOrElseThrow();
      CustomType customType = customTypeReference.targetType();
      if (ClassFunctions.isPrimitiveWrapperClass(customType.canonicalName())) {
        boolean isGeneralReturnType = MethodFunctions.getOverrideMethods(method).stream()
            .anyMatch(m -> m.returnType().orElseThrow().isNamedReference());
        if (!isGeneralReturnType) {
          throw ReflectionsExceptions.withMessage("Domain methods must return primitive type. " +
              "Check method '{0}' in class {1}", method.name(), domainType.canonicalName());
        }
      }
      if (UNACCEPTABLE_TYPES.contains(customType.canonicalName())) {
        throw ReflectionsExceptions.withMessage("Domain method returns unacceptable type. " +
            "Check method '{0}' in class {1}", method.name(), domainType.canonicalName());
      }
    }
  }

  private static void validateConversionMethod(MethodStatement method, CustomType domainType) {
    CustomTypeReference returnType = method.returnType().orElseThrow().asCustomTypeReferenceOrElseThrow();
    if (DomainFunctions.isAliasOf(returnType, domainType)) {
      throw ReflectionsExceptions.withMessage("Domain conversion method should not return equivalent domain. " +
          "Check method '{0}' in class {1}", method.name(), domainType.canonicalName());
    }

    if (returnType.typeArguments().isEmpty()) {
      String expectedMethodName = NameConventionFunctions.getConversionMethodName(returnType);
      if (!method.name().equals(expectedMethodName)) {
        throw ReflectionsExceptions.withMessage("Invalid name of the domain conversion method. " +
            "Check method '{0}' in class {1}. Expected name '{2}'",
            method.name(), domainType.canonicalName(), expectedMethodName);
      }
    }
  }

  private void validateMethodParameters(MethodStatement method, CustomType domainType) {
    method.params().forEach(p -> validateMethodParameter(p, method, domainType));
  }

  private void validateMethodParameter(MethodParam param, MethodStatement method, CustomType domainType) {
    if (param.type().isArrayReference()) {
      throw ReflectionsExceptions.withMessage("It is not allowed to use array type in channel qualifiers. " +
          "Check method '{0}' in class {1}", method.name(), domainType.canonicalName());
    }
    if (param.type().isCustomTypeReference()) {
      CustomTypeReference customTypeReference = param.type().asCustomTypeReferenceOrElseThrow();
      CustomType customType = customTypeReference.targetType();
      if (UNACCEPTABLE_TYPES.contains(customType.canonicalName())) {
        throw ReflectionsExceptions.withMessage("Channel method qualifier has unacceptable type. " +
            "Check parameter '{0}' in method '{1}' in class {2}",
            param.name(), method.name(), domainType.canonicalName());
      }
      if (customType.hasAnnotation(Domain.class) && DomainFunctions.hasEquivalentDomains(customType)) {
        throw ReflectionsExceptions.withMessage("It is not allowed to use domain alias in channel method qualifier. " +
                "Check parameter '{0}' in method '{1}' in class {2}",
            param.name(), method.name(), domainType.canonicalName());
      }
    }
  }

  private static final List<String> UNACCEPTABLE_TYPES = List.of(
      Optional.class.getCanonicalName(),
      Future.class.getCanonicalName()
  );
}
