package tech.intellispaces.reflections.framework.naming;

import java.util.Optional;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.commons.type.ClassNameFunctions;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.method.MethodParam;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.javareflection.reference.CustomTypeReference;
import tech.intellispaces.reflections.framework.ArtifactType;
import tech.intellispaces.reflections.framework.annotation.Channel;
import tech.intellispaces.reflections.framework.annotation.Domain;
import tech.intellispaces.reflections.framework.annotation.Ontology;
import tech.intellispaces.reflections.framework.annotation.Reflection;
import tech.intellispaces.reflections.framework.artifact.ArtifactTypes;
import tech.intellispaces.reflections.framework.node.ReflectionsNodeFunctions;
import tech.intellispaces.reflections.framework.reflection.MovabilityType;
import tech.intellispaces.reflections.framework.reflection.MovabilityTypes;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.reflections.framework.settings.DomainReference;
import tech.intellispaces.reflections.framework.settings.DomainTypes;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;

public interface NameConventionFunctions {

  static boolean isDomainName(String canonicalName) {
    return canonicalName.endsWith(DOMAIN);
  }

  static String convertToDomainClassName(String domainName) {
    return domainName + DOMAIN;
  }

  static String convertToDomainName(String domainClassName) {
    return StringFunctions.removeTailIfPresent(domainClassName, DOMAIN);
  }

  static String convertToChannelClassName(String channelName) {
    return channelName + CHANNEL;
  }

  static String getDomainNameOfRegularObjectForm(String objectCanonicalName) {
    String simpleName = ClassNameFunctions.getSimpleName(objectCanonicalName);
    simpleName = StringFunctions.removeHeadIfPresent(simpleName, UNMOVABLE);
    simpleName = StringFunctions.removeHeadIfPresent(simpleName, MOVABLE);
    simpleName = simpleName + DOMAIN;
    return ClassNameFunctions.replaceSimpleName(objectCanonicalName, simpleName);
  }

  static String getObjectTypename(
          String domainClassName, ReflectionForm objectForm, MovabilityType movabilityType, boolean replaceDomainWithDelegate
  ) {
    return switch (ReflectionForms.of(objectForm)) {
      case Regular -> switch (MovabilityTypes.of(movabilityType)) {
        case General -> getGeneralRegularFormClassname(domainClassName, replaceDomainWithDelegate);
        case Movable -> getMovableRegularFormTypeName(domainClassName, replaceDomainWithDelegate);
        case Unmovable -> getUnmovableRegularFormTypeName(domainClassName, replaceDomainWithDelegate);
      };
      case Reflection -> switch (MovabilityTypes.of(movabilityType)) {
        case General -> getGeneralReflectionTypeName(domainClassName, replaceDomainWithDelegate);
        case Movable -> getMovableReflectionTypeName(domainClassName, replaceDomainWithDelegate);
        case Unmovable -> getUnmovableReflectionTypeName(domainClassName, replaceDomainWithDelegate);
      };
      case Primitive -> throw NotImplementedExceptions.withCode("LXc75Q");
      case PrimitiveWrapper -> throw NotImplementedExceptions.withCode("XGDuuQ");
    };
  }

  static String getGeneralRegularFormClassname(String domainClassName, boolean replaceDomainWithDelegate) {
    if (replaceDomainWithDelegate) {
      String domainName = convertToDomainName(domainClassName);
      DomainReference domain = ReflectionsNodeFunctions.ontologyReference().getDomainByName(domainName);
      if (domain != null) {
        return domain.delegateClassName() != null ? domain.delegateClassName() : domainName;
      }
    } else {
      DomainReference domain = ReflectionsNodeFunctions.ontologyReference().getDomainByName(domainClassName);
      if (domain != null) {
        return domain.domainName();
      }
    }
    return StringFunctions.removeTailOrElseThrow(transformClassName(domainClassName), DOMAIN);
  }

