package tech.intellispaces.framework.core.space.domain;

import tech.intellispaces.framework.core.annotation.Domain;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReference;

/**
 * Domain functions.
 */
public interface DomainFunctions {

  static boolean isDomainClass(Class<?> aClass) {
    return aClass.isAnnotationPresent(Domain.class);
  }

  static boolean isDomainType(TypeReference type) {
    return type.isCustomTypeReference() && type.asCustomTypeReferenceSurely().targetType().hasAnnotation(Domain.class);
  }
}
