package tech.intellispaces.framework.core.space.domain;

import tech.intellispaces.framework.core.annotation.Domain;

/**
 * Domain functions.
 */
public interface DomainFunctions {

  static boolean isDomainClass(Class<?> aClass) {
    return aClass.isAnnotationPresent(Domain.class);
  }
}
