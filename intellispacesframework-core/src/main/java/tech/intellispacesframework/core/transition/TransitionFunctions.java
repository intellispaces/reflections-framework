package tech.intellispacesframework.core.transition;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.annotation.Transition;
import tech.intellispacesframework.core.object.ObjectFunctions;
import tech.intellispacesframework.dynamicproxy.tracker.Tracker;
import tech.intellispacesframework.dynamicproxy.tracker.TrackerBuilder;
import tech.intellispacesframework.dynamicproxy.tracker.TrackerFunctions;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Transition functions.
 */
public interface TransitionFunctions {

  static <S, T, Q> String getTransitionId(Class<S> sourceDomain, BiFunction<S, Q, T> transitionMethod, Q qualifierAnyValidValue) {
    Tracker tracker = TrackerBuilder.build();
    S trackedObject = TrackerFunctions.createTrackedObject(sourceDomain, tracker);
    transitionMethod.apply(trackedObject, qualifierAnyValidValue);
    List<Method> trackedMethods = tracker.getInvokedMethods();
    return extractTransitionId(trackedMethods, sourceDomain, transitionMethod);
  }

  static String getTransitionIdOfEmbeddedGuide(Class<?> objectHandleClass, Method guideMethod) {
    Class<?> domainClass = ObjectFunctions.getDomainClassOfObjectHandle(objectHandleClass);
    for (Method m : domainClass.getDeclaredMethods()) {
      if (m.getName().equals(guideMethod.getName())) {
        Transition ta = m.getAnnotation(Transition.class);
        return ta.value();
      }
    }
    throw new RuntimeException();
  }

  private static String extractTransitionId(List<Method> trackedMethods, Class<?> sourceDomain, BiFunction<?, ?, ?> transitionMethod) {
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
