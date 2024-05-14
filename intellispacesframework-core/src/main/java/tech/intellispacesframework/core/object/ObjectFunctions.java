package tech.intellispacesframework.core.object;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.commons.type.TypeFunctions;
import tech.intellispacesframework.core.annotation.Domain;
import tech.intellispacesframework.javastatements.statement.custom.CustomType;
import tech.intellispacesframework.javastatements.statement.reference.CustomTypeReference;

import java.util.HashSet;
import java.util.Set;

public class ObjectFunctions {

  public static boolean isObjectHandleClass(Class<?> aClass) {
    return isDefaultObjectHandleClass(aClass) || isCustomObjectHandleClass(aClass);
  }

  public static boolean isDefaultObjectHandleClass(Class<?> aClass) {
    return DEFAULT_OBJECT_HANDLE_CLASSES.contains(aClass);
  }

  public static boolean isCustomObjectHandleClass(Class<?> aClass) {
    return ObjectHandle.class.isAssignableFrom(aClass);
  }

  public static Class<?> getObjectHandleClass(Class<?> aClass) {
    return getObjectHandleClassInternal(aClass);
  }

  private static Class<?> getObjectHandleClassInternal(Class<?> aClass) {
    if (aClass.isAnnotationPresent(tech.intellispacesframework.core.annotation.ObjectHandle.class)) {
      return aClass;
    }
    if (aClass.getSuperclass() != null) {
      Class<?> result = getObjectHandleClassInternal(aClass.getSuperclass());
      if (result != null) {
        return result;
      }
    }
    for (Class<?> anInterface : aClass.getInterfaces()) {
      Class<?> result = getObjectHandleClassInternal(anInterface);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  public static boolean isCompatibleObjectType(Class<?> type1, Class<?> type2) {
    Class<?> actualType1 = TypeFunctions.getObjectClass(type1);
    Class<?> actualType2 = TypeFunctions.getObjectClass(type2);
    return actualType2 == actualType1 || actualType1.isAssignableFrom(actualType2);
  }

  public static CustomType getDomainClassOfObjectHandle(CustomType objectHandleType) {
    CustomType domainType = getDomainClassRecursive(objectHandleType, new HashSet<>());
    if (domainType == null) {
      throw UnexpectedViolationException.withMessage("Failed to get domain type of object handle {}", objectHandleType.canonicalName());
    }
    return domainType;
  }

  private static CustomType getDomainClassRecursive(CustomType type, Set<String> history) {
    if (history.contains(type)) {
      return null;
    }
    history.add(type.canonicalName());

    for (CustomTypeReference parentTypeRef : type.parentTypes()) {
      CustomType parentType = parentTypeRef.targetType();
      if (parentType.hasAnnotation(Domain.class)) {
        return parentType;
      }
    }

    for (CustomTypeReference parentTypeRef : type.parentTypes()) {
      CustomType parentType = parentTypeRef.targetType();
      CustomType result = getDomainClassRecursive(parentType, history);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  public static Class<?> getDomainClassOfObjectHandle(Class<?> objectHandleClass) {
    Class<?> domainClass = getDomainClassRecursive(objectHandleClass, new HashSet<>());
    if (domainClass == null) {
      throw UnexpectedViolationException.withMessage("Failed to get domain type of object handle {}", objectHandleClass.getCanonicalName());
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

  private final static Set<Class<?>> DEFAULT_OBJECT_HANDLE_CLASSES = Set.of(
      boolean.class, Boolean.class,
      byte.class, Byte.class,
      short.class, Short.class,
      int.class, Integer.class,
      long.class, Long.class,
      float.class, Float.class,
      double.class, Double.class,
      char.class, Character.class,
      String.class
  );

  private ObjectFunctions() {}
}
