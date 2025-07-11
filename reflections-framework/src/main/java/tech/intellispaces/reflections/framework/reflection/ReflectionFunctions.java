package tech.intellispaces.reflections.framework.reflection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.intellispaces.actions.runnable.RunnableAction;
import tech.intellispaces.actions.text.StringActions;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedException;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.commons.type.ClassFunctions;
import tech.intellispaces.commons.type.Classes;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.javareflection.JavaStatements;
import tech.intellispaces.javareflection.customtype.AnnotationFunctions;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.customtype.CustomTypes;
import tech.intellispaces.javareflection.instance.AnnotationInstance;
import tech.intellispaces.javareflection.method.MethodParam;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.javareflection.reference.CustomTypeReference;
import tech.intellispaces.javareflection.reference.NamedReference;
import tech.intellispaces.javareflection.reference.NotPrimitiveReference;
import tech.intellispaces.javareflection.reference.ReferenceBound;
import tech.intellispaces.javareflection.reference.TypeReference;
import tech.intellispaces.javareflection.reference.WildcardReference;
import tech.intellispaces.reflections.framework.annotation.Movable;
import tech.intellispaces.reflections.framework.annotation.Reflection;
import tech.intellispaces.reflections.framework.annotation.Wrapper;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.node.ReflectionsNodeFunctions;
import tech.intellispaces.reflections.framework.settings.DomainAssignments;
import tech.intellispaces.reflections.framework.settings.DomainReference;
import tech.intellispaces.reflections.framework.space.domain.DomainFunctions;

public class ReflectionFunctions {
  private static final Logger LOG = LoggerFactory.getLogger(ReflectionFunctions.class);

  public static boolean isObjectFormType(TypeReference type) {
    if (type.isPrimitiveReference()) {
      return true;
    }
    if (type.isCustomTypeReference()) {
      CustomType targetType = type.asCustomTypeReferenceOrElseThrow().targetType();
      if (targetType.hasParent(SystemReflection.class)) {
        return true;
      }
    }
    return getDomainOfObjectForm(type).isPresent();
  }

  public static boolean isObjectFormClass(Class<?> aClass) {
    Wrapper wrapper = aClass.getAnnotation(Wrapper.class);
    if (wrapper != null) {
      aClass = wrapper.value();
    }
    if (isDefaultReflectionType(aClass.getCanonicalName())) {
      return true;
    }
    if (aClass.isAnnotationPresent(Reflection.class)) {
      return true;
    }
    if (SystemReflection.class.isAssignableFrom(aClass)) {
      return true;
    }
    Optional<Class<?>> domainClass = Classes.get(
        NameConventionFunctions.getDomainNameOfRegularObjectForm(aClass.getCanonicalName())
    );
    return domainClass.isPresent();
  }

  public static boolean isReflectionType(TypeReference type) {
    return isDefaultReflectionType(type) || isCustomReflectionType(type);
  }

  public static boolean isReflectionClass(Class<?> aClass) {
    return isDefaultReflectionClass(aClass) || isCustomReflectionClass(aClass);
  }

  public static boolean isCustomReflectionType(TypeReference type) {
    if (!type.isCustomTypeReference()) {
      return false;
    }
    CustomType customType = type.asCustomTypeReferenceOrElseThrow().targetType();
    return customType.hasParent(SystemReflection.class);
  }

  public static boolean isCustomReflectionClass(Class<?> aClass) {
    Wrapper wrapper = aClass.getAnnotation(Wrapper.class);
    if (wrapper != null) {
      aClass = wrapper.value();
    }
    if (aClass.isAnnotationPresent(Reflection.class)) {
      return true;
    }
    if (SystemReflection.class.isAssignableFrom(aClass)) {
      return true;
    };
    Optional<Class<?>> domainClass = Classes.get(
        NameConventionFunctions.getDomainNameOfRegularObjectForm(aClass.getCanonicalName())
    );
    return domainClass.isPresent();
  }

  public static boolean isDefaultReflectionClass(Class<?> aClass) {
    return DEFAULT_REFLECTION_CLASSES.contains(aClass.getCanonicalName());
  }

  public static boolean isDefaultReflectionClass(CustomType aClass) {
    return DEFAULT_REFLECTION_CLASSES.contains(aClass.canonicalName());
  }

  public static boolean isDefaultReflectionType(TypeReference type) {
    return type.isPrimitiveReference() ||
        (type.isCustomTypeReference() && isDefaultReflectionType(type.asCustomTypeReferenceOrElseThrow().targetType()));
  }

  public static boolean isDefaultReflectionType(CustomType type) {
    return isDefaultReflectionType(type.canonicalName());
  }

