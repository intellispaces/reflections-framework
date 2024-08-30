package intellispaces.core.common;

import intellispaces.commons.exception.UnexpectedViolationException;
import intellispaces.commons.string.StringFunctions;
import intellispaces.commons.type.TypeFunctions;
import intellispaces.core.annotation.Domain;
import intellispaces.core.annotation.ObjectHandle;
import intellispaces.core.annotation.Ontology;
import intellispaces.core.annotation.Transition;
import intellispaces.core.object.ObjectFunctions;
import intellispaces.core.object.ObjectHandleTypes;
import intellispaces.core.space.transition.TransitionFunctions;
import intellispaces.core.traverse.TraverseTypes;
import intellispaces.javastatements.customtype.CustomType;
import intellispaces.javastatements.method.MethodParam;
import intellispaces.javastatements.method.MethodStatement;
import intellispaces.javastatements.reference.CustomTypeReference;

import java.util.Optional;

public interface NameConventionFunctions {

  static String getObjectHandleTypename(String domainClassName, ObjectHandleTypes handleType) {
    return switch (handleType) {
      case Bunch -> getBunchObjectHandleTypename(domainClassName);
      case Common -> getBaseObjectHandleTypename(domainClassName);
      case Movable -> getMovableObjectHandleTypename(domainClassName);
      case Unmovable -> getUnmovableObjectHandleTypename(domainClassName);
    };
  }

  static String getBunchObjectHandleTypename(String domainClassName) {
    if (ObjectFunctions.isDefaultObjectHandleType(domainClassName)) {
      return domainClassName;
    }
    return StringFunctions.replaceEndingOrElseThrow(transformClassName(domainClassName), "Domain", "Bunch");
  }

  static String getBaseObjectHandleTypename(String domainClassName) {
    return StringFunctions.replaceEndingOrElseThrow(transformClassName(domainClassName), "Domain", "");
  }

  static String getMovableObjectHandleTypename(String domainClassName) {
    return TypeFunctions.addPrefixToSimpleName("Movable", getBaseObjectHandleTypename(domainClassName));
  }

  static String getUnmovableObjectHandleTypename(String domainClassName) {
    return TypeFunctions.addPrefixToSimpleName("Unmovable", getBaseObjectHandleTypename(domainClassName));
  }

  static String getObjectHandleWrapperCanonicalName(CustomType objectHandleType) {
    Optional<ObjectHandle> a = objectHandleType.selectAnnotation(ObjectHandle.class);
    if (a.isPresent()) {
      return TypeFunctions.replaceSimpleName(objectHandleType.canonicalName(), a.get().name());
    }
    return objectHandleType.canonicalName() + "Wrapper";
  }

  static String getObjectHandleWrapperCanonicalName(Class<?> objectHandleClass) {
    ObjectHandle a = objectHandleClass.getAnnotation(ObjectHandle.class);
    if (a != null) {
      return TypeFunctions.replaceSimpleName(objectHandleClass.getCanonicalName(), a.name());
    }
    return objectHandleClass.getCanonicalName() + "Wrapper";
  }

  static String getUnitWrapperCanonicalName(String unitClassName) {
    return transformClassName(unitClassName) + "Wrapper";
  }

  static String getAutoGuiderCanonicalName(String guideClassName) {
    return StringFunctions.replaceEndingOrElseThrow(transformClassName(guideClassName), "Guide", "AutoGuide");
  }

  static String getDataClassName(String domainClassName) {
    return StringFunctions.replaceEndingOrElseThrow(transformClassName(domainClassName), "Domain", "Data");
  }

