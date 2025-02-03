package tech.intellispaces.jaquarius.space.channel;

import tech.intellispaces.commons.base.exception.NotImplementedExceptions;
import tech.intellispaces.commons.base.exception.UnexpectedExceptions;
import tech.intellispaces.commons.base.text.StringFunctions;
import tech.intellispaces.commons.java.reflection.customtype.CustomType;
import tech.intellispaces.commons.java.reflection.instance.AnnotationInstance;
import tech.intellispaces.commons.java.reflection.instance.ClassInstance;
import tech.intellispaces.commons.java.reflection.instance.Instance;
import tech.intellispaces.commons.java.reflection.method.MethodParam;
import tech.intellispaces.commons.java.reflection.method.MethodSignature;
import tech.intellispaces.commons.java.reflection.method.MethodSignatures;
import tech.intellispaces.commons.java.reflection.method.MethodStatement;
import tech.intellispaces.commons.java.reflection.reference.CustomTypeReference;
import tech.intellispaces.commons.java.reflection.reference.CustomTypeReferences;
import tech.intellispaces.commons.java.reflection.reference.TypeReference;
import tech.intellispaces.commons.java.reflection.reference.TypeReferenceFunctions;
import tech.intellispaces.commons.proxy.tracker.Tracker;
import tech.intellispaces.commons.proxy.tracker.TrackerFunctions;
import tech.intellispaces.commons.proxy.tracker.Trackers;
import tech.intellispaces.jaquarius.annotation.Channel;
import tech.intellispaces.jaquarius.annotation.Guide;
import tech.intellispaces.jaquarius.annotation.Mapper;
import tech.intellispaces.jaquarius.annotation.MapperOfMoving;
import tech.intellispaces.jaquarius.annotation.Mover;
import tech.intellispaces.jaquarius.channel.Channel0;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.channel.Channel2;
import tech.intellispaces.jaquarius.channel.Channel3;
import tech.intellispaces.jaquarius.channel.Channel4;
import tech.intellispaces.jaquarius.channel.ChannelFunction0;
import tech.intellispaces.jaquarius.channel.ChannelFunction1;
import tech.intellispaces.jaquarius.exception.ConfigurationExceptions;
import tech.intellispaces.jaquarius.id.RepetableUuidIdentifierGenerator;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleFunctions;
import tech.intellispaces.jaquarius.space.domain.DomainFunctions;
import tech.intellispaces.jaquarius.traverse.TraverseType;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Channel related functions.
 */
public interface ChannelFunctions {

  static boolean isChannelClass(Class<?> aClass) {
    return aClass.isAnnotationPresent(Channel.class);
  }

  static MethodSignature getChannelMethod(Class<?> channelClass) {
    if (!isChannelClass(channelClass)) {
      throw UnexpectedExceptions.withMessage("Expected channel class. Actual class {0} is not channel class",
          channelClass.getCanonicalName());
    }
    Method[] methods = channelClass.getDeclaredMethods();
    if (methods.length != 1) {
      throw UnexpectedExceptions.withMessage("Expected one channel method. Check channel class {0}",
          channelClass.getCanonicalName());
    }
    return MethodSignatures.get(methods[0]);
  }

  static boolean isChannelMethod(MethodStatement method) {
    return method.hasAnnotation(Channel.class);
  }

  static String getCid(MethodStatement channelMethod) {
    return channelMethod.selectAnnotation(Channel.class).orElseThrow().value();
  }

  static String getChannelId(Class<?> channelClass) {
    Channel channel = channelClass.getAnnotation(Channel.class);
    if (channel == null) {
      throw UnexpectedExceptions.withMessage("Class {0} does not contain annotation {1}",
          channelClass.getCanonicalName(), Channel.class.getSimpleName());
    }
    return channel.value();
  }