  static String getGeneralReflectionTypeName(String domainClassName, boolean replaceDomainWithDelegate) {
    if (replaceDomainWithDelegate) {
      String domainName = convertToDomainName(domainClassName);
      DomainReference domain = ReflectionsNodeFunctions.ontologyReference().getDomainByName(domainName);
      if (domain != null) {
        return domain.delegateClassName() != null ? domain.delegateClassName() : domainName;
      }
    } else {
      DomainReference domain = ReflectionsNodeFunctions.ontologyReference().getDomainByName(domainClassName);
      if (domain != null) {
        return domain.domainName();
      }
    }
    return StringFunctions.replaceTailOrElseThrow(transformClassName(domainClassName), DOMAIN, REFLECTION);
  }

  static String getUnmovableReflectionTypeName(String domainClassName) {
    return ClassNameFunctions.addPrefixToSimpleName(UNMOVABLE,
        StringFunctions.replaceTailOrElseThrow(transformClassName(domainClassName), DOMAIN, REFLECTION));
  }

  static String getUnmovableRegularFormTypeName(String domainClassName, boolean replaceDomainWithDelegate) {
    if (replaceDomainWithDelegate) {
      DomainReference domain = ReflectionsNodeFunctions.ontologyReference().getDomainByName(convertToDomainName(domainClassName));
      if (domain != null && domain.delegateClassName() != null && (
          DomainTypes.Number.is(domain.type()) ||
              DomainTypes.Short.is(domain.type()) ||
              DomainTypes.Integer.is(domain.type()) ||
              DomainTypes.Float.is(domain.type()) ||
              DomainTypes.Double.is(domain.type())
      )) {
        return domain.delegateClassName();
      }
    }
    return ClassNameFunctions.addPrefixToSimpleName(UNMOVABLE,
        StringFunctions.removeTailOrElseThrow(transformClassName(domainClassName), DOMAIN));
  }

  static String getMovableRegularFormTypeName(String domainClassName, boolean replaceDomainWithDelegate) {
    if (replaceDomainWithDelegate) {
      DomainReference domain = ReflectionsNodeFunctions.ontologyReference().getDomainByName(convertToDomainName(domainClassName));
      if (domain != null && domain.delegateClassName() != null && (
          DomainTypes.Number.is(domain.type()) ||
              DomainTypes.Short.is(domain.type()) ||
              DomainTypes.Integer.is(domain.type()) ||
              DomainTypes.Float.is(domain.type()) ||
              DomainTypes.Double.is(domain.type())
      )) {
        return domain.delegateClassName();
      }
    }
    return ClassNameFunctions.addPrefixToSimpleName(MOVABLE,
        StringFunctions.removeTailOrElseThrow(transformClassName(domainClassName), DOMAIN));
  }

  static String getUnmovableReflectionTypeName(String domainClassName, boolean replaceDomainWithDelegate) {
    if (replaceDomainWithDelegate) {
      DomainReference domain = ReflectionsNodeFunctions.ontologyReference().getDomainByName(convertToDomainName(domainClassName));
      if (domain != null && domain.delegateClassName() != null && (
          DomainTypes.Number.is(domain.type()) ||
              DomainTypes.Short.is(domain.type()) ||
              DomainTypes.Integer.is(domain.type()) ||
              DomainTypes.Float.is(domain.type()) ||
              DomainTypes.Double.is(domain.type())
      )) {
        return domain.delegateClassName();
      }
    }
    return ClassNameFunctions.addPrefixToSimpleName(UNMOVABLE, getGeneralReflectionTypeName(domainClassName, false));
  }

  static String getMovableReflectionTypeName(String domainClassName, boolean replaceDomainWithDelegate) {
    if (replaceDomainWithDelegate) {
      DomainReference domain = ReflectionsNodeFunctions.ontologyReference().getDomainByName(convertToDomainName(domainClassName));
      if (domain != null && domain.delegateClassName() != null && (
          DomainTypes.Number.is(domain.type()) ||
              DomainTypes.Short.is(domain.type()) ||
              DomainTypes.Integer.is(domain.type()) ||
              DomainTypes.Float.is(domain.type()) ||
              DomainTypes.Double.is(domain.type())
      )) {
        return domain.delegateClassName();
      }
    }
    return ClassNameFunctions.addPrefixToSimpleName(MOVABLE, getGeneralReflectionTypeName(domainClassName, false));
  }

