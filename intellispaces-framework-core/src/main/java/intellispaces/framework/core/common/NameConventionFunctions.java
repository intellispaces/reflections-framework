package intellispaces.framework.core.common;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.base.text.TextFunctions;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.CustomTypeReference;
import intellispaces.framework.core.annotation.Domain;
import intellispaces.framework.core.annotation.ObjectHandle;
import intellispaces.framework.core.annotation.Ontology;
import intellispaces.framework.core.annotation.Transition;
import intellispaces.framework.core.object.ObjectFunctions;
import intellispaces.framework.core.object.ObjectHandleTypes;
import intellispaces.framework.core.space.transition.TransitionFunctions;

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
    return TextFunctions.replaceEndingOrElseThrow(transformClassName(domainClassName), "Domain", "Bunch");
  }

  static String getBaseObjectHandleTypename(String domainClassName) {
    return TextFunctions.replaceEndingOrElseThrow(transformClassName(domainClassName), "Domain", "");
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
    return TextFunctions.replaceEndingOrElseThrow(transformClassName(guideClassName), "Guide", "AutoGuide");
  }

  static String getDataClassName(String domainClassName) {
    return TextFunctions.replaceEndingOrElseThrow(transformClassName(domainClassName), "Domain", "Data");
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
    String simpleName = TextFunctions.replaceEndingOrElseThrow(domainType.simpleName(), "Domain", "") +
        "BasedOn" +
        TextFunctions.capitalizeFirstLetter(TextFunctions.replaceEndingOrElseThrow(baseDomainType.simpleName(), "Domain", ""));
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
    String simpleName = TextFunctions.capitalizeFirstLetter(baseDomainType.simpleName()) +
        "BasedOn" + TextFunctions.capitalizeFirstLetter(domainType.simpleName());
    return TypeFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getMovableDownwardObjectHandleTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = TypeFunctions.getPackageName(domainType.canonicalName());
    String simpleName = "Movable" +
        TextFunctions.capitalizeFirstLetter(baseDomainType.simpleName()) +
        "BasedOn" + TextFunctions.capitalizeFirstLetter(domainType.simpleName());
    return TypeFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getUnmovableDownwardObjectHandleTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = TypeFunctions.getPackageName(domainType.canonicalName());
    String simpleName = "Unmovable" +
        TextFunctions.capitalizeFirstLetter(baseDomainType.simpleName()) +
        "BasedOn" + TextFunctions.capitalizeFirstLetter(domainType.simpleName());
    return TypeFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getMovableDownwardObjectHandleTypename(Class<?> domainClass, Class<?> baseDomainClass) {
    String packageName = TypeFunctions.getPackageName(domainClass.getCanonicalName());
    String simpleName = "Movable" +
        TextFunctions.capitalizeFirstLetter(baseDomainClass.getSimpleName()) +
        "BasedOn" + TextFunctions.capitalizeFirstLetter(domainClass.getSimpleName());
    return TypeFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getConversionMethodName(CustomTypeReference reference) {
    return getConversionMethodName(reference.targetType());
  }

  static String getConversionMethodName(CustomType targetType) {
    return "as" + TextFunctions.capitalizeFirstLetter(
        TextFunctions.replaceEndingOrElseThrow(targetType.simpleName(), "Domain", ""));
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
    String simpleName = TextFunctions.replaceEndingOrElseThrow(
        TypeFunctions.getSimpleName(domainType.canonicalName()), "Domain", ""
    );
    if (isMappingTraverseType(transitionMethod)) {
      if (isTransformTransition(transitionMethod)) {
        simpleName = TextFunctions.capitalizeFirstLetter(simpleName) + "To" +
            transitionMethod.name().substring(2) + "Transition";
      } else {
        simpleName = TextFunctions.capitalizeFirstLetter(simpleName) + "To" +
            TextFunctions.capitalizeFirstLetter(transitionMethod.name()) + "Transition";
      }
    } else {
      simpleName = TextFunctions.capitalizeFirstLetter(simpleName) +
          TextFunctions.capitalizeFirstLetter(joinMethodNameAndParameterTypes(transitionMethod)) + "Transition";
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
        sb.append(TextFunctions.capitalizeFirstLetter(param.type().asPrimitiveReferenceOrElseThrow().typename()));
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
    return TextFunctions.capitalizeFirstLetter(transitionMethod.name()) + "Transition";
  }

  static String getGuideClassCanonicalName(
      String spaceName, CustomType domainType, MethodStatement transitionMethod
  ) {
    String transitionCanonicalName = getTransitionClassCanonicalName(spaceName, domainType, transitionMethod);
    return TextFunctions.replaceLast(transitionCanonicalName, "Transition", "Guide");
  }

  private static boolean isMappingTraverseType(MethodStatement method) {
    return !TransitionFunctions.getTraverseType(method).isMovingRelated();
  }

  private static String transformClassName(String className) {
    return className.replace("$", "");
  }
}
