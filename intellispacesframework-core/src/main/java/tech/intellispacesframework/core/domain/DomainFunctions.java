package tech.intellispacesframework.core.domain;

import tech.intellispacesframework.core.annotation.Domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Domain functions.
 */
public interface DomainFunctions {

  static boolean isDomainClass(Class<?> aClass) {
    return aClass.isAnnotationPresent(Domain.class);
  }

  static Class<?> getDomainClassOfObjectHandle(Class<?> objectHandleClass) {
    Class<?> domainClass = getDomainClassRecursive(objectHandleClass, new HashSet<>());
    if (domainClass == null) {
      throw new RuntimeException();
    }
    return domainClass;
  }

  private static Class<?> getDomainClassRecursive(Class<?> aClass, Set<Class<?>> history) {
    if (history.contains(aClass)) {
      return null;
    }
    history.add(aClass);

    for (Class<?> interfaceClass : aClass.getInterfaces()) {
      if (interfaceClass.isAnnotationPresent(Domain.class)) {
        return interfaceClass;
      }
    }

    for (Class<?> interfaceClass : aClass.getInterfaces()) {
      Class<?> result = getDomainClassRecursive(interfaceClass, history);
      if (result != null) {
        return result;
      }
    }

    Class<?> superclass = aClass.getSuperclass();
    if (superclass != null) {
      return getDomainClassRecursive(superclass, history);
    }
    return null;
  }
}
