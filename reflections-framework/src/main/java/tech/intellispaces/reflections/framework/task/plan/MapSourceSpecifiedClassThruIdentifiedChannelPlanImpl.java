package tech.intellispaces.reflections.framework.task.plan;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class MapSourceSpecifiedClassThruIdentifiedChannelPlanImpl
    extends AbstractTraverseSpecifiedClassSourceThruIdentifierChannelTraversePlan
    implements MapSourceSpecifiedClassThruIdentifiedChannelPlan
{
  public MapSourceSpecifiedClassThruIdentifiedChannelPlanImpl(Class<?> reflectionClass, Rid cid) {
    super(reflectionClass, cid);
  }

  @Override
  public TaskPlanType type() {
    return TaskPlanTypes.MapSourceSpecifiedClassThruIdentifiedChannel;
  }

  @Override
  public Object execute(TraverseExecutor executor) throws TraverseException {
    throw UnexpectedExceptions.withMessage("The method being called does not match this plan");
  }

  @Override
  public Object execute(
      Object source,
      TraverseExecutor executor
  ) throws TraverseException {
    return executor.execute(this, source);
  }

  @Override
  public int executeReturnInt(
      Object source,
      TraverseExecutor executor
  ) throws TraverseException {
    return executor.executeReturnInt(this, source);
  }

  @Override
  public double executeReturnDouble(
      Object source,
      TraverseExecutor executor
  ) throws TraverseException {
    return executor.executeReturnDouble(this, source);
  }

  @Override
  public Object execute(
      Object source,
      Object qualifier,
      TraverseExecutor executor
  ) throws TraverseException {
    return executor.execute(this, source, qualifier);
  }

  @Override
  public Object execute(
      Object source,
      Object qualifier1,
      Object qualifier2,
      TraverseExecutor executor
  ) throws TraverseException {
    return executor.execute(this, source, qualifier1, qualifier2);
  }

  @Override
  public Object execute(
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3,
      TraverseExecutor executor
  ) throws TraverseException {
    return executor.execute(this, source, qualifier1, qualifier2, qualifier3);
  }

  @Override
  public Object execute(
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3,
      Object qualifier4,
      TraverseExecutor executor
  ) throws TraverseException {
    throw NotImplementedExceptions.withCode("N2MbRw");
  }
}
