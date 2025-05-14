package tech.intellispaces.reflections.framework.traverse.plan;

/**
 * The declarative plan to traverse reflection through channel.
 */
public interface ObjectHandleTraversePlan extends DeclarativeTraversePlan {

  /**
   * The source reflection class.
   */
  Class<?> objectHandleClass();

  /**
   * Channel ID.
   */
  String channelId();

  ExecutionTraversePlan cachedExecutionPlan(Class<?> sourceClass);

  void cacheExecutionPlan(Class<?> sourceClass, ExecutionTraversePlan traversePlan);
}
