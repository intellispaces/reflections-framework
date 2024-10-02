package intellispaces.framework.core.traverse.plan;

/**
 * Declarative plan to traverse object handle through channel.
 */
public interface ObjectHandleTraversePlan extends DeclarativePlan {

  /**
   * Source object handle class.
   */
  Class<?> objectHandleClass();

  /**
   * Channel ID.
   */
  String cid();

  ExecutionPlan getExecutionPlan(Class<?> sourceClass);

  void addExecutionPlan(Class<?> sourceClass, ExecutionPlan traversePlan);
}
