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
import tech.intellispaces.reflections.framework.settings.DomainAssignments;
import tech.intellispaces.reflections.framework.settings.DomainReference;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;

public interface NameConventionFunctions {

  static String convertToChannelClassName(String channelName) {
    return channelName + CHANNEL;
  }

  static String getDomainNameOfRegularObjectForm(String objectCanonicalName) {
    String simpleName = ClassNameFunctions.getSimpleName(objectCanonicalName);
    simpleName = StringFunctions.removeHeadIfPresent(simpleName, MOVABLE);
    simpleName = simpleName + DOMAIN;
    return ClassNameFunctions.replaceSimpleName(objectCanonicalName, simpleName);
  }

  static String getObjectTypename(
        String domainClassName, ReflectionForm objectForm, MovabilityType movabilityType, boolean replaceDomainWithDelegate
  ) {
    return switch (ReflectionForms.of(objectForm)) {
      case Reflection -> switch (MovabilityTypes.of(movabilityType)) {
        case General -> getGeneralReflectionTypeName(domainClassName, replaceDomainWithDelegate);
        case Movable -> getMovableReflectionTypeName(domainClassName, replaceDomainWithDelegate);
      };
      case Primitive -> throw NotImplementedExceptions.withCode("LXc75Q");
      case PrimitiveWrapper -> throw NotImplementedExceptions.withCode("XGDuuQ");
    };
  }

  static String getGeneralReflectionTypeName(String domainClassName) {
    return getGeneralReflectionTypeName(domainClassName, false);
  }

  static String getGeneralReflectionTypeName(String domainClassName, boolean replaceDomainWithDelegate) {
    DomainReference domain = ReflectionsNodeFunctions.ontologyReference().getDomainByClassName(domainClassName);
    if (replaceDomainWithDelegate) {
      if (domain != null && domain.delegateClassName() != null) {
        return domain.delegateClassName();
      }
    } else {
      if (domain != null) {
        domainClassName = domain.classCanonicalName();
      }
    }
    return StringFunctions.removeTailOrElseThrow(transformClassName(domainClassName), DOMAIN);
  }

  static String getUnmovableReflectionTypeName(String domainClassName) {
    return StringFunctions.removeTailOrElseThrow(transformClassName(domainClassName), DOMAIN);
  }

  static String getMovableRegularFormTypeName(String domainClassName, boolean replaceDomainWithDelegate) {
    if (replaceDomainWithDelegate) {
      DomainReference domain = ReflectionsNodeFunctions.ontologyReference().getDomainByClassName(domainClassName);
      if (domain != null && domain.delegateClassName() != null && (
          DomainAssignments.Number.is(domain.assignment()) ||
              DomainAssignments.Short.is(domain.assignment()) ||
              DomainAssignments.Integer.is(domain.assignment()) ||
              DomainAssignments.Float.is(domain.assignment()) ||
              DomainAssignments.Double.is(domain.assignment())
      )) {
        return domain.delegateClassName();
      }
    }
    return ClassNameFunctions.addPrefixToSimpleName(MOVABLE,
        StringFunctions.removeTailOrElseThrow(transformClassName(domainClassName), DOMAIN));
  }

  static String getUnmovableReflectionTypeName(String domainClassName, boolean replaceDomainWithDelegate) {
    if (replaceDomainWithDelegate) {
      DomainReference domain = ReflectionsNodeFunctions.ontologyReference().getDomainByClassName(domainClassName);
      if (domain != null && domain.delegateClassName() != null && (
          DomainAssignments.Number.is(domain.assignment()) ||
              DomainAssignments.Short.is(domain.assignment()) ||
              DomainAssignments.Integer.is(domain.assignment()) ||
              DomainAssignments.Float.is(domain.assignment()) ||
              DomainAssignments.Double.is(domain.assignment())
      )) {
        return domain.delegateClassName();
      }
    }
    return getGeneralReflectionTypeName(domainClassName, false);
  }