  static <S, R> String getChannelId(Class<S> sourceDomain, ChannelFunction0<? super S, R> channelFunction) {
    return findChannelId(sourceDomain, channelFunction, channelFunction::traverse);
  }

  static <S, R, Q> String getChannelId(
      Class<S> sourceDomain, ChannelFunction1<? super S, R, Q> channelFunction, Q qualifierAnyValidValue
  ) {
    return findChannelId(sourceDomain, channelFunction,
        (trackedObject) -> channelFunction.traverse(trackedObject, qualifierAnyValidValue));
  }

  static String computedChannelId(String channelClassCanonicalName) {
    return new RepetableUuidIdentifierGenerator(channelClassCanonicalName).next();
  }

  private static <S> String findChannelId(
      Class<S> sourceDomain, Object channelFunction, Consumer<S> trackedObjectProcessor
  ) {
    Tracker tracker = Trackers.get();
    S trackedObject = TrackerFunctions.createTrackedObject(sourceDomain, tracker);
    trackedObjectProcessor.accept(trackedObject);
    List<Method> trackedMethods = tracker.getInvokedMethods();
    return extractChannelId(trackedMethods, sourceDomain, channelFunction);
  }

  static MethodStatement getChannelMethod(CustomType channelType) {
    return channelType.declaredMethods().stream()
        .filter(m -> m.isPublic() && !m.isDefault() && !m.isStatic())
        .findFirst()
        .orElseThrow(() -> UnexpectedExceptions.withMessage("Could not find channel method " +
            "in class {0}", channelType.canonicalName()));
  }

