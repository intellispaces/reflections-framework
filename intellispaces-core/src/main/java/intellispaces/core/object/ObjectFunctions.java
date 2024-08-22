package intellispaces.core.object;

import intellispaces.actions.common.string.StringActions;
import intellispaces.actions.runner.Runner;
import intellispaces.commons.exception.UnexpectedViolationException;
import intellispaces.commons.type.TypeFunctions;
import intellispaces.core.annotation.Data;
import intellispaces.core.annotation.ObjectHandleBunch;
import intellispaces.core.common.AnnotationFunctions;
import intellispaces.core.common.NameConventionFunctions;
import intellispaces.core.space.domain.DomainFunctions;
import intellispaces.javastatements.JavaStatements;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.reference.CustomTypeReference;
import intellispaces.javastatements.reference.NotPrimitiveReference;
import intellispaces.javastatements.reference.TypeReference;
import intellispaces.javastatements.reference.WildcardReference;
import intellispaces.javastatements.type.Type;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class ObjectFunctions {

  private ObjectFunctions() {}

  public static boolean isDataObjectHandle(Class<?> objectHandleClass) {
    return getDomainClassOfObjectHandle(objectHandleClass).isAnnotationPresent(Data.class);
  }

  public static Class<?> getObjectHandleClass(ObjectHandleTypes objectHandleType) {
    return switch (objectHandleType) {
      case Common -> ObjectHandle.class;
      case Movable -> MovableObjectHandle.class;
      case Unmovable -> UnmovableObjectHandle.class;
      case Bunch -> throw UnexpectedViolationException.withMessage("Bunch object handle class is not defined");
    };
  }

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
    return type.isPrimitiveReference() ||
        (type.isCustomTypeReference() && isDefaultObjectHandleType(type.asCustomTypeReferenceOrElseThrow().targetType()));
  }

  public static boolean isDefaultObjectHandleType(CustomType type) {
    return isDefaultObjectHandleType(type.canonicalName());
  }

  public static boolean isDefaultObjectHandleType(String canonicalName) {
    return DEFAULT_OBJECT_HANDLE_CLASSES.contains(canonicalName);
  }

  public static boolean isMovableObjectHandle(Object objectHandle) {
    return isMovableObjectHandle(objectHandle.getClass());
  }

  public static boolean isMovableObjectHandle(Class<?> objectHandleClass) {
    return MovableObjectHandle.class.isAssignableFrom(objectHandleClass);
  }

  public static boolean isMovableObjectHandle(CustomType objectHandleType) {
    return objectHandleType.hasParent(MovableObjectHandle.class.getCanonicalName());
  }

  public static String getBaseObjectHandleTypename(TypeReference domainType) {
    if (domainType.isPrimitiveReference()) {
      return TypeFunctions.getPrimitiveWrapperClass(
          domainType.asPrimitiveReferenceOrElseThrow().typename()).getCanonicalName();
    } else if (domainType.isNamedReference()) {
      return domainType.asNamedReferenceOrElseThrow().name();
    } else if (domainType.isWildcard()) {
      return Object.class.getCanonicalName();
    }
    return getBaseObjectHandleTypename(domainType.asCustomTypeReferenceOrElseThrow().targetType());
  }

  public static String getBaseObjectHandleTypename(CustomType domainType) {
    if (isDefaultObjectHandleType(domainType)) {
      return domainType.className();
    }
    return NameConventionFunctions.getBaseObjectHandleTypename(domainType.className());
  }

  public static String getObjectHandleTypename(CustomType customType, ObjectHandleTypes type) {
    if (isDefaultObjectHandleType(customType)) {
      return customType.canonicalName();
    }
    if (!DomainFunctions.isDomainType(customType)) {
      return customType.canonicalName();
    }
    return NameConventionFunctions.getObjectHandleTypename(customType.className(), type);
  }

  public static String getObjectHandleTypename(String canonicalName, ObjectHandleTypes type) {
    if (isDefaultObjectHandleType(canonicalName)) {
      return canonicalName;
    }
    return NameConventionFunctions.getObjectHandleTypename(canonicalName, type);
  }

  public static boolean isCustomObjectHandleClass(Class<?> aClass) {
    return ObjectHandle.class.isAssignableFrom(aClass);
  }

  public static boolean isCustomObjectHandleType(TypeReference type) {
    return type.isCustomTypeReference() && type.asCustomTypeReferenceOrElseThrow().targetType().hasParent(ObjectHandle.class);
  }

  public static Class<?> defineObjectHandleClass(Class<?> aClass) {
    return defineObjectHandleClassInternal(aClass);
  }

  private static Class<?> defineObjectHandleClassInternal(Class<?> aClass) {
    if (aClass.isAnnotationPresent(intellispaces.core.annotation.MovableObjectHandle.class) ||
        aClass.isAnnotationPresent(intellispaces.core.annotation.UnmovableObjectHandle.class) ||
        isDefaultObjectHandleClass(aClass)
    ) {
      return aClass;
    }
    if (aClass.getSuperclass() != null) {
      Class<?> result = defineObjectHandleClassInternal(aClass.getSuperclass());
      if (result != null) {
        return result;
      }
    }
    for (Class<?> anInterface : aClass.getInterfaces()) {
      Class<?> result = defineObjectHandleClassInternal(anInterface);
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

  public static CustomType getDomainTypeOfObjectHandle(TypeReference objectHandleType) {
    if (objectHandleType.isPrimitiveReference()) {
      Class<?> wrapperClass = TypeFunctions.getPrimitiveWrapperClass(objectHandleType.asPrimitiveReferenceOrElseThrow().typename());
      return JavaStatements.customTypeStatement(wrapperClass);
    } else if (objectHandleType.isCustomTypeReference()) {
      return getDomainTypeOfObjectHandle(objectHandleType.asCustomTypeReferenceOrElseThrow().targetType());
    } else {
      throw UnexpectedViolationException.withMessage("Not implemented");
    }
  }

  public static CustomType getDomainTypeOfObjectHandle(CustomType objectHandleType) {
    List<CustomType> objectHandleTypes = AnnotationFunctions.findTopAnnotatedTypes(
        objectHandleType, ObjectHandleBunch.class.getCanonicalName()
    );
    if (objectHandleTypes.isEmpty()) {
      throw UnexpectedViolationException.withMessage("Could not find object handle type of class {}",
          objectHandleType.canonicalName());
    } else if (objectHandleTypes.size() > 1) {
      throw UnexpectedViolationException.withMessage("More than one object handle type of class {} found",
          objectHandleType.canonicalName());
    }
    return objectHandleTypes.get(0).selectAnnotation(ObjectHandleBunch.class.getCanonicalName()).orElseThrow()
        .elementValue("value").orElseThrow()
        .asClass().orElseThrow()
        .type();
  }

  public static Class<?> getDomainClassOfObjectHandle(Class<?> objectHandleClass) {
    if (isDefaultObjectHandleClass(objectHandleClass)) {
      return objectHandleClass;
    }
    List<Class<?>> objectHandleTypes = AnnotationFunctions.findTopAnnotatedClasses(
        objectHandleClass, ObjectHandleBunch.class
    );
    if (objectHandleTypes.isEmpty()) {
      throw UnexpectedViolationException.withMessage("Could not find object handle type of class {}",
          objectHandleClass.getCanonicalName());
    } else if (objectHandleTypes.size() > 1) {
      throw UnexpectedViolationException.withMessage("More than one object handle type of class {} found",
          objectHandleClass.getCanonicalName());
    }
    return objectHandleTypes.get(0).getAnnotation(ObjectHandleBunch.class).value();
  }

  @SuppressWarnings("unchecked")
  public static <T> T tryDowngrade(Object sourceObjectHandle, Class<T> targetObjectHandleClass) {
    Class<?> sourceObjectHandleClass = sourceObjectHandle.getClass();
    if (isCustomObjectHandleClass(sourceObjectHandleClass) && isCustomObjectHandleClass(targetObjectHandleClass)) {
      Class<?> sourceObjectHandleDomain = getDomainClassOfObjectHandle(sourceObjectHandleClass);
      Class<?> targetObjectHandleDomain = getDomainClassOfObjectHandle(targetObjectHandleClass);
      if (targetObjectHandleDomain.isAssignableFrom(sourceObjectHandleDomain)) {
        if (isMovableObjectHandle(targetObjectHandleClass)) {
          return (T) tryCreateDowngradeObjectHandle(sourceObjectHandle, sourceObjectHandleDomain, targetObjectHandleDomain);
        }
      }
    }
    return null;
  }

  private static Object tryCreateDowngradeObjectHandle(
      Object sourceObjectHandle, Class<?> sourceObjectHandleDomain, Class<?> targetObjectHandleDomain
  ) {
    String downgradeObjectHandleCanonicalName = NameConventionFunctions.getMovableDownwardObjectHandleTypename(
        sourceObjectHandleDomain, targetObjectHandleDomain);
    Optional<Class<?>> downgradeObjectHandleClass = TypeFunctions.getClass(downgradeObjectHandleCanonicalName);
    if (downgradeObjectHandleClass.isPresent()) {
      try {
        Constructor<?> constructor = downgradeObjectHandleClass.get().getConstructors()[0];
        return constructor.newInstance(sourceObjectHandle);
      } catch (Exception e) {
        throw UnexpectedViolationException.withCauseAndMessage(e, "Could not create downgrade object handle");
      }
    }
    return null;
  }

  public static String getObjectHandleDeclaration(
      TypeReference domainType, ObjectHandleTypes handleType, Function<String, String> simpleNameMapping
  ) {
    if (domainType.isPrimitiveReference()) {
      return domainType.asPrimitiveReferenceOrElseThrow().typename();
    } else if (domainType.asNamedReference().isPresent()) {
      return domainType.asNamedReference().get().name();
    } else if (domainType.isCustomTypeReference()) {
      CustomTypeReference customTypeReference = domainType.asCustomTypeReferenceOrElseThrow();
      CustomType targetType = customTypeReference.targetType();
      if (targetType.canonicalName().equals(Class.class.getCanonicalName())) {
        var sb = new StringBuilder();
        sb.append(Class.class.getSimpleName());
        if (!customTypeReference.typeArguments().isEmpty()) {
          sb.append("<");
          Runner commaAppender = StringActions.skippingFirstTimeCommaAppender(sb);
          for (NotPrimitiveReference argType : customTypeReference.typeArguments()) {
            commaAppender.run();
            sb.append(argType.actualDeclaration());
          }
          sb.append(">");
        }
        return sb.toString();
      } else if (targetType.canonicalName().startsWith("java.lang.")) {
        return targetType.simpleName();
      } else {
        var sb = new StringBuilder();
        String canonicalName = ObjectFunctions.getObjectHandleTypename(targetType, handleType);
        String simpleName = simpleNameMapping.apply(canonicalName);
        sb.append(simpleName);
        if (!customTypeReference.typeArguments().isEmpty()) {
          sb.append("<");
          Runner commaAppender = StringActions.skippingFirstTimeCommaAppender(sb);
          for (NotPrimitiveReference argType : customTypeReference.typeArguments()) {
            commaAppender.run();
            sb.append(getObjectHandleDeclaration(argType, ObjectHandleTypes.Common, simpleNameMapping));
          }
          sb.append(">");
        }
        return sb.toString();
      }
    } else if (domainType.isWildcard()) {
      WildcardReference wildcardTypeReference = domainType.asWildcardOrElseThrow();
      if (wildcardTypeReference.extendedBound().isPresent()) {
        return getObjectHandleDeclaration(wildcardTypeReference.extendedBound().get(), handleType, simpleNameMapping);
      } else {
        return Object.class.getCanonicalName();
      }
    } else if (domainType.isArrayReference()) {
      TypeReference elementType = domainType.asArrayReferenceOrElseThrow().elementType();
      return getObjectHandleDeclaration(elementType, handleType, simpleNameMapping) + "[]";
    } else {
      throw new UnsupportedOperationException("Unsupported type - " + domainType.actualDeclaration());
    }
  }

  public static Class<?> propertiesHandleClass() {
    if (propertiesHandleClass == null) {
      propertiesHandleClass = TypeFunctions.getClass(ObjectHandleConstants.PROPERTIES_HANDLE_CLASSNAME).orElseThrow(
          () -> UnexpectedViolationException.withMessage("Could not get class {}", ObjectHandleConstants.PROPERTIES_HANDLE_CLASSNAME)
      );
    }
    return propertiesHandleClass;
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
      Object.class.getCanonicalName(),
      Boolean.class.getCanonicalName(),
      Byte.class.getCanonicalName(),
      Short.class.getCanonicalName(),
      Integer.class.getCanonicalName(),
      Long.class.getCanonicalName(),
      Float.class.getCanonicalName(),
      Double.class.getCanonicalName(),
      Character.class.getCanonicalName(),
      String.class.getCanonicalName(),
      Class.class.getCanonicalName(),
      Type.class.getCanonicalName(),
      Void.class.getCanonicalName()
  );

  private static Class<?> propertiesHandleClass;
}
