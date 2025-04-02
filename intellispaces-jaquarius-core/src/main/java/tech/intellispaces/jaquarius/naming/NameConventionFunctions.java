package tech.intellispaces.jaquarius.naming;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.reflection.customtype.CustomType;
import tech.intellispaces.commons.reflection.method.MethodParam;
import tech.intellispaces.commons.reflection.method.MethodStatement;
import tech.intellispaces.commons.reflection.reference.CustomTypeReference;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.commons.type.ClassNameFunctions;
import tech.intellispaces.jaquarius.Jaquarius;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.Domain;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.annotation.Ontology;
import tech.intellispaces.jaquarius.object.reference.MovabilityType;
import tech.intellispaces.jaquarius.object.reference.MovabilityTypes;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.settings.KeyDomain;
import tech.intellispaces.jaquarius.settings.KeyDomainPurposes;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;

import java.util.Optional;

public interface NameConventionFunctions {

  static String convertToDomainClassName(String domainName) {
    return domainName + "Domain";
  }

  static String convertToDomainName(String domainClassName) {
    return StringFunctions.removeTailIfPresent(domainClassName, "Domain");
  }

  static String convertToChannelClassName(String channelName) {
    return channelName + "Channel";
  }

  static String getDomainNameOfPlainObjectForm(String objectCanonicalName) {
    String simpleName = ClassNameFunctions.getSimpleName(objectCanonicalName);
    simpleName = StringFunctions.removeHeadIfPresent(simpleName, "Unmovable");
    simpleName = StringFunctions.removeHeadIfPresent(simpleName, "Movable");
    simpleName = simpleName + "Domain";
    return ClassNameFunctions.replaceSimpleName(objectCanonicalName, simpleName);
  }

  static String getObjectTypename(
      String domainClassName, ObjectReferenceForm objectForm, MovabilityType movabilityType, boolean replaceKeyDomain
  ) {
    return switch (ObjectReferenceForms.from(objectForm)) {
      case Plain -> switch (MovabilityTypes.from(movabilityType)) {
        case Undefined -> getUndefinedPlainObjectTypename(domainClassName);
        case Movable -> getMovablePlainObjectTypename(domainClassName, replaceKeyDomain);
        case Unmovable -> getUnmovablePlainObjectTypename(domainClassName, replaceKeyDomain);
      };
      case ObjectHandle -> switch (MovabilityTypes.from(movabilityType)) {
        case Undefined -> getUndefinedObjectHandleTypename(domainClassName);
        case Movable -> getMovableObjectHandleTypename(domainClassName, replaceKeyDomain);
        case Unmovable -> getUnmovableObjectHandleTypename(domainClassName, replaceKeyDomain);
      };
      case Primitive -> throw NotImplementedExceptions.withCode("LXc75Q");
      case PrimitiveWrapper -> throw NotImplementedExceptions.withCode("XGDuuQ");
    };
  }

  static String getUndefinedPlainObjectTypename(String domainClassName) {
    return StringFunctions.removeTailOrElseThrow(transformClassName(domainClassName), "Domain");
  }

  static String getUndefinedObjectHandleTypename(String domainClassName) {
    return StringFunctions.replaceTailOrElseThrow(transformClassName(domainClassName), "Domain", "Handle");
  }

  static String getUnmovableObjectHandleTypename(String domainClassName) {
    return ClassNameFunctions.addPrefixToSimpleName("Unmovable",
        StringFunctions.replaceTailOrElseThrow(transformClassName(domainClassName), "Domain", "Handle"));
  }

  static String getUnmovablePlainObjectTypename(String domainClassName, boolean replaceKeyDomain) {
    if (replaceKeyDomain) {
      KeyDomain keyDomain = Jaquarius.settings().getKeyDomainByName(convertToDomainName(domainClassName));
      if (keyDomain != null && keyDomain.delegateClassName() != null && (
          KeyDomainPurposes.Number.is(keyDomain.purpose()) ||
              KeyDomainPurposes.Short.is(keyDomain.purpose()) ||
              KeyDomainPurposes.Integer.is(keyDomain.purpose()) ||
              KeyDomainPurposes.Float.is(keyDomain.purpose()) ||
              KeyDomainPurposes.Double.is(keyDomain.purpose())
      )) {
        return keyDomain.delegateClassName();
      }
    }
    return ClassNameFunctions.addPrefixToSimpleName("Unmovable",
        StringFunctions.removeTailOrElseThrow(transformClassName(domainClassName), "Domain"));
  }

