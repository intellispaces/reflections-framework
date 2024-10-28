package intellispaces.jaquarius.space.channel;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.common.base.text.TextFunctions;
import intellispaces.common.dynamicproxy.tracker.Tracker;
import intellispaces.common.dynamicproxy.tracker.TrackerBuilder;
import intellispaces.common.dynamicproxy.tracker.TrackerFunctions;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodParam;
import intellispaces.common.javastatement.method.MethodSignature;
import intellispaces.common.javastatement.method.MethodSignatures;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.method.Methods;
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
import intellispaces.jaquarius.exception.ConfigurationException;
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
      throw UnexpectedViolationException.withMessage("Expected channel class. Actual class {0} is not channel class",
          channelClass.getCanonicalName());
    }
    Method[] methods = channelClass.getDeclaredMethods();
    if (methods.length != 1) {
      throw UnexpectedViolationException.withMessage("Expected one channel method. Check channel class {0}",
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
      throw UnexpectedViolationException.withMessage("Class {0} does not contain annotation {1}",
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

  private static <S> String findChannelId(
      Class<S> sourceDomain, Object channelMethod, Consumer<S> trackedObjectProcessor
  ) {
    Tracker tracker = TrackerBuilder.build();
    S trackedObject = TrackerFunctions.createTrackedObject(sourceDomain, tracker);
    trackedObjectProcessor.accept(trackedObject);
    List<Method> trackedMethods = tracker.getInvokedMethods();
    return extractChannelId(trackedMethods, sourceDomain, channelMethod);
  }

  static MethodStatement getChannelMethod(CustomType channelType) {
    return channelType.declaredMethods().stream()
        .filter(m -> m.isPublic() && !m.isDefault() && !m.isStatic())
        .findFirst()
        .orElseThrow(() -> {throw UnexpectedViolationException.withMessage("Could not find channel method " +
                "in class {0}", channelType.canonicalName());});
  }

  static String getUnitGuideCid(Object unitInstance, Method guideMethod) {
    Mapper mapper = guideMethod.getAnnotation(Mapper.class);
    if (mapper == null) {
      mapper = Methods.of(guideMethod).overrideMethods().stream()
          .map(m -> m.selectAnnotation(Mapper.class))
          .filter(Optional::isPresent)
          .map(Optional::get)
          .findFirst().orElse(null);
    }
    if (mapper != null) {
      Class<?> channelClass = mapper.value();
      if (channelClass != null) {
        Channel channel = channelClass.getAnnotation(Channel.class);
        if (channel != null) {
          return channel.value();
        }
      }
    }

    Mover mover = guideMethod.getAnnotation(Mover.class);
    if (mover == null) {
      mover = Methods.of(guideMethod).overrideMethods().stream()
          .map(m -> m.selectAnnotation(Mover.class))
          .filter(Optional::isPresent)
          .map(Optional::get)
          .findFirst().orElse(null);
    }
    if (mover != null) {
      Class<?> channelClass = mover.value();
      if (channelClass != null) {
        Channel channel = channelClass.getAnnotation(Channel.class);
        if (channel != null) {
          return channel.value();
        }
      }
    }

    MapperOfMoving mapperOfMoving = guideMethod.getAnnotation(MapperOfMoving.class);
    if (mapperOfMoving == null) {
      mapperOfMoving = Methods.of(guideMethod).overrideMethods().stream()
        .map(m -> m.selectAnnotation(MapperOfMoving.class))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst().orElse(null);
    }
    if (mapperOfMoving != null) {
      Class<?> chanelClass = mapperOfMoving.value();
      if (chanelClass != null) {
        Channel channel = chanelClass.getAnnotation(Channel.class);
        if (channel != null) {
          return channel.value();
        }
      }
    }

    Class<?> declaringClass = guideMethod.getDeclaringClass();
    if (!declaringClass.isAnnotationPresent(Guide.class)) {
      throw UnexpectedViolationException.withMessage("Expected guide unit class {0}",
          declaringClass.getCanonicalName());
    }
    if (unitInstance instanceof intellispaces.jaquarius.guide.Guide) {
      var guide = (intellispaces.jaquarius.guide.Guide<?, ?>) unitInstance;
      return guide.cid();
    } else {
      Channel channel = findOverrideChannelRecursive(guideMethod, guideMethod.getDeclaringClass());
      if (channel == null) {
        throw UnexpectedViolationException.withMessage("Could not get unit guide annotation @Channel. Unit {0}, " +
                "guide method ''{1}''", guideMethod.getDeclaringClass().getCanonicalName(), guideMethod.getName());
      }
      return channel.value();
    }
  }

  private static Channel findOverrideChannelRecursive(Method guideMethod, Class<?> aClass) {
    Channel t = getUnitGuideChannel(guideMethod, aClass);
    if (t != null) {
      return t;
    }
    if (aClass.getSuperclass() != null) {
      t = findOverrideChannelRecursive(guideMethod, aClass.getSuperclass());
      if (t != null) {
        return t;
      }
    }
    for (Class<?> aInterface : aClass.getInterfaces()) {
      t = findOverrideChannelRecursive(guideMethod, aInterface);
      if (t != null) {
        return t;
      }
    }
    return null;
  }

  private static Channel getUnitGuideChannel(Method guideMethod, Class<?> aClass) {
    Channel channel = aClass.getAnnotation(Channel.class);
    if (channel == null) {
      return null;
    }
    try {
      Method method = aClass.getDeclaredMethod(guideMethod.getName(), guideMethod.getParameterTypes());
      if (method == null) {
        return null;
      }
      return channel;
    } catch (NoSuchMethodException e) {
      return null;
    }
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
      throw UnexpectedViolationException.withMessage("Could not find annotation @{0} of method ''{1}'' in domain {2}",
        Channel.class.getSimpleName(), domainMethod.name(), domainMethod.owner().canonicalName());
    }
    return channel.get();
  }

  static Channel getObjectHandleMethodChannelAnnotation(MethodStatement objectHandleMethod) {
    CustomType objectHandleType = objectHandleMethod.owner();
    CustomType domainType = ObjectFunctions.getDomainTypeOfObjectHandle(objectHandleType);
    for (MethodStatement domainMethod : domainType.declaredMethods()) {
      if (domainMethod.name().equals(objectHandleMethod.name())) {
        return domainMethod.selectAnnotation(Channel.class).orElseThrow();
      }
    }
    throw UnexpectedViolationException.withMessage("Failed to find related channel annotation " +
            "of method ''{0}'' in {1}", objectHandleMethod.name(), objectHandleType.canonicalName());
  }

  static Channel getObjectHandleMethodChannelAnnotation(Method objectHandleMethod) {
    Class<?> objectHandleClass = objectHandleMethod.getDeclaringClass();
    Class<?> domainClass = ObjectFunctions.getDomainClassOfObjectHandle(objectHandleClass);
    Channel channel = getObjectHandleMethodChannelAnnotation(domainClass, objectHandleMethod);
    if (channel == null) {
      throw UnexpectedViolationException.withMessage("Failed to find related channel annotation " +
          "of method ''{0}'' in {1}. Domain class {2}",
          objectHandleMethod.getName(), objectHandleClass.getCanonicalName(), domainClass.getCanonicalName());
    }
    return channel;
  }

  private static Channel getObjectHandleMethodChannelAnnotation(
      Class<?> domainClass, Method objectHandleMethod
  ) {
    for (Method domainMethod : domainClass.getDeclaredMethods()) {
      if (isEquivalentMethods(domainMethod, objectHandleMethod)) {
        return domainMethod.getAnnotation(Channel.class);
      }
    }
    for (Class<?> parent : domainClass.getInterfaces()) {
      if (DomainFunctions.isDomainClass(parent)) {
        Channel channel = getObjectHandleMethodChannelAnnotation(parent, objectHandleMethod);
        if (channel != null) {
          return channel;
        }
      }
    }
    return null;
  }

  private static boolean isEquivalentMethods(Method domainMethod, Method objectHandleMethod) {
    if (!domainMethod.getName().equals(getMainName(objectHandleMethod))) {
      return false;
    } else if (domainMethod.getParameterCount() != objectHandleMethod.getParameterCount()) {
      return false;
    } else {
      for (int i = 0; i < domainMethod.getParameterCount(); ++i) {
        Class<?> domainParamType1 = domainMethod.getParameters()[i].getType();
        Class<?> objectHandleParamType = objectHandleMethod.getParameters()[i].getType();
        Class<?> domainParamType2 = ObjectFunctions.getDomainClassOfObjectHandle(objectHandleParamType);
        if (domainParamType1 != domainParamType2) {
          return false;
        }
      }
      return true;
    }
  }

  private static String getMainName(Method objectHandleMethod) {
    if (objectHandleMethod.getReturnType().isPrimitive()) {
      return TextFunctions.removeTailOrElseThrow(objectHandleMethod.getName(), "Primitive");
    }
    return objectHandleMethod.getName();
  }

  static Channel getObjectGuideChannelAnnotation(Method objectHandleMethod) {
    return getObjectHandleMethodChannelAnnotation(objectHandleMethod);
  }

  private static String extractChannelId(
      List<Method> trackedMethods, Class<?> sourceDomain, Object channelMethod
  ) {
    if (trackedMethods.isEmpty()) {
      throw UnexpectedViolationException.withMessage("Several methods of the domain class {0} were " +
              "invoked while channel method '{1}' was being testing",
          sourceDomain.getCanonicalName(), channelMethod);
    }
    if (trackedMethods.size() > 1) {
      throw UnexpectedViolationException.withMessage("No method of the domain class {0} was invoked " +
              "while channel {1} was being testing", sourceDomain.getCanonicalName(), channelMethod);
    }
    Channel ta = trackedMethods.get(0).getAnnotation(Channel.class);
    if (ta == null) {
      throw UnexpectedViolationException.withMessage("Method ''{0}'' of the domain class {1} hasn't annotation {2}",
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
      default -> throw UnexpectedViolationException.withMessage("Not implemented");
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
        ConfigurationException.withMessage("Could not define traverse type of method '{0}' in '{1}'",
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
