package tech.intellispaces.jaquarius.object;

import tech.intellispaces.jaquarius.annotation.Data;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public interface DataFunctions {

  static boolean isDataObjectHandle(Class<?> objectHandleClass) {
    return isDataDomain(ObjectHandleFunctions.getDomainClassOfObjectHandle(objectHandleClass));
  }

  static boolean isDataDomain(Class<?> domainClass) {
    return isDataDomainInternal(domainClass, new HashSet<>());
  }

  private static boolean isDataDomainInternal(Class<?> aClass, Set<Class<?>> history) {
    if (history.contains(aClass)) {
      return false;
    }
    history.add(aClass);

    for (Annotation a : aClass.getAnnotations()) {
      if (a.annotationType() == Data.class) {
        return true;
      }
    }
    for (Annotation a : aClass.getAnnotations()) {
      if (isDataDomainInternal(a.annotationType(), history)) {
        return true;
      }
    }
    return false;
  }
}
