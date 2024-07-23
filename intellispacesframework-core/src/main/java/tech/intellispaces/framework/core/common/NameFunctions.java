package tech.intellispaces.framework.core.common;

import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;
import tech.intellispaces.framework.commons.string.StringFunctions;
import tech.intellispaces.framework.commons.type.TypeFunctions;
import tech.intellispaces.framework.core.annotation.Domain;
import tech.intellispaces.framework.core.annotation.ObjectHandle;
import tech.intellispaces.framework.core.annotation.Ontology;
import tech.intellispaces.framework.core.annotation.Transition;
import tech.intellispaces.framework.core.object.ObjectHandleTypes;
import tech.intellispaces.framework.core.traverse.TraverseType;
import tech.intellispaces.framework.core.traverse.TraverseTypes;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodParam;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;

import java.util.Optional;

public interface NameFunctions {

  static String getObjectHandleImplementationTypename(CustomType objectHandleType) {
    Optional<ObjectHandle> annotation = objectHandleType.selectAnnotation(ObjectHandle.class);
    if (annotation.isPresent()) {
      return TypeFunctions.replaceSimpleName(objectHandleType.canonicalName(), annotation.get().value());
    } else {
      return objectHandleType.canonicalName() + "Impl";
    }
  }

  static String getObjectHandleImplementationTypename(Class<?> objectHandleClass) {
    ObjectHandle annotation = objectHandleClass.getAnnotation(ObjectHandle.class);
    if (annotation != null) {
      return TypeFunctions.replaceSimpleName(objectHandleClass.getCanonicalName(), annotation.value());
    } else {
      return objectHandleClass.getCanonicalName() + "Impl";
    }
  }

  static String getObjectHandleTypename(String domainClassName, ObjectHandleTypes handleType) {
    return switch (handleType) {
      case Common -> getCommonObjectHandleTypename(domainClassName);
      case Movable -> getMovableObjectHandleTypename(domainClassName);
      case Unmovable -> getUnmovableObjectHandleTypename(domainClassName);
    };
  }

  static String getCommonObjectHandleTypename(String domainClassName) {
    return transformClassName(domainClassName) + "Handle";
  }

  static String getMovableObjectHandleTypename(String domainClassName) {
    return TypeFunctions.addPrefixToClassName("Movable", transformClassName(domainClassName)) + "Handle";
  }

  static String getUnmovableObjectHandleTypename(String domainClassName) {
    return TypeFunctions.addPrefixToClassName("Unmovable", transformClassName(domainClassName)) + "Handle";
  }

  static String getUnitWrapperCanonicalName(String unitClassName) {
    return transformClassName(unitClassName) + "Wrapper";
  }

  static String getDataClassName(String domainClassName) {
    return transformClassName(domainClassName) + "HandleImpl";
  }

  static String getTransitionClassCanonicalName(MethodStatement transitionMethod) {
    String spaceName = transitionMethod.owner().packageName();
    CustomType domainType = transitionMethod.owner();
    if (!domainType.hasAnnotation(Domain.class)) {
      throw UnexpectedViolationException.withMessage("Transition method {} should be declared in domain class. " +
          "But actual class {} is not marked with annotation {}",
          transitionMethod.name(), domainType.canonicalName(), transitionMethod.owner().canonicalName()
      );
    }
    return getTransitionClassCanonicalName(spaceName, domainType, transitionMethod);
  }

  static String getTransitionClassCanonicalName(
      String spaceName, CustomType domainType, MethodStatement transitionMethod
  ) {
    String transitionSimpleName = transitionMethod.selectAnnotation(Transition.class).orElseThrow().name();
    if (!transitionSimpleName.isBlank()) {
      return TypeFunctions.joinPackageAndSimpleName(spaceName, transitionSimpleName);
    }
    return assignTransitionClassCanonicalName(spaceName, domainType, transitionMethod);
  }

  static String getUnmovableUpgradeObjectHandleTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = TypeFunctions.getPackageName(domainType.canonicalName());
    String simpleName = domainType.simpleName() + "BasedOn" + StringFunctions.capitalizeFirstLetter(baseDomainType.simpleName());
    return TypeFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getMovableDowngradeObjectHandleTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = TypeFunctions.getPackageName(domainType.canonicalName());
    String simpleName = "Movable" +
        StringFunctions.capitalizeFirstLetter(baseDomainType.simpleName()) +
        "BasedOn" + StringFunctions.capitalizeFirstLetter(domainType.simpleName());
    return TypeFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getMovableDowngradeObjectHandleTypename(Class<?> domainClass, Class<?> baseDomainClass) {
    String packageName = TypeFunctions.getPackageName(domainClass.getCanonicalName());
    String simpleName = "Movable" +
        StringFunctions.capitalizeFirstLetter(baseDomainClass.getSimpleName()) +
        "BasedOn" + StringFunctions.capitalizeFirstLetter(domainClass.getSimpleName());
    return TypeFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  private static String assignTransitionClassCanonicalName(
      String spaceName, CustomType domainType, MethodStatement transitionMethod
  ) {
    final String simpleName;
    if (transitionMethod.owner().hasAnnotation(Ontology.class)) {
      simpleName = assumeTransitionClassSimpleNameWhenOntologyClass(transitionMethod);
    } else {
      simpleName = assumeTransitionClassSimpleNameWhenDomainClass(domainType, transitionMethod);
    }
    return TypeFunctions.joinPackageAndSimpleName(spaceName, simpleName);
  }

  private static String assumeTransitionClassSimpleNameWhenDomainClass(
      CustomType domainType, MethodStatement transitionMethod
  ) {
    String simpleName = TypeFunctions.getSimpleName(domainType.canonicalName());
    if (isMappingTraverseType(transitionMethod)) {
      if (isTransformTransition(transitionMethod)) {
        simpleName = StringFunctions.capitalizeFirstLetter(simpleName) + "To" +
            transitionMethod.name().substring(2) + "Transition";
      } else {
        simpleName = StringFunctions.capitalizeFirstLetter(simpleName) + "To" +
            StringFunctions.capitalizeFirstLetter(transitionMethod.name()) + "Transition";
      }
    } else {
      simpleName = StringFunctions.capitalizeFirstLetter(simpleName) +
          StringFunctions.capitalizeFirstLetter(joinMethodNameAndParameterTypes(transitionMethod)) + "Transition";
    }
    return simpleName;
  }

  static String joinMethodNameAndParameterTypes(MethodStatement method) {
    if (method.params().isEmpty()) {
      return method.name();
    }
    var sb = new StringBuilder();
    sb.append(method.name());
    for (MethodParam param : method.params()) {
      if (param.type().isPrimitive()) {
        sb.append(StringFunctions.capitalizeFirstLetter(param.type().asPrimitiveTypeReferenceSurely().typename()));
      } else if (param.type().isCustomTypeReference()) {
        sb.append(param.type().asCustomTypeReferenceSurely().targetType().simpleName());
      } else if (param.type().isNamedTypeReference()) {
        sb.append(param.type().asNamedTypeReferenceSurely().name());
      }
    }
    return sb.toString();
  }

  private static boolean isTransformTransition(MethodStatement transitionMethod) {
    String name = transitionMethod.name();
    return (name.length() > 3 && name.startsWith("as") && Character.isUpperCase(name.charAt(2)));
  }

  private static String assumeTransitionClassSimpleNameWhenOntologyClass(MethodStatement transitionMethod) {
    return StringFunctions.capitalizeFirstLetter(transitionMethod.name()) + "Transition";
  }

  static String getGuideClassCanonicalName(
      TraverseType traverseType, String spaceName, CustomType domainType, MethodStatement transitionMethod
  ) {
    String transitionCanonicalName = getTransitionClassCanonicalName(spaceName, domainType, transitionMethod);
    String suffix = traverseType == TraverseTypes.Mapping ? "Mapper" : "Mover";
    return StringFunctions.replaceLast(transitionCanonicalName, "Transition", suffix + "Guide");
  }

  private static boolean isMappingTraverseType(MethodStatement method) {
    Transition transition = method.selectAnnotation(Transition.class).orElseThrow();
    if (transition.allowedTraverseTypes().length > 1) {
      return transition.defaultTraverseType() == TraverseTypes.Mapping;
    }
    return transition.allowedTraverseTypes()[0] == TraverseTypes.Mapping;
  }

  private static String transformClassName(String className) {
    return className.replace("$", "");
  }
}
