package tech.intellispaces.framework.core.traverse;

/**
 * Declarative plan to traverse object handle through transition.
 */
public interface ObjectHandleTraversePlan extends DeclarativePlan {

  /**
   * Source object handle class.
   */
  Class<?> objectHandleClass();

  /**
   * Transition ID.
   */
  String tid();

  ActualPlan getActualPlan(Class<?> objectHandleClass);

  void addActualPlan(Class<?> objectHandleClass, ActualPlan traversePlan);
}
