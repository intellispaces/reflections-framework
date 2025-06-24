package tech.intellispaces.reflections.framework.task.plan;

import tech.intellispaces.core.Rid;

/**
 * The declarative plan to traverse source reflection of specified class through identified channel.
 */
public interface TraverseSourceSpecifiedClassThruIdentifierChannelTraversePlan extends DeclarativeTraversePlan {

  /**
   * The source reflection class.
   */
  Class<?> sourceClass();

  /**
   * The channel identifier.
   */
  Rid channelId();

  ExecutionTraversePlan executionPlan(Class<?> sourceClass);

  void addExecutionPlan(Class<?> sourceClass, ExecutionTraversePlan plan);
}
