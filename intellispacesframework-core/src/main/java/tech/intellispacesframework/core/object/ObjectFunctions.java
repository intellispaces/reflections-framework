package tech.intellispacesframework.core.object;

import tech.intellispacesframework.commons.type.TypeFunctions;
import tech.intellispacesframework.core.annotation.Domain;

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

  public static boolean isCompatibleObjectType(Class<?> type1, Class<?> type2) {
    Class<?> actualType1 = TypeFunctions.getObjectClass(type1);
    Class<?> actualType2 = TypeFunctions.getObjectClass(type2);
    return actualType2 == actualType1 || actualType1.isAssignableFrom(actualType2);
  }

  public static Class<?> getDomainClassOfObjectHandle(Class<?> objectHandleClass) {
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