  static String getMovablePlainObjectTypename(String domainClassName, boolean replaceKeyDomain) {
    if (replaceKeyDomain) {
      KeyDomain keyDomain = Jaquarius.settings().getKeyDomainByName(convertToDomainName(domainClassName));
      if (keyDomain != null && keyDomain.delegateClassName() != null && (
          KeyDomainPurposes.Number.is(keyDomain.purpose()) ||
              KeyDomainPurposes.Short.is(keyDomain.purpose()) ||
              KeyDomainPurposes.Integer.is(keyDomain.purpose()) ||
              KeyDomainPurposes.Float.is(keyDomain.purpose()) ||
              KeyDomainPurposes.Double.is(keyDomain.purpose())
      )) {
        return keyDomain.delegateClassName();
      }
    }
    return ClassNameFunctions.addPrefixToSimpleName("Movable",
        StringFunctions.removeTailOrElseThrow(transformClassName(domainClassName), "Domain"));
  }

  static String getUnmovableObjectHandleTypename(String domainClassName, boolean replaceKeyDomain) {
    if (replaceKeyDomain) {
      KeyDomain keyDomain = Jaquarius.settings().getKeyDomainByName(convertToDomainName(domainClassName));
      if (keyDomain != null && keyDomain.delegateClassName() != null && (
          KeyDomainPurposes.Number.is(keyDomain.purpose()) ||
              KeyDomainPurposes.Short.is(keyDomain.purpose()) ||
              KeyDomainPurposes.Integer.is(keyDomain.purpose()) ||
              KeyDomainPurposes.Float.is(keyDomain.purpose()) ||
              KeyDomainPurposes.Double.is(keyDomain.purpose())
      )) {
        return keyDomain.delegateClassName();
      }
    }
    return ClassNameFunctions.addPrefixToSimpleName("Unmovable", getUndefinedObjectHandleTypename(domainClassName));
  }

  static String getMovableObjectHandleTypename(String domainClassName, boolean replaceKeyDomain) {
    if (replaceKeyDomain) {
      KeyDomain keyDomain = Jaquarius.settings().getKeyDomainByName(convertToDomainName(domainClassName));
      if (keyDomain != null && keyDomain.delegateClassName() != null && (
          KeyDomainPurposes.Number.is(keyDomain.purpose()) ||
            KeyDomainPurposes.Short.is(keyDomain.purpose()) ||
            KeyDomainPurposes.Integer.is(keyDomain.purpose()) ||
            KeyDomainPurposes.Float.is(keyDomain.purpose()) ||
            KeyDomainPurposes.Double.is(keyDomain.purpose())
      )) {
        return keyDomain.delegateClassName();
      }
    }
    return ClassNameFunctions.addPrefixToSimpleName("Movable", getUndefinedObjectHandleTypename(domainClassName));
  }

  static String getObjectHandleWrapperCanonicalName(CustomType objectHandleType) {
    Optional<ObjectHandle> oha = objectHandleType.selectAnnotation(ObjectHandle.class);
    if (oha.isPresent() && StringFunctions.isNotBlank(oha.get().name())) {
      return ClassNameFunctions.replaceSimpleName(objectHandleType.canonicalName(), oha.get().name());
    }
    return objectHandleType.canonicalName() + "Wrapper";
  }

  static String getObjectHandleWrapperCanonicalName(Class<?> objectHandleClass) {
    ObjectHandle oha = objectHandleClass.getAnnotation(ObjectHandle.class);
    if (oha != null && StringFunctions.isNotBlank(oha.name())) {
      return ClassNameFunctions.replaceSimpleName(objectHandleClass.getCanonicalName(), oha.name());
    }
    return objectHandleClass.getCanonicalName() + "Wrapper";
  }

  static String getUnitWrapperCanonicalName(String unitClassName) {
    return transformClassName(unitClassName) + "Wrapper";
  }

  static String getAutoGuiderCanonicalName(String guideClassName) {
    return StringFunctions.replaceSingleOrElseThrow(transformClassName(guideClassName), "Guide", "AutoGuide");
  }

