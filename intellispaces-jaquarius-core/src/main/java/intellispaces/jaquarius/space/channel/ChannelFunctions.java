package intellispaces.jaquarius.space.channel;

import intellispaces.common.base.exception.NotImplementedExceptions;
import intellispaces.common.base.exception.UnexpectedExceptions;
import intellispaces.common.base.text.StringFunctions;
import intellispaces.common.dynamicproxy.tracker.Tracker;
import intellispaces.common.dynamicproxy.tracker.TrackerFunctions;
import intellispaces.common.dynamicproxy.tracker.Trackers;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodSignature;
import intellispaces.common.javastatement.method.MethodSignatures;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.reference.CustomTypeReference;
import intellispaces.common.javastatement.reference.CustomTypeReferences;
import intellispaces.common.javastatement.reference.TypeReference;
import intellispaces.common.javastatement.reference.TypeReferenceFunctions;
import intellispaces.jaquarius.annotation.Channel;
import intellispaces.jaquarius.annotation.Guide;
import intellispaces.jaquarius.annotation.Mapper;
import intellispaces.jaquarius.annotation.MapperOfMoving;
import intellispaces.jaquarius.annotation.Mover;
import intellispaces.jaquarius.channel.Channel0;
import intellispaces.jaquarius.channel.Channel1;
import intellispaces.jaquarius.channel.Channel2;
import intellispaces.jaquarius.channel.Channel3;
import intellispaces.jaquarius.channel.Channel4;
import intellispaces.jaquarius.exception.ConfigurationExceptions;
import intellispaces.jaquarius.id.RepetableUuidIdentifierGenerator;
import intellispaces.jaquarius.object.ObjectFunctions;
import intellispaces.jaquarius.space.domain.DomainFunctions;
import intellispaces.jaquarius.traverse.TraverseType;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

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

  static String getChannelId(Class<?> channelClass) {
    Channel channel = channelClass.getAnnotation(Channel.class);
    if (channel == null) {
      throw UnexpectedExceptions.withMessage("Class {0} does not contain annotation {1}",
          channelClass.getCanonicalName(), Channel.class.getSimpleName());
    }
    return channel.value();
  }

  static <S, R> String getChannelId(Class<S> sourceDomain, Function<? super S, R> channelMethod) {
    return findChannelId(sourceDomain, channelMethod, channelMethod::apply);
  }

  static <S, R, Q> String getChannelId(
    Class<S> sourceDomain, BiFunction<? super S, Q, R> channelMethod, Q qualifierAnyValidValue
  ) {
    return findChannelId(sourceDomain, channelMethod,
        (trackedObject) -> channelMethod.apply(trackedObject, qualifierAnyValidValue));
  }

  static String computedChannelId(String channelClassCanonicalName) {
    return new RepetableUuidIdentifierGenerator(channelClassCanonicalName).next();
  }

  private static <S> String findChannelId(
      Class<S> sourceDomain, Object channelMethod, Consumer<S> trackedObjectProcessor
  ) {
    Tracker tracker = Trackers.get();
    S trackedObject = TrackerFunctions.createTrackedObject(sourceDomain, tracker);
    trackedObjectProcessor.accept(trackedObject);
    List<Method> trackedMethods = tracker.getInvokedMethods();
    return extractChannelId(trackedMethods, sourceDomain, channelMethod);
  }

  static MethodStatement getChannelMethod(CustomType channelType) {
    return channelType.declaredMethods().stream()
        .filter(m -> m.isPublic() && !m.isDefault() && !m.isStatic())
        .findFirst()
        .orElseThrow(() -> {throw UnexpectedExceptions.withMessage("Could not find channel method " +
                "in class {0}", channelType.canonicalName());});
  }

  static String getUnitGuideCid(Object unitInstance, MethodStatement guideMethod) {
    Optional<Mapper> mapper = guideMethod.selectAnnotation(Mapper.class);
    if (mapper.isEmpty()) {
      mapper = guideMethod.overrideMethods().stream()
          .map(m -> m.selectAnnotation(Mapper.class))
          .filter(Optional::isPresent)
          .map(Optional::get)
          .findFirst();
    }
    if (mapper.isPresent()) {
      Class<?> channelClass = mapper.get().value();
      if (channelClass != null) {
        Channel channel = channelClass.getAnnotation(Channel.class);
        if (channel != null) {
          return channel.value();
        }
      }
    }

    Optional<Mover> mover = guideMethod.selectAnnotation(Mover.class);
    if (mover.isEmpty()) {
      mover = guideMethod.overrideMethods().stream()
          .map(m -> m.selectAnnotation(Mover.class))
          .filter(Optional::isPresent)
          .map(Optional::get)
          .findFirst();
    }
    if (mover.isPresent()) {
      Class<?> channelClass = mover.get().value();
      if (channelClass != null) {
        Channel channel = channelClass.getAnnotation(Channel.class);
        if (channel != null) {
          return channel.value();
        }
      }
    }

    Optional<MapperOfMoving> mapperOfMoving = guideMethod.selectAnnotation(MapperOfMoving.class);
    if (mapperOfMoving.isEmpty()) {
      mapperOfMoving = guideMethod.overrideMethods().stream()
        .map(m -> m.selectAnnotation(MapperOfMoving.class))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst();
    }
    if (mapperOfMoving.isPresent()) {
      Class<?> chanelClass = mapperOfMoving.get().value();
      if (chanelClass != null) {
        Channel channel = chanelClass.getAnnotation(Channel.class);
        if (channel != null) {
          return channel.value();
        }
      }
    }

    CustomType declaringType = guideMethod.owner();
    if (!declaringType.hasAnnotation(Guide.class)) {
      throw UnexpectedExceptions.withMessage("Expected guide unit class {0}",
          declaringType.canonicalName());
    }
    if (unitInstance instanceof intellispaces.jaquarius.guide.Guide) {
      var guide = (intellispaces.jaquarius.guide.Guide<?, ?>) unitInstance;
      return guide.cid();
    } else {
      Channel channel = findOverrideChannelRecursive(guideMethod, guideMethod.owner());
      if (channel == null) {
        throw UnexpectedExceptions.withMessage("Could not get unit guide annotation @Channel. Unit {0}, " +
                "guide method '{1}'", guideMethod.owner().canonicalName(), guideMethod.name());
      }
      return channel.value();
    }
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
    CustomType domainClass = ObjectFunctions.getDomainTypeOfObjectHandle(objectHandleClass);
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
        CustomType domainParamType2 = ObjectFunctions.getDomainTypeOfObjectHandle(
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
      return StringFunctions.removeTailOrElseThrow(objectHandleMethod.name(), "Primitive");
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