  public static boolean isDefaultReflectionType(String canonicalName) {
    return DEFAULT_REFLECTION_CLASSES.contains(canonicalName);
  }

  public static boolean isMovableReflection(Object reflection) {
    return isMovableReflection(reflection.getClass());
  }

  public static boolean isMovableReflection(Class<?> reflectionClass) {
    return isMovableReflection(CustomTypes.of(reflectionClass));
  }

  public static boolean isMovableReflection(CustomType reflectionType) {
    return AnnotationFunctions.isAssignableAnnotatedType(reflectionType, Movable.class);
  }

  public static boolean isUnmovableReflection(CustomType reflectionType) {
    return !isMovableReflection(reflectionType);
  }

  public static Class<?> getReflectionClass(MovabilityType movabilityType) {
    return switch (MovabilityTypes.of(movabilityType)) {
      case General -> SystemReflection.class;
      case Movable -> MovableReflection.class;
    };
  }

  public static String getObjectFormTypename(
          ReflectionForm form, TypeReference type, Function<TypeReference, TypeReference> typeReplacer
  ) {
    return switch (ReflectionForms.of(form)) {
      case Reflection -> getGeneralReflectionTypename(type, typeReplacer);
      default -> throw NotImplementedExceptions.withCode("UoXguA");
    };
  }

  public static String getGeneralReflectionTypename(TypeReference domainType) {
    return getGeneralReflectionTypename(domainType, Function.identity());
  }