  static String getUnmovableDatasetClassName(String domainClassName) {
    return ClassNameFunctions.addPrefixToSimpleName("Unmovable",
      StringFunctions.replaceTailOrElseThrow(transformClassName(domainClassName), "Domain", "Dataset"));
  }

  static String getDatasetBuilderCanonicalName(String domainClassName) {
    return StringFunctions.replaceTailOrElseThrow(transformClassName(domainClassName), "Domain", "Builder");
  }

  static String getChannelClassCanonicalName(MethodStatement channelMethod) {
    String spaceName = channelMethod.owner().packageName();
    CustomType owner = channelMethod.owner();
    if (!owner.hasAnnotation(Domain.class) && !owner.hasAnnotation(Ontology.class)) {
      throw UnexpectedExceptions.withMessage("Channel method {0} should be declared " +
              "in domain or ontology class. But actual class {1} is not marked with annotation",
          channelMethod.name(), owner.canonicalName()
      );
    }
    return getChannelClassCanonicalName(spaceName, owner, channelMethod);
  }

  static String getChannelClassCanonicalName(
      String spaceName, CustomType domain, MethodStatement channelMethod
  ) {
    String channelSimpleName = channelMethod.selectAnnotation(Channel.class).orElseThrow().name();
    if (!channelSimpleName.isBlank()) {
      return ClassNameFunctions.joinPackageAndSimpleName(spaceName, channelSimpleName);
    }
    return assignChannelClassCanonicalName(spaceName, domain, channelMethod);
  }

  static String getObjectProviderWrapperClassName(String objectProviderClassName) {
    return transformClassName(objectProviderClassName) + "Wrapper";
  }

  static String getUnmovableUpwardObjectTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = ClassNameFunctions.getPackageName(domainType.canonicalName());
    String simpleName = StringFunctions.removeTailOrElseThrow(domainType.simpleName(), "Domain") +
        "BasedOn" +
        StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(baseDomainType.simpleName(), "Domain"));
    return ClassNameFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getDownwardObjectHandleTypename(
      CustomType domainType, CustomType baseDomainType, MovabilityType movabilityType
  ) {
    return switch (MovabilityTypes.from(movabilityType)) {
      case Undefined -> getUndefinedDownwardObjectTypename(domainType, baseDomainType);
      case Unmovable -> getUnmovableDownwardObjectTypename(domainType, baseDomainType);
      case Movable -> getMovableDownwardObjectTypename(domainType, baseDomainType);
    };
  }

  static String getUndefinedDownwardObjectTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = ClassNameFunctions.getPackageName(domainType.canonicalName());
    String simpleName = StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(baseDomainType.simpleName(), "Domain")) +
        "BasedOn" + StringFunctions.capitalizeFirstLetter(domainType.simpleName());
    return ClassNameFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getMovableDownwardObjectTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = ClassNameFunctions.getPackageName(domainType.canonicalName());
    String simpleName = "Movable" +
        StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(baseDomainType.simpleName(), "Domain")) +
        "BasedOn" + StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(domainType.simpleName(), "Domain"));
    return ClassNameFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getUnmovableDownwardObjectTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = ClassNameFunctions.getPackageName(domainType.canonicalName());
    String simpleName = "Unmovable" +
        StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(baseDomainType.simpleName(), "Domain")) +
        "BasedOn" + StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(domainType.simpleName(), "Domain"));
    return ClassNameFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getMovableDownwardObjectTypename(Class<?> domainClass, Class<?> baseDomainClass) {
    String packageName = ClassNameFunctions.getPackageName(domainClass.getCanonicalName());
    String simpleName = "Movable" +
        StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(baseDomainClass.getSimpleName(), "Domain")) +
        "BasedOn" + StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(domainClass.getSimpleName(), "Domain"));
    return ClassNameFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getConversionMethodName(CustomTypeReference reference) {
    return getConversionMethodName(reference.targetType());
  }

  static String getConversionMethodName(CustomType targetType) {
    KeyDomain keyDomain = Jaquarius.settings().getKeyDomainByDelegateClass(targetType.canonicalName());
    if (keyDomain != null) {
      return "as" + StringFunctions.capitalizeFirstLetter(targetType.simpleName());
    }
    return "as" + StringFunctions.capitalizeFirstLetter(
        StringFunctions.removeTailOrElseThrow(targetType.simpleName(), "Domain"));
  }

  static boolean isConversionMethod(MethodStatement method) {
    return method.name().length() > 2
        && method.name().startsWith("as")
        && Character.isUpperCase(method.name().charAt(2));
  }

  private static String assignChannelClassCanonicalName(
      String spaceName, CustomType domainType, MethodStatement channelMethod
  ) {
    final String simpleName;
    if (channelMethod.owner().hasAnnotation(Ontology.class)) {
      simpleName = getDefaultChannelClassSimpleName(channelMethod);
    } else {
      simpleName = assumeChannelClassSimpleNameWhenDomainClass(domainType, channelMethod);
    }
    return ClassNameFunctions.joinPackageAndSimpleName(spaceName, simpleName);
  }

  private static String assumeChannelClassSimpleNameWhenDomainClass(
      CustomType domainType, MethodStatement channelMethod
  ) {
    String simpleName = StringFunctions.removeTailOrElseThrow(
        ClassNameFunctions.getSimpleName(domainType.canonicalName()), "Domain"
    );
    if (isMappingTraverseType(channelMethod)) {
      if (isConversionChannel(channelMethod)) {
        simpleName = StringFunctions.capitalizeFirstLetter(simpleName) + "To" +
            channelMethod.name().substring(2) + "Channel";
      } else {
        simpleName = StringFunctions.capitalizeFirstLetter(simpleName) + "To" +
            StringFunctions.capitalizeFirstLetter(channelMethod.name()) + "Channel";
      }
    } else {
      simpleName = StringFunctions.capitalizeFirstLetter(simpleName) +
          StringFunctions.capitalizeFirstLetter(joinMethodNameAndParameterTypes(channelMethod)) + "Channel";
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
        sb.append(StringFunctions.capitalizeFirstLetter(param.type().asPrimitiveReferenceOrElseThrow().primitiveType().typename()));
      } else if (param.type().isCustomTypeReference()) {
        sb.append(param.type().asCustomTypeReferenceOrElseThrow().targetType().simpleName());
      } else if (param.type().isNamedReference()) {
        sb.append(param.type().asNamedReferenceOrElseThrow().name());
      }
    }
    return sb.toString();
  }

  private static boolean isConversionChannel(MethodStatement channelMethod) {
    String name = channelMethod.name();
    return (name.length() > 3 && name.startsWith("as") && Character.isUpperCase(name.charAt(2)));
  }

  static String getDefaultChannelClassSimpleName(MethodStatement channelMethod) {
    return StringFunctions.capitalizeFirstLetter(channelMethod.name()) + "Channel";
  }

  static String getGuideClassCanonicalName(
      ObjectReferenceForm targetForm, String spaceName, CustomType channelType, MethodStatement channelMethod
  ) {
    String name = StringFunctions.replaceTailIfPresent(channelType.canonicalName(), "Channel", "Guide");
    if (ObjectReferenceForms.Primitive.is(targetForm)) {
      name = name + "AsPrimitive";
    } else if (ObjectReferenceForms.PrimitiveWrapper.is(targetForm)) {
      name = name + "AsObject";
    }
    return name;
  }

  static String getObjectProviderCanonicalName(CustomType domainType) {
    String name = StringFunctions.removeTailOrElseThrow(domainType.canonicalName(), "Domain");
    if (name.endsWith("ies")) {
      return name + "s";
    } else if (name.endsWith("es")) {
      return name + "s";
    } else if (name.endsWith("s") || name.endsWith("x") || name.endsWith("z") || name.endsWith("ch") || name.endsWith("sh")) {
      return name + "es";
    } else if (name.endsWith("y")) {
      return name.substring(0, name.length() - 1) + "ies";
    }
    return name + "s";
  }

  static String getObjectProviderBrokerCanonicalName(CustomType domainType) {
    String name = StringFunctions.removeTailOrElseThrow(domainType.canonicalName(), "Domain");
    return name + "sBroker";
  }

  static boolean isPrimitiveTargetForm(MethodStatement method) {
    return method.name().endsWith("AsPrimitive");
  }

  private static boolean isMappingTraverseType(MethodStatement method) {
    return !ChannelFunctions.getTraverseType(method).isMovingBased();
  }

  private static String transformClassName(String className) {
    return className.replace("$", "");
  }
}
