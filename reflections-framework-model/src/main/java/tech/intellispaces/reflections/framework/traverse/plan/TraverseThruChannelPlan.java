package tech.intellispaces.reflections.framework.traverse.plan;

/**
 * The declarative plan to traverse reflection through channel.
 */
public interface TraverseThruChannelPlan extends DeclarativeTraversePlan {

  /**
   * The source reflection class.
   */
  Class<?> reflectionClass();

  /**
   * Channel ID.
   */
  String channelId();

  ExecutionTraversePlan cachedExecutionPlan(Class<?> sourceClass);

  void cacheExecutionPlan(Class<?> sourceClass, ExecutionTraversePlan traversePlan);
}