  public static String getGeneralReflectionTypename(
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
      return "? extends " + getGeneralReflectionTypename(type.asWildcardOrElseThrow().extendedBound().get(), typeReplacer);
    }
    return getGeneralReflectionTypename(type.asCustomTypeReferenceOrElseThrow().targetType());
  }

  public static String getGeneralRegularObjectTypename(
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
      return "? extends " + getGeneralRegularObjectTypename(type.asWildcardOrElseThrow().extendedBound().get(), typeReplacer);
    }
    return getGeneralRegularObjectTypename(type.asCustomTypeReferenceOrElseThrow().targetType());
  }

  public static String getGeneralRegularObjectTypename(CustomType domainType) {
    if (isDefaultReflectionType(domainType)) {
      return domainType.canonicalName();
    }
    return NameConventionFunctions.getGeneralReflectionTypeName(domainType.className(), false);
  }

  public static String getGeneralReflectionTypename(CustomType domainType) {
    if (isDefaultReflectionType(domainType)) {
      return domainType.canonicalName();
    }
    return NameConventionFunctions.getGeneralReflectionTypeName(domainType.className(), false);
  }

  public static String getUnmovableReflectionTypename(CustomType domainType) {
    if (isDefaultReflectionType(domainType)) {
      return domainType.canonicalName();
    }
    return NameConventionFunctions.getUnmovableReflectionTypeName(domainType.className());
  }

  public static String getObjectTypename(
          CustomType customType, ReflectionForm objectForm, MovabilityType movabilityType, boolean replaceDomainWithDelegate
  ) {
    if (isDefaultReflectionType(customType)) {
      return customType.canonicalName();
    }
    if (!DomainFunctions.isDomainType(customType)) {
      return customType.canonicalName();
    }
    return NameConventionFunctions.getObjectTypename(
        customType.canonicalName(),
        objectForm,
        movabilityType,
        replaceDomainWithDelegate
    );
  }

  public static String getObjectTypename(
      String domainClassName, ReflectionForm objectForm, MovabilityType movabilityType
  ) {
    if (isDefaultReflectionType(domainClassName)) {
      return domainClassName;
    }
    return NameConventionFunctions.getObjectTypename(domainClassName, objectForm, movabilityType, true);
  }

  public static Class<?> getReflectionClass(Class<?> aClass) {
    return defineReflectionClassInternal(aClass);
  }

  private static Class<?> defineReflectionClassInternal(Class<?> aClass) {
    if (aClass.isAnnotationPresent(Reflection.class) || isDefaultReflectionClass(aClass)) {
      return aClass;
    }
    if (aClass.getSuperclass() != null) {
      Class<?> result = defineReflectionClassInternal(aClass.getSuperclass());
      if (result != null) {
        return result;
      }
    }
    for (Class<?> anInterface : aClass.getInterfaces()) {
      Class<?> result = defineReflectionClassInternal(anInterface);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  public static Optional<CustomType> getDomainOfObjectForm(TypeReference objectFormType) {
    if (objectFormType.isPrimitiveReference()) {
      Class<?> wrapperClass = ClassFunctions.wrapperClassOfPrimitive(
        objectFormType.asPrimitiveReferenceOrElseThrow().typename()
      );
      return Optional.of(JavaStatements.customTypeStatement(wrapperClass));
    } else if (objectFormType.isCustomTypeReference()) {
      return getDomainOfObjectForm(objectFormType.asCustomTypeReferenceOrElseThrow().targetType());
    } else {
      throw NotImplementedExceptions.withCode("yZJg8A");
    }
  }

  public static Optional<CustomType> getDomainOfObjectForm(CustomType objectFormType) {
    if (isDefaultReflectionType(objectFormType)) {
      return Optional.of(objectFormType);
    }

    Optional<AnnotationInstance> wrapper = objectFormType.selectAnnotation(Wrapper.class.getCanonicalName());
    if (wrapper.isPresent()) {
      objectFormType = wrapper.get()
          .value().orElseThrow()
          .asClass().orElseThrow()
          .type();
    }

    Optional<AnnotationInstance> reflection = objectFormType.selectAnnotation(Reflection.class.getCanonicalName());
    if (reflection.isPresent()) {
      return Optional.of(reflection.get()
          .valueOf("domainClass").orElseThrow()
          .asClass().orElseThrow()
          .type());
    }

    String domainClassName = NameConventionFunctions.getDomainNameOfRegularObjectForm(objectFormType.canonicalName());
    if (objectFormType.canonicalName().equals(domainClassName)) {
      return Optional.of(objectFormType);
    }
    Optional<Class<?>> domainClass = Classes.get(domainClassName);
    return domainClass.map(CustomTypes::of);
  }

  public static CustomType getDomainOfObjectFormOrElseThrow(CustomType objectFormType) {
    Optional<CustomType> domainType = getDomainOfObjectForm(objectFormType);
    if (domainType.isEmpty()) {
      throw UnexpectedExceptions.withMessage("Reflection class {0} must be annotated with annotation {1}",
          objectFormType.canonicalName(), Reflection.class.getSimpleName());
    }
    return domainType.get();
  }

  public static @Nullable Class<?> getReflectionDomainClass(Class<?> reflectionClass) {
    if (isDefaultReflectionClass(reflectionClass)) {
      return reflectionClass;
    }
    Wrapper wrapper = reflectionClass.getAnnotation(Wrapper.class);
    if (wrapper != null) {
      reflectionClass = wrapper.value();
    }

    Reflection reflection = reflectionClass.getAnnotation(Reflection.class);
    if (reflection != null) {
      return reflection.domainClass();
    }

    Optional<Class<?>> domainClass = Classes.get(
        NameConventionFunctions.getDomainNameOfRegularObjectForm(reflectionClass.getCanonicalName())
    );
    return domainClass.orElse(null);
  }

  public static String getGeneralReflectionDeclaration(
      TypeReference domainType, boolean replaceDomainWithDelegate, Function<String, String> simpleNameMapping
  ) {
    return getObjectFormDeclaration(domainType, ReflectionForms.Reflection, MovabilityTypes.General, replaceDomainWithDelegate, simpleNameMapping);
  }

  public static String getObjectFormDeclaration(
      TypeReference domainType,
      ReflectionForm objectForm,
      MovabilityType movabilityType,
      boolean replaceDomainWithDelegate,
      Function<String, String> simpleNameMapping
  ) {
    return getObjectFormDeclaration(domainType, objectForm, movabilityType, true, replaceDomainWithDelegate, simpleNameMapping);
  }

  public static String getObjectFormDeclaration(
      TypeReference domainType,
      ReflectionForm objectForm,
      MovabilityType movabilityType,
      boolean includeTypeParams,
      boolean replaceDomainWithDelegate,
      Function<String, String> simpleNameMapping
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
        String canonicalName = getObjectTypename(targetType, objectForm, movabilityType, replaceDomainWithDelegate);
        String simpleName = simpleNameMapping.apply(canonicalName);
        sb.append(simpleName);
        if (includeTypeParams && !customTypeReference.typeArguments().isEmpty()) {
          sb.append("<");
          RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
          for (NotPrimitiveReference argType : customTypeReference.typeArguments()) {
            commaAppender.run();
            sb.append(getObjectFormDeclaration(
                argType, ReflectionForms.Reflection, MovabilityTypes.General, true, replaceDomainWithDelegate, simpleNameMapping
            ));
          }
          sb.append(">");
        }
        return sb.toString();
      }
    } else if (domainType.isWildcard()) {
      WildcardReference wildcardTypeReference = domainType.asWildcardOrElseThrow();
      if (wildcardTypeReference.extendedBound().isPresent()) {
        return getObjectFormDeclaration(wildcardTypeReference.extendedBound().get(), objectForm, movabilityType, replaceDomainWithDelegate, simpleNameMapping);
      } else {
        return Object.class.getCanonicalName();
      }
    } else if (domainType.isArrayReference()) {
      TypeReference elementType = domainType.asArrayReferenceOrElseThrow().elementType();
      return getObjectFormDeclaration(elementType, objectForm, movabilityType, replaceDomainWithDelegate, simpleNameMapping) + "[]";
    } else {
      throw new UnsupportedOperationException("Unsupported type - " + domainType.actualDeclaration());
    }
  }

  public static List<String> getObjectFormTypeParamDeclarations(
      CustomType domainType,
      ReflectionForm objectForm,
      MovabilityType movabilityType,
      Function<String, String> simpleNameMapping,
      boolean replaceDomainWithDelegate,
      boolean full
  ) {
    if (domainType.typeParameters().isEmpty()) {
      return List.of();
    }

    var params = new ArrayList<String>();
    for (NamedReference namedReference : domainType.typeParameters()) {
      if (!full || namedReference.extendedBounds().isEmpty()) {
        params.add(namedReference.name());
      } else {
        var sb = new StringBuilder();
        sb.append(namedReference.name());
        sb.append(" extends ");
        RunnableAction boundCommaAppender = StringActions.skipFirstTimeCommaAppender(sb);
        for (ReferenceBound bound : namedReference.extendedBounds()) {
          boundCommaAppender.run();
          sb.append(getObjectFormDeclaration(bound, objectForm, movabilityType, true, replaceDomainWithDelegate, simpleNameMapping));
        }
        params.add(sb.toString());
      }
    }
    return params;
  }

  public static String getObjectFormTypeParamDeclaration(
      CustomType domainType,
      ReflectionForm objectForm,
      MovabilityType movabilityType,
      Function<String, String> simpleNameMapping,
      boolean replaceDomainWithDelegate,
      boolean full
  ) {
    List<String> params = getObjectFormTypeParamDeclarations(
        domainType, objectForm, movabilityType, simpleNameMapping, replaceDomainWithDelegate, full
    );
    if (params.isEmpty()) {
      return "";
    }

    var sb = new StringBuilder();
    sb.append("<");
    RunnableAction commaAppender = StringActions.skipFirstTimeCommaAppender(sb);
    for (String param : params) {
      commaAppender.run();
      sb.append(param);
    }
    sb.append(">");
    return sb.toString();
  }

  public static String buildReflectionGuideMethodName(MethodStatement method) {
    var sb = new StringBuilder();
    sb.append("$");
    sb.append(method.name());
    if (!method.params().isEmpty()) {
      sb.append("With");
    }
    RunnableAction andAppender = StringActions.skipFirstTimeSeparatorAppender(sb, "And");
    for (MethodParam param : method.params()) {
      andAppender.run();
      sb.append(StringFunctions.removeTailIfPresent(
              StringFunctions.capitalizeFirstLetter(param.type().simpleDeclaration()), NameConventionFunctions.REFLECTION
      ));
    }
    sb.append("Guide");
    return sb.toString();
  }

  public static Class<?> propertiesReflectionClass() {
    if (propertiesReflectionClass == null) {
      DomainReference domain = ReflectionsNodeFunctions.ontologyReference().getDomainByType(DomainAssignments.PropertiesSet);
      String reflectionClassName = NameConventionFunctions.getGeneralReflectionTypeName(domain.classCanonicalName(), false);
      propertiesReflectionClass = ClassFunctions.getClass(reflectionClassName).orElseThrow(() ->
          UnexpectedExceptions.withMessage("Could not get class {0}", reflectionClassName)
      );
    }
    return propertiesReflectionClass;
  }

  public static void unbindSilently(Object objectReference) {
    unbindSilently(tech.intellispaces.reflections.framework.reflection.Reflections.reflection(objectReference));
  }

  public static void unbindSilently(SystemReflection reflection) {
    if (reflection == null) {
      return;
    }
    try {
      reflection.unbind();
    } catch (Exception e) {
      LOG.error("Could not unbind object reference", e);
    }
  }

  public static void unbindEach(List<SystemReflection> reflections) {
    List<Exception> exceptions = null;
    for (var objectReference : reflections) {
      try {
        objectReference.unbind();
      } catch (Exception e) {
        if (exceptions == null) {
          exceptions = new ArrayList<>();
          exceptions.add(e);
        }
      }
    }
    if (exceptions != null) {
      UnexpectedException ue = UnexpectedExceptions.withMessage("Could not unbind reflection");
      exceptions.forEach(ue::addSuppressed);
      throw ue;
    }
  }

  private ReflectionFunctions() {}

  private final static Set<String> DEFAULT_REFLECTION_CLASSES = Set.of(
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

  private static Class<?> propertiesReflectionClass;
}
