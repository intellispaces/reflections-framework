package tech.intellispaces.reflectionsj.dataset;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import tech.intellispaces.reflectionsj.annotation.Dataset;
import tech.intellispaces.reflectionsj.object.reference.ObjectReferenceFunctions;

public interface DatasetFunctions {

  static boolean isDatasetObjectHandle(Class<?> objectHandleClass) {
    return isDatasetDomain(ObjectReferenceFunctions.getDomainClassOfObjectHandle(objectHandleClass));
  }

  static boolean isDatasetDomain(Class<?> domainClass) {
    return isDatasetDomainInternal(domainClass, new HashSet<>());
  }

  private static boolean isDatasetDomainInternal(Class<?> aClass, Set<Class<?>> history) {
    if (history.contains(aClass)) {
      return false;
    }
    history.add(aClass);

    for (Annotation a : aClass.getAnnotations()) {
      if (a.annotationType() == Dataset.class) {
        return true;
      }
    }
    for (Annotation a : aClass.getAnnotations()) {
      if (isDatasetDomainInternal(a.annotationType(), history)) {
        return true;
      }
    }
    return false;
  }
}
