package intellispaces.framework.core.traverse.plan;

/**
 * Declarative plan to traverse object handle through channel.
 */
public interface ObjectHandleTraversePlan extends DeclarativeTraversePlan {

  /**
   * Source object handle class.
   */
  Class<?> objectHandleClass();

  /**
   * Channel ID.
   */
  String cid();

  ExecutionTraversePlan getExecutionPlan(Class<?> sourceClass);

  void addExecutionPlan(Class<?> sourceClass, ExecutionTraversePlan traversePlan);
}
