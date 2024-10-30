package intellispaces.jaquarius.object;

import intellispaces.common.action.runner.Runner;
import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.base.text.TextActions;
import intellispaces.common.base.type.Type;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.common.javastatement.JavaStatements;
import intellispaces.common.javastatement.customtype.AnnotationFunctions;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.customtype.CustomTypes;
import intellispaces.common.javastatement.instance.AnnotationInstance;
import intellispaces.common.javastatement.reference.CustomTypeReference;
import intellispaces.common.javastatement.reference.NotPrimitiveReference;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.common.javastatement.reference.WildcardReference;
import intellispaces.jaquarius.annotation.Movable;
import intellispaces.jaquarius.annotation.ObjectHandle;
import intellispaces.jaquarius.annotation.Unmovable;
import intellispaces.jaquarius.annotation.Wrapper;
import intellispaces.jaquarius.common.NameConventionFunctions;
import intellispaces.jaquarius.space.domain.DomainFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class ObjectFunctions {
  private static final Logger LOG = LoggerFactory.getLogger(ObjectFunctions.class);

  public static Class<?> getObjectHandleClass(ObjectHandleTypes objectHandleType) {
    return switch (objectHandleType) {
      case Common -> intellispaces.jaquarius.object.ObjectHandle.class;
      case Movable -> MovableObjectHandle.class;
      case Unmovable -> UnmovableObjectHandle.class;
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
    return isMovableObjectHandle(CustomTypes.of(objectHandleClass));
  }

  public static boolean isMovableObjectHandle(CustomType objectHandleType) {
    return AnnotationFunctions.isAssignableAnnotatedType(objectHandleType, Movable.class);
  }

  public static boolean isUnmovableObjectHandle(CustomType objectHandleType) {
    return AnnotationFunctions.isAssignableAnnotatedType(objectHandleType, Unmovable.class);
  }

  public static String getCommonObjectHandleTypename(TypeReference domainType) {
    return getCommonObjectHandleTypename(domainType, Function.identity());
  }

  public static String getCommonObjectHandleTypename(
      TypeReference type, Function<TypeReference, TypeReference> typeReplacer
  ) {
    type = typeReplacer.apply(type);
    if (type.isPrimitiveReference()) {
      return TypeFunctions.getPrimitiveWrapperClass(
          type.asPrimitiveReferenceOrElseThrow().typename()).getCanonicalName();
    } else if (type.isNamedReference()) {
      return type.asNamedReferenceOrElseThrow().name();
    } else if (type.isWildcard()) {
      if (type.asWildcardOrElseThrow().extendedBound().isEmpty()) {
        return "?";
      }
      return "? extends " + getCommonObjectHandleTypename(type.asWildcardOrElseThrow().extendedBound().get(), typeReplacer);
    }
    return getCommonObjectHandleTypename(type.asCustomTypeReferenceOrElseThrow().targetType());
  }

  public static String getCommonObjectHandleTypename(CustomType domainType) {
    if (isDefaultObjectHandleType(domainType)) {
      return domainType.canonicalName();
    }
    return NameConventionFunctions.getCommonObjectHandleTypename(domainType.className());
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
    Wrapper wrapper = aClass.getAnnotation(Wrapper.class);
    if (wrapper != null) {
      aClass = wrapper.value();
    }
    return aClass.isAnnotationPresent(ObjectHandle.class);
  }

  public static boolean isCustomObjectHandleType(TypeReference type) {
    if (!type.isCustomTypeReference()) {
      return false;
    }
    CustomType customType = type.asCustomTypeReferenceOrElseThrow().targetType();
    return AnnotationFunctions.isAssignableAnnotatedType(customType, ObjectHandle.class);
  }

  public static Class<?> getObjectHandleClass(Class<?> aClass) {
    return defineObjectHandleClassInternal(aClass);
  }

  private static Class<?> defineObjectHandleClassInternal(Class<?> aClass) {
    if (aClass.isAnnotationPresent(ObjectHandle.class) ||
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
      Class<?> wrapperClass = TypeFunctions.getPrimitiveWrapperClass(
        objectHandleType.asPrimitiveReferenceOrElseThrow().typename()
      );
      return JavaStatements.customTypeStatement(wrapperClass);
    } else if (objectHandleType.isCustomTypeReference()) {
      return getDomainTypeOfObjectHandle(objectHandleType.asCustomTypeReferenceOrElseThrow().targetType());
    } else {
      throw UnexpectedViolationException.withMessage("Not implemented");
    }
  }

  public static CustomType getDomainTypeOfObjectHandle(CustomType objectHandleType) {
    Optional<AnnotationInstance> wrapper = objectHandleType.selectAnnotation(Wrapper.class.getCanonicalName());
    if (wrapper.isPresent()) {
      objectHandleType = wrapper.get()
          .elementValue("value").orElseThrow()
          .asClass().orElseThrow()
          .type();
    }

    Optional<AnnotationInstance> objectHandle = objectHandleType.selectAnnotation(
      ObjectHandle.class.getCanonicalName()
    );
    if (objectHandle.isPresent()) {
      return objectHandle.get()
          .elementValue("value").orElseThrow()
          .asClass().orElseThrow()
          .type();
    }
    throw UnexpectedViolationException.withMessage("Object handle class {0} must be annotated with annotation {1}",
        objectHandleType.canonicalName(), ObjectHandle.class.getSimpleName());
  }

  public static Class<?> getDomainClassOfObjectHandle(Class<?> objectHandleClass) {
    if (isDefaultObjectHandleClass(objectHandleClass)) {
      return objectHandleClass;
    }
    Wrapper wrapper = objectHandleClass.getAnnotation(Wrapper.class);
    if (wrapper != null) {
      objectHandleClass = wrapper.value();
    }

    ObjectHandle objectHandle = objectHandleClass.getAnnotation(
      ObjectHandle.class
    );
    if (objectHandle != null) {
      return objectHandle.value();
    }
    throw UnexpectedViolationException.withMessage("Object handle class {0} must be annotated with annotation {1}",
        objectHandleClass.getCanonicalName(), ObjectHandle.class.getSimpleName());
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
          Runner commaAppender = TextActions.skippingFirstTimeCommaAppender(sb);
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
          Runner commaAppender = TextActions.skippingFirstTimeCommaAppender(sb);
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
        return "? extends " + getObjectHandleDeclaration(
            wildcardTypeReference.extendedBound().get(), handleType, simpleNameMapping
        );
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
          () -> UnexpectedViolationException.withMessage("Could not get class {0}",
              ObjectHandleConstants.PROPERTIES_HANDLE_CLASSNAME)
      );
    }
    return propertiesHandleClass;
  }

  public static void releaseSilently(intellispaces.jaquarius.object.ObjectHandle<?> objectHandle) {
    if (objectHandle == null) {
      return;
    }
    try {
      objectHandle.release();
    } catch (Exception e) {
      LOG.error("Could not release object handle", e);
    }
  }

  public static void releaseEach(List<intellispaces.jaquarius.object.ObjectHandle<?>> objectHandles) {
    List<Exception> exceptions = null;
    for (var objectHandle : objectHandles) {
      try {
        objectHandle.release();
      } catch (Exception e) {
        if (exceptions == null) {
          exceptions = new ArrayList<>();
          exceptions.add(e);
        }
      }
    }
    if (exceptions != null) {
      UnexpectedViolationException uve = UnexpectedViolationException.withMessage("Could not release object handles");
      exceptions.forEach(uve::addSuppressed);
      throw uve;
    }
  }

  private ObjectFunctions() {}

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
      Number.class.getCanonicalName(),
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
