package tech.intellispaces.framework.core.space.domain;

import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;
import tech.intellispaces.framework.commons.type.TypeFunctions;
import tech.intellispaces.framework.core.annotation.Domain;
import tech.intellispaces.framework.javastatements.JavaStatements;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReference;

import java.util.Set;

/**
 * Domain functions.
 */
public final class DomainFunctions {

  public static boolean isDomainClass(Class<?> aClass) {
    return aClass.isAnnotationPresent(Domain.class);
  }

  public static boolean isDomainType(TypeReference type) {
    return type.isCustomTypeReference() && type.asCustomTypeReferenceSurely().targetType().hasAnnotation(Domain.class);
  }

  public static CustomType getDomainType(TypeReference type) {
    if (type.isPrimitive()) {
      Class<?> wrapperClass = TypeFunctions.getPrimitiveWrapperClass(type.asPrimitiveTypeReferenceSurely().typename());
      return JavaStatements.customTypeStatement(wrapperClass);
    } else if (type.isCustomTypeReference()) {
      CustomType domainType = type.asCustomTypeReferenceSurely().targetType();
      if (!isDefaultDomainType(domainType) && !domainType.hasAnnotation(Domain.class)) {
        throw UnexpectedViolationException.withMessage("Unexpected type reference. Class {} is not domain class",
            domainType.canonicalName());
      }
      return domainType;
    } else {
      throw UnexpectedViolationException.withMessage("Unexpected type reference: {}", type);
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
