package tech.intellispaces.framework.core.object;

import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;
import tech.intellispaces.framework.commons.type.TypeFunctions;
import tech.intellispaces.framework.core.annotation.Domain;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.reference.CustomTypeReference;
import tech.intellispaces.framework.javastatements.statement.reference.TypeReference;

import java.util.HashSet;
import java.util.Set;

public class ObjectFunctions {

  public static boolean isObjectHandleClass(Class<?> aClass) {
    return isDefaultObjectHandleClass(aClass) || isCustomObjectHandleClass(aClass);
  }

  public static boolean isObjectHandleType(TypeReference type) {
    return isDefaultObjectHandleType(type) || isCustomObjectHandleType(type);
  }

  public static boolean isDefaultObjectHandleClass(Class<?> aClass) {
    return DEFAULT_OBJECT_HANDLE_CLASSES.contains(aClass.getCanonicalName());
  }

  public static boolean isDefaultObjectHandleType(TypeReference type) {
    return type.isPrimitive() ||
        (type.isCustomTypeReference() && DEFAULT_OBJECT_HANDLE_CLASSES.contains(type.asCustomTypeReferenceSurely().targetType().canonicalName()));
  }

  public static boolean isCustomObjectHandleClass(Class<?> aClass) {
    return ObjectHandle.class.isAssignableFrom(aClass);
  }

  public static boolean isCustomObjectHandleType(TypeReference type) {
    return type.isCustomTypeReference() && type.asCustomTypeReferenceSurely().targetType().hasParent(ObjectHandle.class);
  }

  public static Class<?> seekObjectHandleClass(Class<?> aClass) {
    return seekObjectHandleClassInternal(aClass);
  }

  private static Class<?> seekObjectHandleClassInternal(Class<?> aClass) {
    if (aClass.isAnnotationPresent(tech.intellispaces.framework.core.annotation.ObjectHandle.class) ||
        isDefaultObjectHandleClass(aClass)
    ) {
      return aClass;
    }
    if (aClass.getSuperclass() != null) {
      Class<?> result = seekObjectHandleClassInternal(aClass.getSuperclass());
      if (result != null) {
        return result;
      }
    }
    for (Class<?> anInterface : aClass.getInterfaces()) {
      Class<?> result = seekObjectHandleClassInternal(anInterface);
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

  public static CustomType getDomainTypeOfObjectHandle(CustomType objectHandleType) {
    CustomType domainType = getDomainClassRecursive(objectHandleType, new HashSet<>());
    if (domainType == null) {
      throw UnexpectedViolationException.withMessage("Failed to get domain type of object handle {}", objectHandleType.canonicalName());
    }
    return domainType;
  }

  private static CustomType getDomainClassRecursive(CustomType type, Set<String> history) {
    if (history.contains(type.canonicalName())) {
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

  private final static Set<String> DEFAULT_OBJECT_HANDLE_CLASSES = Set.of(
      boolean.class.getCanonicalName(),
      byte.class.getCanonicalName(),
      short.class.getCanonicalName(),
      int.class.getCanonicalName(),
      long.class.getCanonicalName(),
      float.class.getCanonicalName(),
      double.class.getCanonicalName(),
      char.class.getCanonicalName(),
      Boolean.class.getCanonicalName(),
      Byte.class.getCanonicalName(),
      Short.class.getCanonicalName(),
      Integer.class.getCanonicalName(),
      Long.class.getCanonicalName(),
      Float.class.getCanonicalName(),
      Double.class.getCanonicalName(),
      Character.class.getCanonicalName(),
      String.class.getCanonicalName(),
      Class.class.getCanonicalName()
  );

  private ObjectFunctions() {}
}
