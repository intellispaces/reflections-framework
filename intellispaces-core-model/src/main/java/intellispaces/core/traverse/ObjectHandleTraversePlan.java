package intellispaces.core.traverse;

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

  ExecutionPlan getExecutionPlan(Class<?> objectHandleClass);

  void addExecutionPlan(Class<?> objectHandleClass, ExecutionPlan traversePlan);
}
