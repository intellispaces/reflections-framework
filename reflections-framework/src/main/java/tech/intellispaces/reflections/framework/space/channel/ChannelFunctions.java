package tech.intellispaces.reflections.framework.space.channel;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.text.StringFunctions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.core.Rid;
import tech.intellispaces.core.Rids;
import tech.intellispaces.core.id.IdentifierFunctions;
import tech.intellispaces.javareflection.customtype.CustomType;
import tech.intellispaces.javareflection.customtype.CustomTypes;
import tech.intellispaces.javareflection.instance.AnnotationInstance;
import tech.intellispaces.javareflection.instance.ClassInstance;
import tech.intellispaces.javareflection.instance.Instance;
import tech.intellispaces.javareflection.method.MethodParam;
import tech.intellispaces.javareflection.method.MethodSignature;
import tech.intellispaces.javareflection.method.MethodSignatures;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.javareflection.reference.CustomTypeReference;
import tech.intellispaces.javareflection.reference.CustomTypeReferences;
import tech.intellispaces.javareflection.reference.TypeReference;
import tech.intellispaces.javareflection.reference.TypeReferenceFunctions;
import tech.intellispaces.proxies.tracker.Tracker;
import tech.intellispaces.proxies.tracker.TrackerFunctions;
import tech.intellispaces.proxies.tracker.Trackers;
import tech.intellispaces.reflections.framework.annotation.Channel;
import tech.intellispaces.reflections.framework.annotation.Guide;
import tech.intellispaces.reflections.framework.annotation.Mapper;
import tech.intellispaces.reflections.framework.annotation.MapperOfMoving;
import tech.intellispaces.reflections.framework.annotation.Mover;
import tech.intellispaces.reflections.framework.channel.Channel0;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.channel.Channel2;
import tech.intellispaces.reflections.framework.channel.Channel3;
import tech.intellispaces.reflections.framework.channel.Channel4;
import tech.intellispaces.reflections.framework.channel.ChannelFunction0;
import tech.intellispaces.reflections.framework.channel.ChannelFunction1;
import tech.intellispaces.reflections.framework.exception.ConfigurationExceptions;
import tech.intellispaces.reflections.framework.guide.SystemGuide;
import tech.intellispaces.reflections.framework.id.RepetableUuidIdentifierGenerator;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;
import tech.intellispaces.reflections.framework.space.domain.DomainFunctions;
import tech.intellispaces.reflections.framework.traverse.TraverseType;

