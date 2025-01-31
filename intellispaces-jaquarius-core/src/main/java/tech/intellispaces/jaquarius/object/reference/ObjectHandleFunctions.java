package tech.intellispaces.jaquarius.object.reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.intellispaces.action.runnable.RunnableAction;
import tech.intellispaces.action.text.StringActions;
import tech.intellispaces.general.exception.NotImplementedExceptions;
import tech.intellispaces.general.exception.UnexpectedException;
import tech.intellispaces.general.exception.UnexpectedExceptions;
import tech.intellispaces.general.text.StringFunctions;
import tech.intellispaces.general.type.ClassFunctions;
import tech.intellispaces.general.type.Type;
import tech.intellispaces.jaquarius.annotation.Movable;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.annotation.Unmovable;
import tech.intellispaces.jaquarius.annotation.Wrapper;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.jaquarius.traverse.TraverseQualifierSetForm;
import tech.intellispaces.jaquarius.traverse.TraverseQualifierSetForms;
import tech.intellispaces.java.reflection.JavaStatements;
import tech.intellispaces.java.reflection.customtype.AnnotationFunctions;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.customtype.CustomTypes;
import tech.intellispaces.java.reflection.instance.AnnotationInstance;
import tech.intellispaces.java.reflection.method.MethodParam;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.*;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class ObjectHandleFunctions {
  private static final Logger LOG = LoggerFactory.getLogger(ObjectHandleFunctions.class);

  public static Class<?> getObjectHandleClass(ObjectHandleTypes objectHandleType) {
    return switch (objectHandleType) {
      case General -> tech.intellispaces.jaquarius.object.reference.ObjectHandle.class;
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

  public static boolean isDefaultObjectHandleClass(CustomType aClass) {
    return DEFAULT_OBJECT_HANDLE_CLASSES.contains(aClass.canonicalName());
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
      return ClassFunctions.wrapperClassOfPrimitive(type.asPrimitiveReferenceOrElseThrow().typename()).getCanonicalName();
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
    return NameConventionFunctions.getGeneralObjectHandleTypename(domainType.className());
  }

  public static String getObjectHandleTypename(CustomType customType, ObjectHandleType type) {
    if (isDefaultObjectHandleType(customType)) {
      return customType.canonicalName();
    }
    if (!DomainFunctions.isDomainType(customType)) {
      return customType.canonicalName();
    }
    return NameConventionFunctions.getObjectHandleTypename(customType.className(), type);
  }

  public static String getObjectHandleTypename(String canonicalName, ObjectHandleType type) {
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
    Class<?> actualType1 = ClassFunctions.getObjectClass(type1);
    Class<?> actualType2 = ClassFunctions.getObjectClass(type2);
    return actualType2 == actualType1 || actualType1.isAssignableFrom(actualType2);
  }

  public static CustomType getDomainTypeOfObjectHandle(TypeReference objectHandleType) {
    if (objectHandleType.isPrimitiveReference()) {
      Class<?> wrapperClass = ClassFunctions.wrapperClassOfPrimitive(
        objectHandleType.asPrimitiveReferenceOrElseThrow().typename()
      );
      return JavaStatements.customTypeStatement(wrapperClass);
    } else if (objectHandleType.isCustomTypeReference()) {
      return getDomainTypeOfObjectHandle(objectHandleType.asCustomTypeReferenceOrElseThrow().targetType());
    } else {
      throw NotImplementedExceptions.withCode("yZJg8A");
    }
  }

  public static CustomType getDomainTypeOfObjectHandle(CustomType objectHandleType) {
    if (isDefaultObjectHandleType(objectHandleType)) {
      if (ClassFunctions.isPrimitiveClass(objectHandleType.canonicalName())) {
        return CustomTypes.of(ClassFunctions.wrapperClassOfPrimitive((objectHandleType.canonicalName())));
      }
      return objectHandleType;
    }

    Optional<AnnotationInstance> wrapper = objectHandleType.selectAnnotation(Wrapper.class.getCanonicalName());
    if (wrapper.isPresent()) {
      objectHandleType = wrapper.get()
          .value().orElseThrow()
          .asClass().orElseThrow()
          .type();
    }

    Optional<AnnotationInstance> objectHandle = objectHandleType.selectAnnotation(
      ObjectHandle.class.getCanonicalName()
    );
    if (objectHandle.isPresent()) {
      return objectHandle.get()
          .value().orElseThrow()
          .asClass().orElseThrow()
          .type();
    }
    throw UnexpectedExceptions.withMessage("Object handle class {0} must be annotated with annotation {1}",
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

    ObjectHandle objectHandle = objectHandleClass.getAnnotation(ObjectHandle.class);
    if (objectHandle != null) {
      return objectHandle.value();
    }
    throw UnexpectedExceptions.withMessage("Object handle class {0} must be annotated with annotation {1}",
        objectHandleClass.getCanonicalName(), ObjectHandle.class.getSimpleName());
  }

  @SuppressWarnings("unchecked")
  public static <T> T tryDowngrade(Object sourceObjectHandle, Class<T> targetObjectHandleClass) {
    Class<?> sourceObjectHandleClass = sourceObjectHandle.getClass();
    if (isCustomObjectHandleClass(sourceObjectHandleClass) && isCustomObjectHandleClass(targetObjectHandleClass)) {
      CustomType sourceObjectHandleDomain = getDomainTypeOfObjectHandle(CustomTypes.of(sourceObjectHandleClass));
      CustomType targetObjectHandleDomain = getDomainTypeOfObjectHandle(CustomTypes.of(targetObjectHandleClass));
      if (sourceObjectHandleDomain.hasParent(targetObjectHandleDomain)) {
        if (isMovableObjectHandle(targetObjectHandleClass)) {
          return (T) tryCreateDowngradeObjectHandle(sourceObjectHandle, sourceObjectHandleDomain, targetObjectHandleDomain);
        }
      }
    }
    return null;
  }

  private static Object tryCreateDowngradeObjectHandle(
      Object sourceObjectHandle, CustomType sourceObjectHandleDomain, CustomType targetObjectHandleDomain
  ) {
    String downgradeObjectHandleCanonicalName = NameConventionFunctions.getMovableDownwardObjectHandleTypename(
        sourceObjectHandleDomain, targetObjectHandleDomain);
    Optional<Class<?>> downgradeObjectHandleClass = ClassFunctions.getClass(downgradeObjectHandleCanonicalName);
    if (downgradeObjectHandleClass.isPresent()) {
      try {
        Constructor<?> constructor = downgradeObjectHandleClass.get().getConstructors()[0];
        return constructor.newInstance(sourceObjectHandle);
      } catch (Exception e) {
        throw UnexpectedExceptions.withCauseAndMessage(e, "Could not create downgrade object handle");
      }
    }
    return null;
  }

  public static ObjectReferenceForm getReferenceForm(TypeReference type, TraverseQualifierSetForm methodForm) {
    if (methodForm.is(TraverseQualifierSetForms.Object.name())) {
      return ObjectReferenceForms.Object;
    } else if (methodForm.is(TraverseQualifierSetForms.Normal.name())) {
      if (type.isCustomTypeReference() &&
          ClassFunctions.isPrimitiveWrapperClass(type.asCustomTypeReferenceOrElseThrow().targetType().canonicalName())
      ) {
        return ObjectReferenceForms.Primitive;
      }
      return ObjectReferenceForms.Object;
    } else {
      throw NotImplementedExceptions.withCode("qvd21A");
    }
  }

  public static String getGeneralObjectHandleDeclaration(
      TypeReference domainType, Function<String, String> simpleNameMapping
  ) {
    return getObjectHandleDeclaration(domainType, ObjectHandleTypes.General, simpleNameMapping);
  }

  public static String getObjectHandleDeclaration(
      TypeReference domainType,
      ObjectHandleType handleType,
      Function<String, String> simpleNameMapping
  ) {
    ObjectReferenceForm referenceForm = getReferenceForm(domainType, TraverseQualifierSetForms.Normal);
    return getObjectHandleDeclaration(domainType, handleType, referenceForm, simpleNameMapping);
  }

  public static String getObjectHandleDeclaration(
      TypeReference domainType,
      ObjectHandleType handleType,
      ObjectReferenceForm form,
      Function<String, String> simpleNameMapping
  ) {
    return getObjectHandleDeclaration(domainType, handleType, form, true, simpleNameMapping);
  }

  public static String getObjectHandleDeclaration(
      TypeReference domainType,
      ObjectHandleType handleType,
      ObjectReferenceForm form,
      boolean includeTypeParams,
      Function<String, String> simpleNameMapping
  ) {
    if (domainType.isPrimitiveReference()) {
      return domainType.asPrimitiveReferenceOrElseThrow().typename();
    } else if (domainType.asNamedReference().isPresent()) {
      return domainType.asNamedReference().get().name();
    } else if (domainType.isCustomTypeReference()) {
      CustomTypeReference customTypeReference = domainType.asCustomTypeReferenceOrElseThrow();
      CustomType targetType = customTypeReference.targetType();
      if (ObjectReferenceForms.Primitive.is(form)) {
        return ClassFunctions.primitiveTypenameOfWrapper(targetType.canonicalName());
      } else if (targetType.canonicalName().equals(Class.class.getCanonicalName())) {
        var sb = new StringBuilder();
        sb.append(Class.class.getSimpleName());
        if (includeTypeParams && !customTypeReference.typeArguments().isEmpty()) {
          sb.append("<");
          RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
          for (NotPrimitiveReference argType : customTypeReference.typeArguments()) {
            commaAppender.run();
            sb.append(argType.actualDeclaration());
          }
          sb.append(">");
        }
        return sb.toString();
      } else if (ClassFunctions.isLanguageClass(targetType.canonicalName())) {
        return targetType.simpleName();
      } else {
        var sb = new StringBuilder();
        String canonicalName = getObjectHandleTypename(targetType, handleType);
        String simpleName = simpleNameMapping.apply(canonicalName);
        sb.append(simpleName);
        if (includeTypeParams && !customTypeReference.typeArguments().isEmpty()) {
          sb.append("<");
          RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
          for (NotPrimitiveReference argType : customTypeReference.typeArguments()) {
            commaAppender.run();
            sb.append(getObjectHandleDeclaration(
                argType, ObjectHandleTypes.General, ObjectReferenceForms.Object, true, simpleNameMapping
            ));
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

  public static String getObjectHandleTypeParams(
      CustomType domainType,
      ObjectHandleType handleType,
      ObjectReferenceForm form,
      Function<String, String> simpleNameMapping,
      boolean full
  ) {
    if (domainType.typeParameters().isEmpty()) {
      return "";
    }

    var sb = new StringBuilder();
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
    sb.append("<");
    for (NamedReference namedReference : domainType.typeParameters()) {
      commaAppender.run();
      if (!full || namedReference.extendedBounds().isEmpty()) {
        sb.append(namedReference.name());
      } else {
        sb.append(namedReference.name());
        sb.append(" extends ");
        RunnableAction boundCommaAppender = StringActions.skipFirstTimeCommaAppender(sb);
        for (ReferenceBound bound : namedReference.extendedBounds()) {
          boundCommaAppender.run();
          sb.append(getObjectHandleDeclaration(bound, handleType, form, true, simpleNameMapping));
        }
      }
    }
    sb.append(">");
    return sb.toString();
  }

  public static String buildObjectHandleGuideMethodName(MethodStatement method) {
    var sb = new StringBuilder();
    sb.append("$");
    sb.append(method.name());
    if (!method.params().isEmpty()) {
      sb.append("With");
    }
    RunnableAction andAppender = StringActions.skipFirstTimeSeparatorAppender(sb, "And");
    for (MethodParam param : method.params()) {
      andAppender.run();
      sb.append(StringFunctions.removeTailIfPresent(StringFunctions.capitalizeFirstLetter(param.type().simpleDeclaration()), "Handle"));
    }
    sb.append("Guide");
    return sb.toString();
  }

  public static Class<?> propertiesHandleClass() {
    if (propertiesHandleClass == null) {
      propertiesHandleClass = ClassFunctions.getClass(ObjectHandleConstants.DICTIONARY_HANDLE_CLASSNAME).orElseThrow(
          () -> UnexpectedExceptions.withMessage("Could not get class {0}",
              ObjectHandleConstants.DICTIONARY_HANDLE_CLASSNAME)
      );
    }
    return propertiesHandleClass;
  }

  public static void releaseSilently(ObjectReference<?> objectReference) {
    if (objectReference == null) {
      return;
    }
    try {
      objectReference.release();
    } catch (Exception e) {
      LOG.error("Could not release object reference", e);
    }
  }

  public static void releaseEach(List<ObjectReference<?>> objectReferences) {
    List<Exception> exceptions = null;
    for (var objectReference : objectReferences) {
      try {
        objectReference.release();
      } catch (Exception e) {
        if (exceptions == null) {
          exceptions = new ArrayList<>();
          exceptions.add(e);
        }
      }
    }
    if (exceptions != null) {
      UnexpectedException ue = UnexpectedExceptions.withMessage("Could not release object handles");
      exceptions.forEach(ue::addSuppressed);
      throw ue;
    }
  }

  private ObjectHandleFunctions() {}

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