  static String getUnitGuideCid(MethodStatement guideMethod) {
    String cid = guideMethod.selectAnnotation(Mapper.class.getCanonicalName())
        .map(ChannelFunctions::extractCid)
        .orElse(null);
    if (cid != null) {
      return cid;
    }
    cid = guideMethod.overrideMethods().stream()
        .map(m -> m.selectAnnotation(Mapper.class.getCanonicalName()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(ChannelFunctions::extractCid)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
    if (cid != null) {
      return cid;
    }

    cid = guideMethod.selectAnnotation(Mover.class.getCanonicalName())
        .map(ChannelFunctions::extractCid)
        .orElse(null);
    if (cid != null) {
      return cid;
    }
    cid = guideMethod.overrideMethods().stream()
        .map(m -> m.selectAnnotation(Mover.class.getCanonicalName()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(ChannelFunctions::extractCid)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
    if (cid != null) {
      return cid;
    }

    cid = guideMethod.selectAnnotation(MapperOfMoving.class.getCanonicalName())
        .map(ChannelFunctions::extractCid)
        .orElse(null);
    if (cid != null) {
      return cid;
    }
    cid = guideMethod.overrideMethods().stream()
        .map(m -> m.selectAnnotation(MapperOfMoving.class.getCanonicalName()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(ChannelFunctions::extractCid)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
    if (cid != null) {
      return cid;
    }

    CustomType declaringType = guideMethod.owner();
    if (!declaringType.hasAnnotation(Guide.class)) {
      throw UnexpectedExceptions.withMessage("Expected guide unit class {0}",
          declaringType.canonicalName());
    }
    Channel channel = findOverrideChannelRecursive(guideMethod, guideMethod.owner());
    if (channel == null) {
      throw UnexpectedExceptions.withMessage("Could not get unit guide annotation @Channel. Unit {0}, " +
              "guide method '{1}'", guideMethod.owner().canonicalName(), guideMethod.name());
    }
    return channel.value();
  }

  static String getUnitGuideCid(Object unit, MethodStatement guideMethod) {
    String cid = getUnitGuideCid(guideMethod);
    if (cid != null) {
      return cid;
    }

    if (unit instanceof tech.intellispaces.jaquarius.guide.Guide) {
      var guide = (tech.intellispaces.jaquarius.guide.Guide<?, ?>) unit;
      return guide.cid();
    }
    throw UnexpectedExceptions.withMessage("Could not define guide channel ID");
  }

  private static String extractCid(AnnotationInstance annotation) {
    Optional<Instance> value = annotation.value();
    if (value.isEmpty()) {
      return null;
    }
    ClassInstance valueClassInstance = value.get().asClass().orElseThrow();
    if (Void.class.getCanonicalName().equals(valueClassInstance.type().canonicalName())) {
      return null;
    }
    AnnotationInstance channelAnnotation = valueClassInstance.type()
        .selectAnnotation(Channel.class.getCanonicalName())
        .orElseThrow();
    Optional<Instance> cid = channelAnnotation.value();
    return cid.map(instance -> instance.asString().orElseThrow().value()).orElse(null);
  }

  private static Channel findOverrideChannelRecursive(MethodStatement guideMethod, CustomType aClass) {
    Channel t = getUnitGuideChannel(guideMethod, aClass);
    if (t != null) {
      return t;
    }
    for (CustomTypeReference parent : aClass.parentTypes()) {
      t = findOverrideChannelRecursive(guideMethod, parent.targetType());
      if (t != null) {
        return t;
      }
    }
    return null;
  }

  private static Channel getUnitGuideChannel(MethodStatement guideMethod, CustomType aClass) {
    Optional<Channel> channel = aClass.selectAnnotation(Channel.class);
    if (channel.isEmpty()) {
      return null;
    }
    Optional<MethodStatement> method = aClass.declaredMethod(guideMethod.name(), guideMethod.parameterTypes());
    if (method.isEmpty()) {
      return null;
    }
    return channel.get();
  }

  static boolean isMovingBasedChannel(MethodStatement domainMethod) {
    Channel channel = getDomainMainChannelAnnotation(domainMethod);
    return ChannelFunctions.getTraverseType(channel).isMovingBased();
  }

  static Channel getDomainMainChannelAnnotation(MethodStatement domainMethod) {
    Optional<Channel> channel = domainMethod.selectAnnotation(Channel.class);
    if (channel.isPresent()) {
      return channel.get();
    }
    channel = domainMethod.overrideMethods().stream()
        .map(m -> m.selectAnnotation(Channel.class))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findAny();
    if (channel.isEmpty()) {
      throw UnexpectedExceptions.withMessage("Could not find annotation @{0} of method '{1}' in domain {2}",
        Channel.class.getSimpleName(), domainMethod.name(), domainMethod.owner().canonicalName());
    }
    return channel.get();
  }

  static Channel findObjectHandleMethodChannelAnnotation(MethodStatement objectHandleMethod) {
    CustomType objectHandleClass = objectHandleMethod.owner();
    CustomType domainClass = ObjectHandleFunctions.getDomainTypeOfObjectHandle(objectHandleClass);
    Channel channel = findObjectHandleMethodChannelAnnotation(domainClass, objectHandleMethod);
    if (channel == null) {
      throw UnexpectedExceptions.withMessage("Failed to find related channel annotation " +
          "of method '{0}' in {1}. Domain class {2}",
          objectHandleMethod.name(), objectHandleClass.canonicalName(), domainClass.canonicalName());
    }
    return channel;
  }

  private static Channel findObjectHandleMethodChannelAnnotation(
      CustomType domainClass, MethodStatement objectHandleMethod
  ) {
    for (MethodStatement domainMethod : domainClass.declaredMethods()) {
      if (isEquivalentMethods(domainMethod, objectHandleMethod)) {
        return domainMethod.selectAnnotation(Channel.class).orElseThrow();
      }
    }
    for (CustomTypeReference parent : domainClass.parentTypes()) {
      if (DomainFunctions.isDomainType(parent.targetType())) {
        Channel channel = findObjectHandleMethodChannelAnnotation(parent.targetType(), objectHandleMethod);
        if (channel != null) {
          return channel;
        }
      }
    }
    return null;
  }

  private static boolean isEquivalentMethods(MethodStatement domainMethod, MethodStatement objectHandleMethod) {
    if (!domainMethod.name().equals(getMethodMainFormName(objectHandleMethod))) {
      return false;
    } else if (domainMethod.params().size() != objectHandleMethod.params().size()) {
      return false;
    } else {
      for (int i = 0; i < domainMethod.params().size(); ++i) {
        TypeReference domainParamType1 = domainMethod.params().get(i).type();
        TypeReference objectHandleParamType = objectHandleMethod.params().get(i).type();
        CustomType domainParamType2 = ObjectHandleFunctions.getDomainTypeOfObjectHandle(
            objectHandleParamType.asCustomTypeReferenceOrElseThrow().targetType()
        );
        if (!TypeReferenceFunctions.isEqualTypes(domainParamType1, CustomTypeReferences.get(domainParamType2))) {
          return false;
        }
      }
      return true;
    }
  }

  private static String getMethodMainFormName(MethodStatement objectHandleMethod) {
    Optional<TypeReference> returnType = objectHandleMethod.returnType();
    if (returnType.isPresent() && returnType.get().isPrimitiveReference()) {
      return StringFunctions.removeTailOrElseThrow(objectHandleMethod.name(), "AsPrimitive");
    }
    return objectHandleMethod.name();
  }

  static Channel findObjectGuideChannelAnnotation(MethodStatement objectHandleMethod) {
    return findObjectHandleMethodChannelAnnotation(objectHandleMethod);
  }

  private static String extractChannelId(
      List<Method> trackedMethods, Class<?> sourceDomain, Object channelMethod
  ) {
    if (trackedMethods.isEmpty()) {
      throw UnexpectedExceptions.withMessage("Several methods of the domain class {0} were " +
              "invoked while channel method '{1}' was being testing",
          sourceDomain.getCanonicalName(), channelMethod);
    }
    if (trackedMethods.size() > 1) {
      throw UnexpectedExceptions.withMessage("No method of the domain class {0} was invoked " +
              "while channel {1} was being testing", sourceDomain.getCanonicalName(), channelMethod);
    }
    Channel ta = trackedMethods.get(0).getAnnotation(Channel.class);
    if (ta == null) {
      throw UnexpectedExceptions.withMessage("Method '{0}' of the domain class {1} hasn't annotation {2}",
          channelMethod, sourceDomain.getCanonicalName(), Channel.class.getCanonicalName());
    }
    return ta.value();
  }

  static Class<?> getChannelClass(int qualifierCount) {
    return switch (qualifierCount) {
      case 0 -> Channel0.class;
      case 1 -> Channel1.class;
      case 2 -> Channel2.class;
      case 3 -> Channel3.class;
      case 4 -> Channel4.class;
      default -> throw NotImplementedExceptions.withCode("+oyPNA");
    };
  }

  static List<TraverseType> getTraverseTypes(MethodStatement method) {
    Channel channel = method.selectAnnotation(Channel.class).orElseThrow();
    return Arrays.asList(channel.allowedTraverse());
  }

  static TraverseType getTraverseType(Channel channel) {
    if (channel.allowedTraverse().length > 1) {
      return channel.defaultTraverse();
    }
    return channel.allowedTraverse()[0];
  }

  static TraverseType getTraverseType(MethodStatement method) {
    Channel channel = method.selectAnnotation(Channel.class).orElseThrow(() ->
        ConfigurationExceptions.withMessage("Could not define traverse type of method '{0}' in '{1}'",
            method.name(), method.owner().className())
        );
    return getTraverseType(channel);
  }

  static Class<?> getChannelSourceDomainClass(Class<?> channelClass) {
    MethodSignature channelMethod = getChannelMethod(channelClass);
    List<MethodParam> params = channelMethod.params();
    return params.get(0).type().asCustomTypeReferenceOrElseThrow().targetClass();
  }
}