  static String getTransitionClassCanonicalName(MethodStatement transitionMethod) {
    String spaceName = transitionMethod.owner().packageName();
    CustomType owner = transitionMethod.owner();
    if (!owner.hasAnnotation(Domain.class) && !owner.hasAnnotation(Ontology.class)) {
      throw UnexpectedViolationException.withMessage("Transition method {0} should be declared " +
              "in domain or ontology class. But actual class {1} is not marked with annotation",
          transitionMethod.name(), owner.canonicalName()
      );
    }
    return getTransitionClassCanonicalName(spaceName, owner, transitionMethod);
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

  static String getUnmovableUpwardObjectHandleTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = TypeFunctions.getPackageName(domainType.canonicalName());
    String simpleName = StringFunctions.replaceEndingOrElseThrow(domainType.simpleName(), "Domain", "") +
        "BasedOn" +
        StringFunctions.capitalizeFirstLetter(StringFunctions.replaceEndingOrElseThrow(baseDomainType.simpleName(), "Domain", ""));
    return TypeFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getDownwardObjectHandleTypename(
      CustomType domainType, CustomType baseDomainType, ObjectHandleTypes handleType
  ) {
    return switch (handleType) {
      case Bunch -> throw new RuntimeException();
      case Common -> getBaseDownwardObjectHandleTypename(domainType, baseDomainType);
      case Movable -> getMovableDownwardObjectHandleTypename(domainType, baseDomainType);
      case Unmovable -> getUnmovableDownwardObjectHandleTypename(domainType, baseDomainType);
    };
  }

  static String getBaseDownwardObjectHandleTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = TypeFunctions.getPackageName(domainType.canonicalName());
    String simpleName = StringFunctions.capitalizeFirstLetter(baseDomainType.simpleName()) +
        "BasedOn" + StringFunctions.capitalizeFirstLetter(domainType.simpleName());
    return TypeFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getMovableDownwardObjectHandleTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = TypeFunctions.getPackageName(domainType.canonicalName());
    String simpleName = "Movable" +
        StringFunctions.capitalizeFirstLetter(baseDomainType.simpleName()) +
        "BasedOn" + StringFunctions.capitalizeFirstLetter(domainType.simpleName());
    return TypeFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getUnmovableDownwardObjectHandleTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = TypeFunctions.getPackageName(domainType.canonicalName());
    String simpleName = "Unmovable" +
        StringFunctions.capitalizeFirstLetter(baseDomainType.simpleName()) +
        "BasedOn" + StringFunctions.capitalizeFirstLetter(domainType.simpleName());
    return TypeFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getMovableDownwardObjectHandleTypename(Class<?> domainClass, Class<?> baseDomainClass) {
    String packageName = TypeFunctions.getPackageName(domainClass.getCanonicalName());
    String simpleName = "Movable" +
        StringFunctions.capitalizeFirstLetter(baseDomainClass.getSimpleName()) +
        "BasedOn" + StringFunctions.capitalizeFirstLetter(domainClass.getSimpleName());
    return TypeFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getConversionMethodName(CustomTypeReference reference) {
    return getConversionMethodName(reference.targetType());
  }

  static String getConversionMethodName(CustomType targetType) {
    return "as" + StringFunctions.capitalizeFirstLetter(
        StringFunctions.replaceEndingOrElseThrow(targetType.simpleName(), "Domain", ""));
  }

  private static String assignTransitionClassCanonicalName(
      String spaceName, CustomType domainType, MethodStatement transitionMethod
  ) {
    final String simpleName;
    if (transitionMethod.owner().hasAnnotation(Ontology.class)) {
      simpleName = getDefaultTransitionClassSimpleName(transitionMethod);
    } else {
      simpleName = assumeTransitionClassSimpleNameWhenDomainClass(domainType, transitionMethod);
    }
    return TypeFunctions.joinPackageAndSimpleName(spaceName, simpleName);
  }

  private static String assumeTransitionClassSimpleNameWhenDomainClass(
      CustomType domainType, MethodStatement transitionMethod
  ) {
    String simpleName = StringFunctions.replaceEndingOrElseThrow(
        TypeFunctions.getSimpleName(domainType.canonicalName()), "Domain", ""
    );
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
      if (param.type().isPrimitiveReference()) {
        sb.append(StringFunctions.capitalizeFirstLetter(param.type().asPrimitiveReferenceOrElseThrow().typename()));
      } else if (param.type().isCustomTypeReference()) {
        sb.append(param.type().asCustomTypeReferenceOrElseThrow().targetType().simpleName());
      } else if (param.type().isNamedReference()) {
        sb.append(param.type().asNamedReferenceOrElseThrow().name());
      }
    }
    return sb.toString();
  }

  private static boolean isTransformTransition(MethodStatement transitionMethod) {
    String name = transitionMethod.name();
    return (name.length() > 3 && name.startsWith("as") && Character.isUpperCase(name.charAt(2)));
  }

  static String getDefaultTransitionClassSimpleName(MethodStatement transitionMethod) {
    return StringFunctions.capitalizeFirstLetter(transitionMethod.name()) + "Transition";
  }

  static String getGuideClassCanonicalName(
      String spaceName, CustomType domainType, MethodStatement transitionMethod
  ) {
    String transitionCanonicalName = getTransitionClassCanonicalName(spaceName, domainType, transitionMethod);
    return StringFunctions.replaceLast(transitionCanonicalName, "Transition", "Guide");
  }

  private static boolean isMappingTraverseType(MethodStatement method) {
    return TransitionFunctions.getTraverseType(method) == TraverseTypes.Mapping;
  }

  private static String transformClassName(String className) {
    return className.replace("$", "");
  }
}
