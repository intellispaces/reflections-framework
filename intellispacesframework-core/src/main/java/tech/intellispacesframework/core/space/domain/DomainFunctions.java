package tech.intellispacesframework.core.space.domain;

import tech.intellispacesframework.core.annotation.Domain;

/**
 * Domain functions.
 */
public interface DomainFunctions {

  static boolean isDomainClass(Class<?> aClass) {
    return aClass.isAnnotationPresent(Domain.class);
  }
}
