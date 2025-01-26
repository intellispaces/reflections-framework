package tech.intellispaces.jaquarius.naming;

import tech.intellispaces.general.exception.UnexpectedExceptions;
import tech.intellispaces.general.text.StringFunctions;
import tech.intellispaces.general.type.ClassNameFunctions;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.Domain;
import tech.intellispaces.jaquarius.annotation.ObjectHandle;
import tech.intellispaces.jaquarius.annotation.Ontology;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleType;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleTypes;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.space.channel.ChannelFunctions;
import tech.intellispaces.jaquarius.space.domain.BasicDomain;
import tech.intellispaces.jaquarius.space.domain.BasicDomains;
import tech.intellispaces.java.reflection.customtype.CustomType;
import tech.intellispaces.java.reflection.method.MethodParam;
import tech.intellispaces.java.reflection.method.MethodStatement;
import tech.intellispaces.java.reflection.reference.CustomTypeReference;

import java.util.Optional;

public interface NameConventionFunctions {

  static String convertIntelliSpacesDomainName(String name) {
    return StringFunctions.replaceHeadOrElseThrow(name, "intellispaces.", "tech.intellispaces.jaquarius.") + "Domain";
  }

  static String convertJaquariusDomainName(String name) {
    return StringFunctions.removeTailIfPresent(
        StringFunctions.replaceHeadIfPresent(name, "tech.intellispaces.jaquarius.", "intellispaces."), "Domain");
  }

  static String getObjectHandleTypename(String domainClassName, ObjectHandleType handleType) {
    return switch (ObjectHandleTypes.from(handleType)) {
      case General -> getGeneralObjectHandleTypename(domainClassName);
      case Movable -> getMovableObjectHandleTypename(domainClassName);
      case Unmovable -> getUnmovableObjectHandleTypename(domainClassName);
    };
  }

  static String getGeneralObjectHandleTypename(String domainClassName) {
    BasicDomain basicDomain = BasicDomains.active().getByDomainName(convertJaquariusDomainName(domainClassName));
    if (basicDomain != null) {
      return basicDomain.delegateClassName();
    }
    return StringFunctions.replaceTailOrElseThrow(transformClassName(domainClassName), "Domain", "Handle");
  }

  static String getUnmovableObjectHandleTypename(String domainClassName) {
    BasicDomain basicDomain = BasicDomains.active().getByDomainName(convertJaquariusDomainName(domainClassName));
    if (basicDomain != null) {
      return basicDomain.delegateClassName();
    }
    return ClassNameFunctions.addPrefixToSimpleName("Unmovable", getGeneralObjectHandleTypename(domainClassName));
  }

  static String getMovableObjectHandleTypename(String domainClassName) {
    return ClassNameFunctions.addPrefixToSimpleName("Movable", getGeneralObjectHandleTypename(domainClassName));
  }

  static String getObjectHandleWrapperCanonicalName(CustomType objectHandleType) {
    Optional<ObjectHandle> oha = objectHandleType.selectAnnotation(ObjectHandle.class);
    if (oha.isPresent() && StringFunctions.isNotBlank(oha.get().name())) {
      return ClassNameFunctions.replaceSimpleName(objectHandleType.canonicalName(), oha.get().name());
    }
    return objectHandleType.canonicalName() + "Impl";
  }

  static String getObjectHandleWrapperCanonicalName(Class<?> objectHandleClass) {
    ObjectHandle oha = objectHandleClass.getAnnotation(ObjectHandle.class);
    if (oha != null && StringFunctions.isNotBlank(oha.name())) {
      return ClassNameFunctions.replaceSimpleName(objectHandleClass.getCanonicalName(), oha.name());
    }
    return objectHandleClass.getCanonicalName() + "Impl";
  }

  static String getUnitWrapperCanonicalName(String unitClassName) {
    return transformClassName(unitClassName) + "Wrapper";
  }

  static String getAutoGuiderCanonicalName(String guideClassName) {
    return StringFunctions.replaceSingleOrElseThrow(transformClassName(guideClassName), "Guide", "AutoGuide");
  }

  static String getDataClassName(String domainClassName) {
    return StringFunctions.replaceTailOrElseThrow(transformClassName(domainClassName), "Domain", "Data");
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

  static String getUnmovableUpwardObjectHandleTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = ClassNameFunctions.getPackageName(domainType.canonicalName());
    String simpleName = StringFunctions.removeTailOrElseThrow(domainType.simpleName(), "Domain") +
        "BasedOn" +
        StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(baseDomainType.simpleName(), "Domain"));
    return ClassNameFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getDownwardObjectHandleTypename(
      CustomType domainType, CustomType baseDomainType, ObjectHandleType handleType
  ) {
    return switch (ObjectHandleTypes.from(handleType)) {
      case General -> getBaseDownwardObjectHandleTypename(domainType, baseDomainType);
      case Movable -> getMovableDownwardObjectHandleTypename(domainType, baseDomainType);
      case Unmovable -> getUnmovableDownwardObjectHandleTypename(domainType, baseDomainType);
    };
  }

  static String getBaseDownwardObjectHandleTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = ClassNameFunctions.getPackageName(domainType.canonicalName());
    String simpleName = StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(baseDomainType.simpleName(), "Domain")) +
        "BasedOn" + StringFunctions.capitalizeFirstLetter(domainType.simpleName());
    return ClassNameFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getMovableDownwardObjectHandleTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = ClassNameFunctions.getPackageName(domainType.canonicalName());
    String simpleName = "Movable" +
        StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(baseDomainType.simpleName(), "Domain")) +
        "BasedOn" + StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(domainType.simpleName(), "Domain"))
        + "Handle";
    return ClassNameFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getUnmovableDownwardObjectHandleTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = ClassNameFunctions.getPackageName(domainType.canonicalName());
    String simpleName = "Unmovable" +
        StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(baseDomainType.simpleName(), "Domain")) +
        "BasedOn" + StringFunctions.capitalizeFirstLetter(StringFunctions.removeTailOrElseThrow(domainType.simpleName(), "Domain"))
        + "Handle";
    return ClassNameFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getMovableDownwardObjectHandleTypename(Class<?> domainClass, Class<?> baseDomainClass) {
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
    BasicDomain basicDomain = BasicDomains.active().getByDelegateClassName(targetType.canonicalName());
    if (basicDomain != null) {
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
    }
    return name;
  }

  private static boolean isMappingTraverseType(MethodStatement method) {
    return !ChannelFunctions.getTraverseType(method).isMovingBased();
  }

  private static String transformClassName(String className) {
    return className.replace("$", "");
  }
}
