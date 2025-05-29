package tech.intellispaces.reflections.framework.traverse.plan;

/**
 * The declarative plan to traverse source of specified class through identified channel.
 */
public interface TraverseSpecifiedClassSourceThruIdentifierChannelTraversePlan extends DeclarativeTraversePlan {

  /**
   * The source reflection class.
   */
  Class<?> sourceClass();

  /**
   * The channel ID.
   */
  String channelId();

  ExecutionTraversePlan executionPlan(Class<?> sourceClass);

  void setExecutionPlan(Class<?> sourceClass, ExecutionTraversePlan plan);
}
