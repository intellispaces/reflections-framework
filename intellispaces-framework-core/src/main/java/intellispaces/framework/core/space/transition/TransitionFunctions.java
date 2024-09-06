package intellispaces.framework.core.space.transition;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.annotation.Guide;
import intellispaces.framework.core.annotation.Mapper;
import intellispaces.framework.core.annotation.Mover;
import intellispaces.framework.core.annotation.Transition;
import intellispaces.framework.core.object.ObjectFunctions;
import intellispaces.framework.core.space.domain.DomainFunctions;
import intellispaces.framework.core.traverse.TraverseTypes;
import intellispaces.common.javastatement.customtype.CustomType;
import intellispaces.common.javastatement.method.MethodStatement;
import intellispaces.common.javastatement.method.Methods;
import intellispaces.common.dynamicproxy.tracker.Tracker;
import intellispaces.common.dynamicproxy.tracker.TrackerBuilder;
import intellispaces.common.dynamicproxy.tracker.TrackerFunctions;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Transition functions.
 */
public interface TransitionFunctions {

  static String getTransitionId(Class<?> transitionClass) {
    Transition transition = transitionClass.getAnnotation(Transition.class);
    if (transition == null) {
      throw UnexpectedViolationException.withMessage("Class {0} does not contain annotation {1}",
          transitionClass.getCanonicalName(), Transition.class.getSimpleName());
    }
    return transition.value();
  }

  static <S, B> String getTransitionId(Class<S> sourceDomain, Function<? super S, B> transitionMethod) {
    return findTransitionId(sourceDomain, transitionMethod, transitionMethod::apply);
  }

  static <S, B, Q> String getTransitionId(
      Class<S> sourceDomain, BiFunction<? super S, Q, B> transitionMethod, Q qualifierAnyValidValue
  ) {
    return findTransitionId(sourceDomain, transitionMethod,
        (trackedObject) -> transitionMethod.apply(trackedObject, qualifierAnyValidValue));
  }

  private static <S> String findTransitionId(
      Class<S> sourceDomain, Object transitionMethod, Consumer<S> trackedObjectProcessor
  ) {
    Tracker tracker = TrackerBuilder.build();
    S trackedObject = TrackerFunctions.createTrackedObject(sourceDomain, tracker);
    trackedObjectProcessor.accept(trackedObject);
    List<Method> trackedMethods = tracker.getInvokedMethods();
    return extractTransitionId(trackedMethods, sourceDomain, transitionMethod);
  }

  static MethodStatement getTransitionMethod(CustomType transitionType) {
    return transitionType.declaredMethods().stream()
        .filter(m -> m.isPublic() && !m.isDefault() && !m.isStatic())
        .findFirst()
        .orElseThrow(() -> {throw UnexpectedViolationException.withMessage("Could not find transition method " +
                "in class {0}", transitionType.canonicalName());});
  }

