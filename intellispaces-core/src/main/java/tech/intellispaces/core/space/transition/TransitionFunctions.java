package tech.intellispaces.core.space.transition;

import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.core.annotation.Guide;
import tech.intellispaces.core.annotation.Mapper;
import tech.intellispaces.core.annotation.Mover;
import tech.intellispaces.core.annotation.Transition;
import tech.intellispaces.core.object.ObjectFunctions;
import tech.intellispaces.core.space.domain.DomainFunctions;
import tech.intellispaces.core.traverse.TraverseTypes;
import tech.intellispaces.javastatements.customtype.CustomType;
import tech.intellispaces.javastatements.method.MethodStatement;
import tech.intellispaces.proxies.tracker.Tracker;
import tech.intellispaces.proxies.tracker.TrackerBuilder;
import tech.intellispaces.proxies.tracker.TrackerFunctions;

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
      throw UnexpectedViolationException.withMessage("Class {} does not contain annotation {}",
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
        .orElseThrow(() -> {throw UnexpectedViolationException.withMessage("Could not find transition method in class {}",
            transitionType.canonicalName());});
  }

  static String getUnitGuideTid(Object unitInstance, Method guideMethod) {
    if (guideMethod.isAnnotationPresent(Mapper.class)) {
      Class<?> transitionClass = guideMethod.getAnnotation(Mapper.class).value();
      if (transitionClass != null) {
        Transition transition = transitionClass.getAnnotation(Transition.class);
        if (transition != null) {
          return transition.value();
        }
      }
    } else if (guideMethod.isAnnotationPresent(Mover.class)) {
      Class<?> transitionClass = guideMethod.getAnnotation(Mover.class).value();
      if (transitionClass != null) {
        Transition transition = transitionClass.getAnnotation(Transition.class);
        if (transition != null) {
          return transition.value();
        }
      }
    }

    Class<?> declaringClass = guideMethod.getDeclaringClass();
    if (!declaringClass.isAnnotationPresent(Guide.class)) {
      throw UnexpectedViolationException.withMessage("Expected guide unit class {}", declaringClass.getCanonicalName());
    }
    if (unitInstance instanceof tech.intellispaces.core.guide.Guide) {
      var guide = (tech.intellispaces.core.guide.Guide<?, ?>) unitInstance;
      return guide.tid();
    } else {
      Transition transition = findOverrideTransitionRecursive(guideMethod, guideMethod.getDeclaringClass());
      if (transition == null) {
        throw UnexpectedViolationException.withMessage("Could not get unit guide annotation @Transition. Unit {}, guide method {}",
            guideMethod.getDeclaringClass().getCanonicalName(), guideMethod.getName());
      }
      return transition.value();
    }
  }

  private static Transition findOverrideTransitionRecursive(Method guideMethod, Class<?> aClass) {
    Transition t = getUnitGuideTid(guideMethod, aClass);
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

  private static Transition getUnitGuideTid(Method guideMethod, Class<?> aClass) {
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
      throw UnexpectedViolationException.withMessage("Could not find annotation @{} of method '{}' in domain {}",
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
    throw UnexpectedViolationException.withMessage("Failed to find related transition annotation of method '{}' in {}",
        objectHandleMethod.name(), objectHandleType.canonicalName());
  }

  static Transition getObjectHandleMethodTransitionAnnotation(Method objectHandleMethod) {
    Class<?> objectHandleClass = objectHandleMethod.getDeclaringClass();
    Class<?> domainClass = ObjectFunctions.getDomainClassOfObjectHandle(objectHandleClass);
    Transition transition = getObjectHandleMethodTransitionAnnotation(domainClass, objectHandleMethod);
    if (transition == null) {
      throw UnexpectedViolationException.withMessage("Failed to find related transition annotation of method '{}' in {}",
          objectHandleMethod.getName(), objectHandleClass.getCanonicalName());
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

  private static String extractTransitionId(List<Method> trackedMethods, Class<?> sourceDomain, Object transitionMethod) {
    if (trackedMethods.isEmpty()) {
      throw UnexpectedViolationException.withMessage("Several methods of the domain class {} were invoked while transition method {} was being testing",
          sourceDomain.getCanonicalName(), transitionMethod);
    }
    if (trackedMethods.size() > 1) {
      throw UnexpectedViolationException.withMessage("No method of the domain class {} was invoked while transition {} was being testing",
          sourceDomain.getCanonicalName(), transitionMethod);
    }
    Transition ta = trackedMethods.get(0).getAnnotation(Transition.class);
    if (ta == null) {
      throw UnexpectedViolationException.withMessage("Method '{}' of the domain class {} hasn't annotation {}",
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
    if (transition.allowedTraverseTypes().length > 1) {
      return transition.defaultTraverseType();
    }
    return transition.allowedTraverseTypes()[0];
  }

  static TraverseTypes getTraverseType(MethodStatement method) {
    Transition transition = method.selectAnnotation(Transition.class).orElseThrow();
    return getTraverseType(transition);
  }
}
