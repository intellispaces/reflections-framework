package tech.intellispaces.core.space.domain;

import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.commons.type.TypeFunctions;
import tech.intellispaces.core.annotation.Domain;
import tech.intellispaces.javastatements.JavaStatements;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.reference.CustomTypeReference;
import tech.intellispaces.javastatements.reference.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Domain functions.
 */
public final class DomainFunctions {

  public static boolean isDomainClass(Class<?> aClass) {
    return aClass.isAnnotationPresent(Domain.class);
  }

  public static boolean isDomainType(TypeReference type) {
    return type.isCustomTypeReference() && type.asCustomTypeReferenceOrElseThrow().targetType().hasAnnotation(Domain.class);
  }

  public static boolean isDomainType(CustomType type) {
    return type.hasAnnotation(Domain.class);
  }

  public static CustomType getDomainType(TypeReference type) {
    if (type.isPrimitiveReference()) {
      Class<?> wrapperClass = TypeFunctions.getPrimitiveWrapperClass(type.asPrimitiveReferenceOrElseThrow().typename());
      return JavaStatements.customTypeStatement(wrapperClass);
    } else if (type.isCustomTypeReference()) {
      CustomType domainType = type.asCustomTypeReferenceOrElseThrow().targetType();
      if (!isDefaultDomainType(domainType) && !domainType.hasAnnotation(Domain.class)) {
        throw UnexpectedViolationException.withMessage("Unexpected type reference. Class {} is not domain class",
            domainType.canonicalName());
      }
      return domainType;
    } else {
      throw UnexpectedViolationException.withMessage("Unexpected type reference: {}", type);
    }
  }

  public static List<CustomType> getDomainTypeAndParents(CustomType domainType) {
    List<CustomType> result = new ArrayList<>();
    result.add(domainType);
    getParentDomains(domainType, result::add);
    return result;
  }

  public static List<CustomType> getParentDomains(CustomType domainType) {
    List<CustomType> parents = new ArrayList<>();
    getParentDomains(domainType, parents::add);
    return parents;
  }

  private static void getParentDomains(CustomType domainType, Consumer<CustomType> consumer) {
    for (CustomTypeReference parent : domainType.parentTypes()) {
      if (DomainFunctions.isDomainType(domainType)) {
        consumer.accept(parent.targetType());
        getParentDomains(parent.targetType(), consumer);
      }
    }
  }

  public static boolean isDefaultDomainType(CustomType type) {
    return DEFAULT_DOMAIN_CLASSES.contains(type.canonicalName());
  }

  private DomainFunctions() {}

  private final static Set<String> DEFAULT_DOMAIN_CLASSES = Set.of(
      Object.class.getCanonicalName(),
      Boolean.class.getCanonicalName(),
      Number.class.getCanonicalName(),
      Byte.class.getCanonicalName(),
      Short.class.getCanonicalName(),
      Integer.class.getCanonicalName(),
      Long.class.getCanonicalName(),
      Float.class.getCanonicalName(),
      Double.class.getCanonicalName(),
      Character.class.getCanonicalName(),
      String.class.getCanonicalName(),
      Class.class.getCanonicalName(),
      Void.class.getCanonicalName()
  );
}