/**
 * ReflectionChannel related functions.
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

  static Rid getChannelId(MethodStatement channelMethod) {
    return Rids.create(channelMethod.selectAnnotation(Channel.class).orElseThrow().value());
  }

  static Rid getChannelId(Class<?> channelClass) {
    Channel channel = channelClass.getAnnotation(Channel.class);
    if (channel == null) {
      throw UnexpectedExceptions.withMessage("Class {0} does not contain annotation {1}",
          channelClass.getCanonicalName(), Channel.class.getSimpleName());
    }
    return Rids.create(channel.value());
  }

  static <S, R> Rid getChannelId(Class<S> domain, ChannelFunction0<? super S, R> channelFunction) {
    return findChannelId(domain, channelFunction, channelFunction::traverse);
  }

  static <S, R, Q> Rid getChannelId(
      Class<S> domain, ChannelFunction1<? super S, R, Q> channelFunction, Q qualifierAnyValidValue
  ) {
    return findChannelId(domain, channelFunction,
        (trackedObject) -> channelFunction.traverse(trackedObject, qualifierAnyValidValue));
  }

  static <S, R, Q> Rid getChannelId(
      Type<S> domain, ChannelFunction1<? super S, R, Q> channelFunction, Q qualifierAnyValidValue
  ) {
    return findChannelId(domain, channelFunction,
        (trackedObject) -> channelFunction.traverse(trackedObject, qualifierAnyValidValue));
  }

  static Rid getOriginDomainChannelId(Class<?> domainClass, Rid cid) {
    CustomType domainType = CustomTypes.of(domainClass);
    return getOriginDomainChannelId(domainType, domainType, cid);
  }

  private static Rid getOriginDomainChannelId(CustomType originDomain, CustomType domain, Rid cid) {
    for (CustomTypeReference parentDomain : domain.parentTypes()) {
      for (MethodStatement method : parentDomain.targetType().declaredMethods()) {
        if (method.hasAnnotation(Channel.class)) {
          Rid curCid = Rids.create(method.selectAnnotation(Channel.class).orElseThrow().value());
          if (cid.equals(curCid)) {
            Optional<MethodStatement> originDomainMethod = originDomain.declaredMethod(
                method.name(), method.parameterTypes());
            if (originDomainMethod.isPresent()) {
              Optional<Channel> originChannelAnnotation = originDomainMethod.orElseThrow().selectAnnotation(Channel.class);
              if (originChannelAnnotation.isPresent()) {
                return Rids.create(originChannelAnnotation.get().value());
              }
            }
          }
        }
      }
    }
    for (CustomTypeReference parentDomain : domain.parentTypes()) {
      Rid originCid = getOriginDomainChannelId(originDomain, parentDomain.targetType(), cid);
      if (originCid != null) {
        return originCid;
      }
    }
    return null;
  }

  static String computedChannelId(String channelClassCanonicalName) {
    return IdentifierFunctions.convertToHexString(
        new RepetableUuidIdentifierGenerator(channelClassCanonicalName).next()
    );
  }

  private static <S> Rid findChannelId(
      Class<S> sourceDomain, Object channelFunction, Consumer<S> trackedObjectProcessor
  ) {
    Tracker tracker = Trackers.get();
    S trackedObject = TrackerFunctions.createTrackedObject(sourceDomain, tracker);
    trackedObjectProcessor.accept(trackedObject);
    List<Method> trackedMethods = tracker.getInvokedMethods();
    return extractChannelId(trackedMethods, sourceDomain, channelFunction);
  }

  @SuppressWarnings("unchecked")
  private static <S> Rid findChannelId(
      Type<S> sourceDomainType, Object channelFunction, Consumer<S> trackedObjectProcessor
  ) {
    Class<S> sourceDomainClass = (Class<S>) sourceDomainType.asClassType().baseClass();;
    return findChannelId(sourceDomainClass, channelFunction, trackedObjectProcessor);
  }

  static MethodStatement getChannelMethod(CustomType channelType) {
    return channelType.declaredMethods().stream()
        .filter(m -> m.isPublic() && !m.isDefault() && !m.isStatic())
        .findFirst()
        .orElseThrow(() -> UnexpectedExceptions.withMessage("Could not find channel method " +
            "in class {0}", channelType.canonicalName()));
  }

  static Rid getUnitGuideChannelId(MethodStatement guideMethod) {
    Rid cid = guideMethod.selectAnnotation(Mapper.class.getCanonicalName())
        .map(ChannelFunctions::extractChannelId)
        .orElse(null);
    if (cid != null) {
      return cid;
    }
    cid = guideMethod.overrideMethods().stream()
        .map(m -> m.selectAnnotation(Mapper.class.getCanonicalName()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(ChannelFunctions::extractChannelId)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
    if (cid != null) {
      return cid;
    }

    cid = guideMethod.selectAnnotation(Mover.class.getCanonicalName())
        .map(ChannelFunctions::extractChannelId)
        .orElse(null);
    if (cid != null) {
      return cid;
    }
    cid = guideMethod.overrideMethods().stream()
        .map(m -> m.selectAnnotation(Mover.class.getCanonicalName()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(ChannelFunctions::extractChannelId)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
    if (cid != null) {
      return cid;
    }

    cid = guideMethod.selectAnnotation(MapperOfMoving.class.getCanonicalName())
        .map(ChannelFunctions::extractChannelId)
        .orElse(null);
    if (cid != null) {
      return cid;
    }
    cid = guideMethod.overrideMethods().stream()
        .map(m -> m.selectAnnotation(MapperOfMoving.class.getCanonicalName()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(ChannelFunctions::extractChannelId)
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
    return Rids.create(channel.value());
  }

  static Rid getUnitGuideChannelId(Object unit, MethodStatement guideMethod) {
    Rid cid = getUnitGuideChannelId(guideMethod);
    if (cid != null) {
      return cid;
    }

    if (unit instanceof SystemGuide) {
      var guide = (SystemGuide<?, ?>) unit;
      return guide.channelId();
    }
    throw UnexpectedExceptions.withMessage("Could not define guide channel ID");
  }

  private static Rid extractChannelId(AnnotationInstance annotation) {
    Optional<Instance> cidAttribute = annotation.valueOf("cid");
    if (cidAttribute.isPresent()) {
      String cid = cidAttribute.get().asString().orElseThrow().value();
      if (StringFunctions.isNotBlank(cid)) {
        return Rids.create(cid);
      }
    }

    Optional<Instance> valueAttribute = annotation.value();
    if (valueAttribute.isEmpty()) {
      return null;
    }
    ClassInstance valueClassInstance = valueAttribute.get().asClass().orElseThrow();
    if (Void.class.getCanonicalName().equals(valueClassInstance.type().canonicalName())) {
      return null;
    }
    AnnotationInstance channelAnnotation = valueClassInstance.type()
        .selectAnnotation(Channel.class.getCanonicalName())
        .orElseThrow();
    Optional<Instance> cid = channelAnnotation.value();
    return cid.map(instance -> Rids.create(instance.asString().orElseThrow().value())).orElse(null);
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

  static Channel findReflectionMethodChannelAnnotation(MethodStatement reflectionMethod) {
    CustomType reflectionClass = reflectionMethod.owner();
    CustomType domainClass = ReflectionFunctions.getDomainOfObjectFormOrElseThrow(reflectionClass);
    Channel channel = findReflectionMethodChannelAnnotation(reflectionMethod, domainClass);
    if (channel == null) {
      throw UnexpectedExceptions.withMessage("Failed to find related channel annotation " +
          "of method '{0}' in {1}. ReflectionDomain class {2}",
          reflectionMethod.name(), reflectionClass.canonicalName(), domainClass.canonicalName());
    }
    return channel;
  }

  private static Channel findReflectionMethodChannelAnnotation(
      MethodStatement reflectionMethod, CustomType domainClass
  ) {
    for (MethodStatement domainMethod : domainClass.declaredMethods()) {
      if (isEquivalentMethods(domainMethod, reflectionMethod)) {
        return domainMethod.selectAnnotation(Channel.class).orElseThrow();
      }
    }
    for (CustomTypeReference parent : domainClass.parentTypes()) {
      if (DomainFunctions.isDomainType(parent.targetType())) {
        Channel channel = findReflectionMethodChannelAnnotation(reflectionMethod, parent.targetType());
        if (channel != null) {
          return channel;
        }
      }
    }
    return null;
  }

  private static boolean isEquivalentMethods(MethodStatement domainMethod, MethodStatement reflectionMethod) {
    if (!domainMethod.name().equals(getMethodMainFormName(reflectionMethod))) {
      return false;
    } else if (domainMethod.params().size() != reflectionMethod.params().size()) {
      return false;
    } else {
      for (int i = 0; i < domainMethod.params().size(); ++i) {
        TypeReference domainParamType1 = domainMethod.params().get(i).type();
        TypeReference reflectionParamType = reflectionMethod.params().get(i).type();
        CustomType domainParamType2 = ReflectionFunctions.getDomainOfObjectFormOrElseThrow(
            reflectionParamType.asCustomTypeReferenceOrElseThrow().targetType()
        );
        if (!TypeReferenceFunctions.isEqualTypes(domainParamType1, CustomTypeReferences.get(domainParamType2))) {
          return false;
        }
      }
      return true;
    }
  }

  private static String getMethodMainFormName(MethodStatement reflectionMethod) {
    if (NameConventionFunctions.isPrimitiveTargetForm(reflectionMethod)) {
      return StringFunctions.removeTailOrElseThrow(reflectionMethod.name(), "AsPrimitive");
    }
    return reflectionMethod.name();
  }

  static Channel findObjectGuideChannelAnnotation(MethodStatement reflectionMethod) {
    return findReflectionMethodChannelAnnotation(reflectionMethod);
  }

  private static Rid extractChannelId(
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
    return Rids.create(ta.value());
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