  static String getMovableReflectionTypeName(String domainClassName, boolean replaceDomainWithDelegate) {
    if (replaceDomainWithDelegate) {
      DomainReference domain = ReflectionsNodeFunctions.ontologyReference().getDomainByClassName(domainClassName);
      if (domain != null && domain.delegateClassName() != null && (
          DomainAssignments.Number.is(domain.assignment()) ||
              DomainAssignments.Short.is(domain.assignment()) ||
              DomainAssignments.Integer.is(domain.assignment()) ||
              DomainAssignments.Float.is(domain.assignment()) ||
              DomainAssignments.Double.is(domain.assignment())
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

  static String getActionGuideImplementationCanonicalName(String guideClassName) {
    return StringFunctions.replaceSingleOrElseThrow(transformClassName(guideClassName), GUIDE, ACTION_GUIDE);
  }

  static String getUnmovableDatasetClassName(String domainClassName) {
    return StringFunctions.replaceTailOrElseThrow(transformClassName(domainClassName), DOMAIN, DATASET);
  }

  static String getDatasetBuilderCanonicalName(String domainClassName) {
    return StringFunctions.replaceTailOrElseThrow(transformClassName(domainClassName), DOMAIN, BUILDER);
  }

  static String getReflectionAdapterClassName(String domainClassName) {
    return StringFunctions.replaceTailOrElseThrow(transformClassName(domainClassName), DOMAIN, ADAPTER);
  }

  static String getChannelClassCanonicalName(MethodStatement channelMethod) {
    String spaceName = channelMethod.owner().packageName();
    CustomType owner = channelMethod.owner();
    if (!owner.hasAnnotation(Domain.class) && !owner.hasAnnotation(Ontology.class)) {
      throw UnexpectedExceptions.withMessage("ReflectionChannel method {0} should be declared " +
              "in domain or ontology class. But actual class {1} is not marked with annotation",
          channelMethod.name(), owner.canonicalName()
      );
    }
    return getChannelClassCanonicalName(spaceName, owner, channelMethod);
  }

  static String getChannelClassCanonicalName(
      String spaceName, CustomType sourceDomain, MethodStatement channelMethod
  ) {
    String channelSimpleName = channelMethod.selectAnnotation(Channel.class).orElseThrow().name();
    if (!channelSimpleName.isBlank()) {
      return ClassNameFunctions.joinPackageAndSimpleName(spaceName, channelSimpleName);
    }
    return assignChannelClassCanonicalName(spaceName, sourceDomain, channelMethod);
  }

  static String getObjectFactoryWrapperClassName(String objectFactoryClassName) {
    return transformClassName(objectFactoryClassName) + WRAPPER;
  }

  static String getFactoriesResourceName() {
    return "META-INF/reflections/factories";
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
      String spaceName, CustomType sourceDomain, MethodStatement channelMethod
  ) {
    final String simpleName;
    if (channelMethod.owner().hasAnnotation(Ontology.class)) {
      simpleName = getDefaultChannelClassSimpleName(channelMethod);
    } else {
      simpleName = assumeChannelClassSimpleNameWhenDomainClass(sourceDomain, channelMethod);
    }
    return ClassNameFunctions.joinPackageAndSimpleName(spaceName, simpleName);
  }

  private static String assumeChannelClassSimpleNameWhenDomainClass(
      CustomType sourceDomain, MethodStatement channelMethod
  ) {
    String simpleName = StringFunctions.removeTailOrElseThrow(
        ClassNameFunctions.getSimpleName(sourceDomain.canonicalName()), DOMAIN
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
    String originArtifactName = originArtifact.canonicalName();
    return getCustomizerCanonicalName(originArtifactName, targetArtifactType);
  }

  static String getCustomizerCanonicalName(String originArtifactName, ArtifactType targetArtifactType) {
    String name = StringFunctions.removeTailOrElseThrow(originArtifactName, DOMAIN);
    return switch (ArtifactTypes.of(targetArtifactType)) {
      case Reflection -> name + REFLECTION + CUSTOMIZER;
      case MovableReflection -> name + MOVABLE_REFLECTION + CUSTOMIZER;
      case ObjectAssistant -> name + ASSISTANT + CUSTOMIZER;
      default -> name;
    };
  }

  String DOMAIN = "Domain";
  String CHANNEL = "Channel";
  String MOVABLE = "Movable";
  String REFLECTION = "Reflection";
  String MOVABLE_REFLECTION = "Unmovable" + REFLECTION;
  String WRAPPER = "Wrapper";
  String GUIDE = "Guide";
  String ACTION_GUIDE = "ActionGuide";
  String DATASET = "Dataset";
  String ADAPTER = "Adapter";
  String BUILDER = "Builder";
  String CUSTOMIZER = "Customizer";
  String ASSISTANT = "Assistant";
}