  static String getUnitGuideTid(Object unitInstance, Method guideMethod) {
    Mapper mapper = guideMethod.getAnnotation(Mapper.class);
    if (mapper == null) {
      mapper = Methods.of(guideMethod).overrideMethods().stream()
          .map(m -> m.selectAnnotation(Mapper.class))
          .filter(Optional::isPresent)
          .map(Optional::get)
          .findFirst().orElse(null);
    }
    if (mapper != null) {
      Class<?> transitionClass = mapper.value();
      if (transitionClass != null) {
        Transition transition = transitionClass.getAnnotation(Transition.class);
        if (transition != null) {
          return transition.value();
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
      Class<?> transitionClass = mover.value();
      if (transitionClass != null) {
        Transition transition = transitionClass.getAnnotation(Transition.class);
        if (transition != null) {
          return transition.value();
        }
      }
    }

    Class<?> declaringClass = guideMethod.getDeclaringClass();
    if (!declaringClass.isAnnotationPresent(Guide.class)) {
      throw UnexpectedViolationException.withMessage("Expected guide unit class {0}",
          declaringClass.getCanonicalName());
    }
    if (unitInstance instanceof intellispaces.framework.core.guide.Guide) {
      var guide = (intellispaces.framework.core.guide.Guide<?, ?>) unitInstance;
      return guide.tid();
    } else {
      Transition transition = findOverrideTransitionRecursive(guideMethod, guideMethod.getDeclaringClass());
      if (transition == null) {
        throw UnexpectedViolationException.withMessage("Could not get unit guide annotation @Transition. Unit {0}, " +
                "guide method ''{1}''", guideMethod.getDeclaringClass().getCanonicalName(), guideMethod.getName());
      }
      return transition.value();
    }
  }

  private static Transition findOverrideTransitionRecursive(Method guideMethod, Class<?> aClass) {
    Transition t = getUnitGuideTransition(guideMethod, aClass);
    if (t != null) {
      return t;
    }
    if (aClass.getSuperclass() != null) {
      t = findOverrideTransitionRecursive(guideMethod, aClass.getSuperclass());
      if (t != null) {
        return t;
      }
    }
    for (Class<?> aInterface : aClass.getInterfaces()) {
      t = findOverrideTransitionRecursive(guideMethod, aInterface);
      if (t != null) {
        return t;
      }
    }
    return null;
  }

  private static Transition getUnitGuideTransition(Method guideMethod, Class<?> aClass) {
    Transition transition = aClass.getAnnotation(Transition.class);
    if (transition == null) {
      return null;
    }
    try {
      Method method = aClass.getDeclaredMethod(guideMethod.getName(), guideMethod.getParameterTypes());
      if (method == null) {
        return null;
      }
      return transition;
    } catch (NoSuchMethodException e) {
      return null;
    }
  }

  static Transition getDomainMainTransitionAnnotation(MethodStatement domainMethod) {
    Optional<Transition> transition = domainMethod.selectAnnotation(Transition.class);
    if (transition.isPresent()) {
      return transition.get();
    }
    transition = domainMethod.overrideMethods().stream()
        .map(m -> m.selectAnnotation(Transition.class))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findAny();
    if (transition.isEmpty()) {
      throw UnexpectedViolationException.withMessage("Could not find annotation @{0} of method ''{1}'' in domain {2}",
        Transition.class.getSimpleName(), domainMethod.name(), domainMethod.owner().canonicalName());
    }
    return transition.get();
  }

  static Transition getObjectHandleMethodTransitionAnnotation(MethodStatement objectHandleMethod) {
    CustomType objectHandleType = objectHandleMethod.owner();
    CustomType domainType = ObjectFunctions.getDomainTypeOfObjectHandle(objectHandleType);
    for (MethodStatement domainMethod : domainType.declaredMethods()) {
      if (domainMethod.name().equals(objectHandleMethod.name())) {
        return domainMethod.selectAnnotation(Transition.class).orElseThrow();
      }
    }
    throw UnexpectedViolationException.withMessage("Failed to find related transition annotation " +
            "of method ''{0}'' in {1}", objectHandleMethod.name(), objectHandleType.canonicalName());
  }

  static Transition getObjectHandleMethodTransitionAnnotation(Method objectHandleMethod) {
    Class<?> objectHandleClass = objectHandleMethod.getDeclaringClass();
    Class<?> domainClass = ObjectFunctions.getDomainClassOfObjectHandle(objectHandleClass);
    Transition transition = getObjectHandleMethodTransitionAnnotation(domainClass, objectHandleMethod);
    if (transition == null) {
      throw UnexpectedViolationException.withMessage("Failed to find related transition annotation " +
              "of method ''{0}'' in {1}", objectHandleMethod.getName(), objectHandleClass.getCanonicalName());
    }
    return transition;
  }

  private static Transition getObjectHandleMethodTransitionAnnotation(
      Class<?> domainClass, Method objectHandleMethod
  ) {
    for (Method domainMethod : domainClass.getDeclaredMethods()) {
      if (isEquivalentMethods(domainMethod, objectHandleMethod)) {
        return domainMethod.getAnnotation(Transition.class);
      }
    }
    for (Class<?> parent : domainClass.getInterfaces()) {
      if (DomainFunctions.isDomainClass(parent)) {
        Transition transition = getObjectHandleMethodTransitionAnnotation(parent, objectHandleMethod);
        if (transition != null) {
          return transition;
        }
      }
    }
    return null;
  }

  private static boolean isEquivalentMethods(Method domainMethod, Method objectHandleMethod) {
    if (!domainMethod.getName().equals(objectHandleMethod.getName())) {
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

  static Transition getAttachedGuideTransitionAnnotation(Method objectHandleMethod) {
    return getObjectHandleMethodTransitionAnnotation(objectHandleMethod);
  }

  private static String extractTransitionId(
      List<Method> trackedMethods, Class<?> sourceDomain, Object transitionMethod
  ) {
    if (trackedMethods.isEmpty()) {
      throw UnexpectedViolationException.withMessage("Several methods of the domain class {0} were " +
              "invoked while transition method '{1}' was being testing",
          sourceDomain.getCanonicalName(), transitionMethod);
    }
    if (trackedMethods.size() > 1) {
      throw UnexpectedViolationException.withMessage("No method of the domain class {0} was invoked " +
              "while transition {1} was being testing", sourceDomain.getCanonicalName(), transitionMethod);
    }
    Transition ta = trackedMethods.get(0).getAnnotation(Transition.class);
    if (ta == null) {
      throw UnexpectedViolationException.withMessage("Method ''{0}'' of the domain class {1} hasn't annotation {2}",
          transitionMethod, sourceDomain.getCanonicalName(), Transition.class.getCanonicalName());
    }
    return ta.value();
  }

  static Class<?> getTransitionClass(int qualifierCount) {
    return switch (qualifierCount) {
      case 0 -> Transition0.class;
      case 1 -> Transition1.class;
      case 2 -> Transition2.class;
      default -> throw UnexpectedViolationException.withMessage("Not implemented");
    };
  }

  static TraverseTypes getTraverseType(Transition transition) {
    if (transition.allowedTraverse().length > 1) {
      return transition.defaultTraverseType();
    }
    return transition.allowedTraverse()[0];
  }

  static TraverseTypes getTraverseType(MethodStatement method) {
    Transition transition = method.selectAnnotation(Transition.class).orElseThrow();
    return getTraverseType(transition);
  }
}
