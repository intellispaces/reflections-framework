package tech.intellispaces.framework.core.common;

import tech.intellispaces.framework.commons.string.StringFunctions;
import tech.intellispaces.framework.commons.type.TypeFunctions;
import tech.intellispaces.framework.core.annotation.Ontology;
import tech.intellispaces.framework.core.annotation.Transition;
import tech.intellispaces.framework.core.object.ObjectHandleTypes;
import tech.intellispaces.framework.core.traverse.TraverseTypes;
import tech.intellispaces.framework.javastatements.statement.custom.CustomType;
import tech.intellispaces.framework.javastatements.statement.method.MethodStatement;

public interface NameFunctions {

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

  static String getTransitionClassCanonicalName(
      String spaceName, CustomType domainType, MethodStatement transitionMethod
  ) {
    String transitionSimpleName = transitionMethod.selectAnnotation(Transition.class).orElseThrow().name();
    if (!transitionSimpleName.isBlank()) {
      return TypeFunctions.joinPackageAndSimpleName(spaceName, transitionSimpleName);
    }
    return assumeTransitionClassCanonicalName(spaceName, domainType, transitionMethod);
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

  private static String assumeTransitionClassCanonicalName(
      String spaceName, CustomType domainType, MethodStatement transitionMethod
  ) {
    final String simpleName;
    if (transitionMethod.holder().hasAnnotation(Ontology.class)) {
      simpleName = assumeTransitionClassSimpleNameForOntology(transitionMethod);
    } else {
      simpleName = assumeTransitionClassSimpleNameForDomain(domainType, transitionMethod);
    }
    return TypeFunctions.joinPackageAndSimpleName(spaceName, simpleName);
  }

  private static String assumeTransitionClassSimpleNameForDomain(
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
          StringFunctions.capitalizeFirstLetter(transitionMethod.name()) + "Transition";
    }
    return simpleName;
  }

  private static boolean isTransformTransition(MethodStatement transitionMethod) {
    String name = transitionMethod.name();
    return (name.length() > 3 && name.startsWith("as") && Character.isUpperCase(name.charAt(2)));
  }

  private static String assumeTransitionClassSimpleNameForOntology(MethodStatement transitionMethod) {
    return StringFunctions.capitalizeFirstLetter(transitionMethod.name()) + "Transition";
  }

  static String getGuideClassCanonicalName(
      String spaceName, CustomType domainType, MethodStatement transitionMethod
  ) {
    String transitionCanonicalName = getTransitionClassCanonicalName(spaceName, domainType, transitionMethod);
    return StringFunctions.replaceLast(transitionCanonicalName, "Transition", "Guide");
  }

  private static boolean isMappingTraverseType(MethodStatement method) {
    return method.selectAnnotation(Transition.class).orElseThrow().type() == TraverseTypes.Mapping;
  }

  private static String transformClassName(String className) {
    return className.replace("$", "");
  }
}
