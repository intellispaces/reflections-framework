package tech.intellispacesframework.core.space.transition;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.annotation.Transition;
import tech.intellispacesframework.core.object.ObjectFunctions;
import tech.intellispacesframework.dynamicproxy.tracker.Tracker;
import tech.intellispacesframework.dynamicproxy.tracker.TrackerBuilder;
import tech.intellispacesframework.dynamicproxy.tracker.TrackerFunctions;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Transition functions.
 */
public interface TransitionFunctions {

  static <S, T> String getTransitionId(Class<S> sourceDomain, Function<S, T> transitionMethod) {
    return findTransitionId(sourceDomain, transitionMethod, transitionMethod::apply);
  }

  static <S, T, Q> String getTransitionId(
      Class<S> sourceDomain, BiFunction<S, Q, T> transitionMethod, Q qualifierAnyValidValue
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

  static String getUnitGuideTid(Method guideMethod) {
    String tid = findUnitGuideTidRecursive(guideMethod, guideMethod.getDeclaringClass());
    if (tid == null) {
      throw UnexpectedViolationException.withMessage("Failed to get unit guide transition ID. Unit {}, guide method {}",
          guideMethod.getDeclaringClass().getCanonicalName(), guideMethod.getName());
    }
    return tid;
  }

  private static String findUnitGuideTidRecursive(Method guideMethod, Class<?> aClass) {
    String tid = getUnitGuideTid(guideMethod, aClass);
    if (tid != null) {
      return tid;
    }
    if (aClass.getSuperclass() != null) {
      tid = findUnitGuideTidRecursive(guideMethod, aClass.getSuperclass());
      if (tid != null) {
        return tid;
      }
    }
    for (Class<?> aInterface : aClass.getInterfaces()) {
      tid = findUnitGuideTidRecursive(guideMethod, aInterface);
      if (tid != null) {
        return tid;
      }
    }
    return null;
  }

  private static String getUnitGuideTid(Method guideMethod, Class<?> aClass) {
    Transition transition = aClass.getAnnotation(Transition.class);
    if (transition == null) {
      return null;
    }
    try {
      Method method = aClass.getDeclaredMethod(guideMethod.getName(), guideMethod.getParameterTypes());
      if (method == null) {
        return null;
      }
      return transition.value();
    } catch (NoSuchMethodException e) {
      return null;
    }
  }

  static String getEmbeddedGuideTid(Method guideMethod) {
    Class<?> objectHandleClass = guideMethod.getDeclaringClass();
    Class<?> domainClass = ObjectFunctions.getDomainClassOfObjectHandle(objectHandleClass);
    for (Method method : domainClass.getDeclaredMethods()) {
      if (method.getName().equals(guideMethod.getName())) {
        Transition transition = method.getAnnotation(Transition.class);
        return transition.value();
      }
    }
    throw UnexpectedViolationException.withMessage("Failed to define transition ID of embedded guide '{}' in {}",
        guideMethod.getName(), objectHandleClass.getCanonicalName());
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
}
