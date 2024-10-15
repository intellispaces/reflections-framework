package intellispaces.framework.core.common;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.base.text.TextFunctions;
import intellispaces.common.base.type.TypeFunctions;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.CustomTypeReference;
import intellispaces.framework.core.annotation.Channel;
import intellispaces.framework.core.annotation.Domain;
import intellispaces.framework.core.annotation.ObjectHandle;
import intellispaces.framework.core.annotation.Ontology;
import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.guide.GuideForms;
import intellispaces.framework.core.object.ObjectFunctions;
import intellispaces.framework.core.object.ObjectHandleTypes;
import intellispaces.framework.core.space.channel.ChannelFunctions;

import java.util.Optional;

public interface NameConventionFunctions {

  static String getObjectHandleTypename(String domainClassName, ObjectHandleTypes handleType) {
    return switch (handleType) {
      case Bunch -> getBunchObjectHandleTypename(domainClassName);
      case Common -> getCommonObjectHandleTypename(domainClassName);
      case Movable -> getMovableObjectHandleTypename(domainClassName);
      case Unmovable -> getUnmovableObjectHandleTypename(domainClassName);
    };
  }

  static String getBunchObjectHandleTypename(String domainClassName) {
    if (ObjectFunctions.isDefaultObjectHandleType(domainClassName)) {
      return domainClassName;
    }
    return TextFunctions.replaceTailOrElseThrow(transformClassName(domainClassName), "Domain", "Bunch");
  }

  static String getCommonObjectHandleTypename(String domainClassName) {
    return TextFunctions.removeTailOrElseThrow(transformClassName(domainClassName), "Domain");
  }

  static String getMovableObjectHandleTypename(String domainClassName) {
    return TypeFunctions.addPrefixToSimpleName("Movable", getCommonObjectHandleTypename(domainClassName));
  }

  static String getUnmovableObjectHandleTypename(String domainClassName) {
    return TypeFunctions.addPrefixToSimpleName("Unmovable", getCommonObjectHandleTypename(domainClassName));
  }

  static String getObjectHandleWrapperCanonicalName(CustomType objectHandleType) {
    Optional<ObjectHandle> oha = objectHandleType.selectAnnotation(ObjectHandle.class);
    if (oha.isPresent() && TextFunctions.isNotBlank(oha.get().name())) {
      return TypeFunctions.replaceSimpleName(objectHandleType.canonicalName(), oha.get().name());
    }
    return objectHandleType.canonicalName() + "Impl";
  }

  static String getObjectHandleWrapperCanonicalName(Class<?> objectHandleClass) {
    ObjectHandle oha = objectHandleClass.getAnnotation(ObjectHandle.class);
    if (oha != null && TextFunctions.isNotBlank(oha.name())) {
      return TypeFunctions.replaceSimpleName(objectHandleClass.getCanonicalName(), oha.name());
    }
    return objectHandleClass.getCanonicalName() + "Impl";
  }

  static String getUnitWrapperCanonicalName(String unitClassName) {
    return transformClassName(unitClassName) + "Wrapper";
  }

  static String getAutoGuiderCanonicalName(String guideClassName) {
    return TextFunctions.replaceSingleOrElseThrow(transformClassName(guideClassName), "Guide", "AutoGuide");
  }

  static String getDataClassName(String domainClassName) {
    return TextFunctions.replaceTailOrElseThrow(transformClassName(domainClassName), "Domain", "Data");
  }

  static String getChannelClassCanonicalName(MethodStatement channelMethod) {
    String spaceName = channelMethod.owner().packageName();
    CustomType owner = channelMethod.owner();
    if (!owner.hasAnnotation(Domain.class) && !owner.hasAnnotation(Ontology.class)) {
      throw UnexpectedViolationException.withMessage("Channel method {0} should be declared " +
              "in domain or ontology class. But actual class {1} is not marked with annotation",
          channelMethod.name(), owner.canonicalName()
      );
    }
    return getChannelClassCanonicalName(spaceName, owner, channelMethod);
  }

  static String getChannelClassCanonicalName(
      String spaceName, CustomType domainType, MethodStatement channelMethod
  ) {
    String channelSimpleName = channelMethod.selectAnnotation(Channel.class).orElseThrow().name();
    if (!channelSimpleName.isBlank()) {
      return TypeFunctions.joinPackageAndSimpleName(spaceName, channelSimpleName);
    }
    return assignChannelClassCanonicalName(spaceName, domainType, channelMethod);
  }

  static String getUnmovableUpwardObjectHandleTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = TypeFunctions.getPackageName(domainType.canonicalName());
    String simpleName = TextFunctions.removeTailOrElseThrow(domainType.simpleName(), "Domain") +
        "BasedOn" +
        TextFunctions.capitalizeFirstLetter(TextFunctions.removeTailOrElseThrow(baseDomainType.simpleName(), "Domain"));
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
    String simpleName = TextFunctions.capitalizeFirstLetter(TextFunctions.removeTailOrElseThrow(baseDomainType.simpleName(), "Domain")) +
        "BasedOn" + TextFunctions.capitalizeFirstLetter(domainType.simpleName());
    return TypeFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getMovableDownwardObjectHandleTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = TypeFunctions.getPackageName(domainType.canonicalName());
    String simpleName = "Movable" +
        TextFunctions.capitalizeFirstLetter(TextFunctions.removeTailOrElseThrow(baseDomainType.simpleName(), "Domain")) +
        "BasedOn" + TextFunctions.capitalizeFirstLetter(TextFunctions.removeTailOrElseThrow(domainType.simpleName(), "Domain"));
    return TypeFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getUnmovableDownwardObjectHandleTypename(CustomType domainType, CustomType baseDomainType) {
    String packageName = TypeFunctions.getPackageName(domainType.canonicalName());
    String simpleName = "Unmovable" +
        TextFunctions.capitalizeFirstLetter(TextFunctions.removeTailOrElseThrow(baseDomainType.simpleName(), "Domain")) +
        "BasedOn" + TextFunctions.capitalizeFirstLetter(domainType.simpleName());
    return TypeFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getMovableDownwardObjectHandleTypename(Class<?> domainClass, Class<?> baseDomainClass) {
    String packageName = TypeFunctions.getPackageName(domainClass.getCanonicalName());
    String simpleName = "Movable" +
        TextFunctions.capitalizeFirstLetter(TextFunctions.removeTailOrElseThrow(baseDomainClass.getSimpleName(), "Domain")) +
        "BasedOn" + TextFunctions.capitalizeFirstLetter(TextFunctions.removeTailOrElseThrow(domainClass.getSimpleName(), "Domain"));
    return TypeFunctions.joinPackageAndSimpleName(packageName, simpleName);
  }

  static String getConversionMethodName(CustomTypeReference reference) {
    return getConversionMethodName(reference.targetType());
  }

  static String getConversionMethodName(CustomType targetType) {
    return "as" + TextFunctions.capitalizeFirstLetter(
        TextFunctions.removeTailOrElseThrow(targetType.simpleName(), "Domain"));
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
    return TypeFunctions.joinPackageAndSimpleName(spaceName, simpleName);
  }

  private static String assumeChannelClassSimpleNameWhenDomainClass(
      CustomType domainType, MethodStatement channelMethod
  ) {
    String simpleName = TextFunctions.removeTailOrElseThrow(
        TypeFunctions.getSimpleName(domainType.canonicalName()), "Domain"
    );
    if (isMappingTraverseType(channelMethod)) {
      if (isTransformChannel(channelMethod)) {
        simpleName = TextFunctions.capitalizeFirstLetter(simpleName) + "To" +
            channelMethod.name().substring(2) + "Channel";
      } else {
        simpleName = TextFunctions.capitalizeFirstLetter(simpleName) + "To" +
            TextFunctions.capitalizeFirstLetter(channelMethod.name()) + "Channel";
      }
    } else {
      simpleName = TextFunctions.capitalizeFirstLetter(simpleName) +
          TextFunctions.capitalizeFirstLetter(joinMethodNameAndParameterTypes(channelMethod)) + "Channel";
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

  private static boolean isTransformChannel(MethodStatement channelMethod) {
    String name = channelMethod.name();
    return (name.length() > 3 && name.startsWith("as") && Character.isUpperCase(name.charAt(2)));
  }

  static String getDefaultChannelClassSimpleName(MethodStatement channelMethod) {
    return TextFunctions.capitalizeFirstLetter(channelMethod.name()) + "Channel";
  }

  static String getGuideClassCanonicalName(
      GuideForm guideForm, String spaceName, CustomType domainType, MethodStatement channelMethod
  ) {
    String channelCanonicalName = getChannelClassCanonicalName(spaceName, domainType, channelMethod);
    String name = TextFunctions.replaceLast(channelCanonicalName, "Channel", "Guide");
    if (guideForm == GuideForms.Primitive) {
      name = name + "Primitive";
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
