package tech.intellispaces.reflections.framework.task.plan;

import tech.intellispaces.core.ReflectionChannel;

/**
 * The declarative traverse plan to move specified source through specified channel.
 */
public interface MoveSpecifiedSourceAndQualifierThruChannel1Plan extends SpecifiedSourceDeclarativeTraversePlan {
  Object source();

  ReflectionChannel channel();

  Object qualifier();
}
