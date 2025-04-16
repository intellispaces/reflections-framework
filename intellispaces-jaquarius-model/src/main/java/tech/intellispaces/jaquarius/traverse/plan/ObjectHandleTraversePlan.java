package tech.intellispaces.jaquarius.traverse.plan;

/**
 * The declarative plan to traverse object handle through channel.
 */
public interface ObjectHandleTraversePlan extends DeclarativeTraversePlan {

  /**
   * Source object handle class.
   */
  Class<?> objectHandleClass();

  /**
   * Channel ID.
   */
  String channelId();

  ExecutionTraversePlan cachedExecutionPlan(Class<?> sourceClass);

  void cacheExecutionPlan(Class<?> sourceClass, ExecutionTraversePlan traversePlan);
}
