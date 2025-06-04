package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class MoveSpecifiedClassSourceThruIdentifiedChannel2TraversePlanImpl
    extends AbstractTraverseSpecifiedClassSourceThruIdentifierChannelTraversePlan
    implements MoveSpecifiedClassSourceThruIdentifiedChannel2TraversePlan
{
  public MoveSpecifiedClassSourceThruIdentifiedChannel2TraversePlanImpl(Class<?> reflectionClass, Rid cid) {
    super(reflectionClass, cid);
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.MoveSpecifiedClassSourceThruIdentifiedChannel2;
  }

  @Override
  public Object execute(
      Object source, Object qualifier1, Object qualifier2, TraverseExecutor executor
  ) throws TraverseException {
    return executor.execute(this, source, qualifier1, qualifier2);
  }
}