  static String getReflectionWrapperCanonicalName(CustomType reflectionType) {
    Optional<Reflection> oha = reflectionType.selectAnnotation(Reflection.class);
    if (oha.isPresent() && StringFunctions.isNotBlank(oha.get().name())) {
      return ClassNameFunctions.replaceSimpleName(reflectionType.canonicalName(), oha.get().name());
    }
    return reflectionType.canonicalName() + WRAPPER;
  }

  static String getReflectionWrapperCanonicalName(Class<?> reflectionClass) {
    Reflection oha = reflectionClass.getAnnotation(Reflection.class);
    if (oha != null && StringFunctions.isNotBlank(oha.name())) {
      return ClassNameFunctions.replaceSimpleName(reflectionClass.getCanonicalName(), oha.name());
    }
    return reflectionClass.getCanonicalName() + WRAPPER;
  }

  static String getUnitWrapperCanonicalName(String unitClassName) {
    return transformClassName(unitClassName) + WRAPPER;
  }

  static String getAutoGuideCanonicalName(String guideClassName) {
    return StringFunctions.replaceSingleOrElseThrow(transformClassName(guideClassName), GUIDE, AUTO_GUIDE);
  }

  static String getUnmovableDatasetClassName(String domainClassName) {
    return ClassNameFunctions.addPrefixToSimpleName(UNMOVABLE,
        StringFunctions.replaceTailOrElseThrow(transformClassName(domainClassName), DOMAIN, DATASET));
  }

  static String getDatasetBuilderCanonicalName(String domainClassName) {
    return StringFunctions.replaceTailOrElseThrow(transformClassName(domainClassName), DOMAIN, BUILDER);
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

  static String getObjectFactoryWrapperClassName(String objectFactoryClassName) {
    return transformClassName(objectFactoryClassName) + WRAPPER;
  }

  static String getFactoriesResourceName() {
    return "META-INF/reflections/factories";
  }

  static String getUnmovableUpwardObjectTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = ClassNameFunctions.getPackageName(domainType.canonicalName());
    String simpleName = StringFunctions.removeTailOrElseThrow(domainType.simpleName(), DOMAIN) +
        "BasedOn" +
        StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(baseDomainType.simpleName(), DOMAIN));
    return ClassNameFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getDownwardReflectionTypename(
      CustomType domainType, CustomType baseDomainType, MovabilityType movabilityType
  ) {
    return switch (MovabilityTypes.of(movabilityType)) {
      case General -> getGeneralDownwardObjectTypename(domainType, baseDomainType);
      case Unmovable -> getUnmovableDownwardObjectTypename(domainType, baseDomainType);
      case Movable -> getMovableDownwardObjectTypename(domainType, baseDomainType);
    };
  }

  static String getGeneralDownwardObjectTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = ClassNameFunctions.getPackageName(domainType.canonicalName());
    String simpleName = StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(baseDomainType.simpleName(), DOMAIN)) +
        "BasedOn" + StringFunctions.capitalizeFirstLetter(domainType.simpleName());
    return ClassNameFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getMovableDownwardObjectTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = ClassNameFunctions.getPackageName(domainType.canonicalName());
    String simpleName = MOVABLE +
        StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(baseDomainType.simpleName(), DOMAIN)) +
        "BasedOn" + StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(domainType.simpleName(), DOMAIN));
    return ClassNameFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getUnmovableDownwardObjectTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = ClassNameFunctions.getPackageName(domainType.canonicalName());
    String simpleName = UNMOVABLE +
        StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(baseDomainType.simpleName(), DOMAIN)) +
        "BasedOn" + StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(domainType.simpleName(), DOMAIN));
    return ClassNameFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getMovableDownwardObjectTypename(Class<?> domainClass, Class<?> baseDomainClass) {
    String packageName = ClassNameFunctions.getPackageName(domainClass.getCanonicalName());
    String simpleName = MOVABLE +
        StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(baseDomainClass.getSimpleName(), DOMAIN)) +
        "BasedOn" + StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(domainClass.getSimpleName(), DOMAIN));
    return ClassNameFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getConversionMethodName(CustomTypeReference reference) {
    return getConversionMethodName(reference.targetType());
  }

  static String getConversionMethodName(CustomType targetType) {
    DomainReference domain = ReflectionsNodeFunctions.ontologyReference().getDomainByDelegateClass(targetType.canonicalName());
    if (domain != null) {
      return "as" + StringFunctions.capitalizeFirstLetter(targetType.simpleName());
    }
    return "as" + StringFunctions.capitalizeFirstLetter(
        StringFunctions.removeTailOrElseThrow(targetType.simpleName(), DOMAIN));
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
        ClassNameFunctions.getSimpleName(domainType.canonicalName()), DOMAIN
    );
    if (isMappingTraverseType(channelMethod)) {
      if (isConversionChannel(channelMethod)) {
        simpleName = StringFunctions.capitalizeFirstLetter(simpleName) + "To" +
            channelMethod.name().substring(2) + CHANNEL;
      } else {
        simpleName = StringFunctions.capitalizeFirstLetter(simpleName) + "To" +
            StringFunctions.capitalizeFirstLetter(channelMethod.name()) + CHANNEL;
      }
    } else {
      simpleName = StringFunctions.capitalizeFirstLetter(simpleName) +
          StringFunctions.capitalizeFirstLetter(joinMethodNameAndParameterTypes(channelMethod)) + CHANNEL;
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
    return StringFunctions.capitalizeFirstLetter(channelMethod.name()) + CHANNEL;
  }

  static String getGuideClassCanonicalName(
          ReflectionForm targetForm, String spaceName, CustomType channelType, MethodStatement channelMethod
  ) {
    String name = StringFunctions.replaceTailIfPresent(channelType.canonicalName(), CHANNEL, GUIDE);
    if (ReflectionForms.Primitive.is(targetForm)) {
      name = name + "AsPrimitive";
    } else if (ReflectionForms.PrimitiveWrapper.is(targetForm)) {
      name = name + "AsObject";
    }
    return name;
  }

  static String getObjectAssistantCanonicalName(CustomType domainType) {
    String name = StringFunctions.removeTailOrElseThrow(domainType.canonicalName(), DOMAIN);
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

  static String getObjectAssistantHandleCanonicalName(CustomType domainType) {
    String name = StringFunctions.removeTailOrElseThrow(domainType.canonicalName(), DOMAIN);
    return name + "sHandle";
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

  static String getCustomizerCanonicalName(CustomType originArtifact, ArtifactType targetArtifactType) {
    final String name = StringFunctions.removeTailOrElseThrow(originArtifact.canonicalName(), DOMAIN);
    if (isDomainName(originArtifact.canonicalName())) {
      return switch (ArtifactTypes.of(targetArtifactType)) {
        case RegularObject -> name + CUSTOMIZER;
        case MovableRegularObject -> ClassNameFunctions.addPrefixToSimpleName(MOVABLE, name) + CUSTOMIZER;
        case UnmovableRegularObject -> ClassNameFunctions.addPrefixToSimpleName(UNMOVABLE, name) + CUSTOMIZER;
        case Reflection -> name + REFLECTION + CUSTOMIZER;
        case MovableReflection -> name + MOVABLE_REFLECTION + CUSTOMIZER;
        case UnmovableReflection -> name + UNMOVABLE_REFLECTION + CUSTOMIZER;
        case ObjectAssistant -> name + ASSISTANT + CUSTOMIZER;
        default -> name;
      };
    }
    throw NotImplementedExceptions.withCodeAndMessage("9kDR9g", "Origin artifact {0}, target artifact type {}",
        originArtifact.canonicalName(), targetArtifactType.name());
  }

  String DOMAIN = "Domain";
  String CHANNEL = "Channel";
  String UNMOVABLE = "Unmovable";
  String MOVABLE = "Movable";
  String REFLECTION = "Reflection";
  String MOVABLE_REFLECTION = MOVABLE + REFLECTION;
  String UNMOVABLE_REFLECTION = UNMOVABLE + REFLECTION;
  String WRAPPER = "Wrapper";
  String GUIDE = "Guide";
  String AUTO_GUIDE = "AutoGuide";
  String DATASET = "Dataset";
  String BUILDER = "Builder";
  String CUSTOMIZER = "Customizer";
  String ASSISTANT = "Assistant";
}
