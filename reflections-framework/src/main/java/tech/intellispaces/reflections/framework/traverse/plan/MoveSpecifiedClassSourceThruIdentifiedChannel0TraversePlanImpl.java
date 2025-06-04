package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class MoveSpecifiedClassSourceThruIdentifiedChannel0TraversePlanImpl
    extends AbstractTraverseSpecifiedClassSourceThruIdentifierChannelTraversePlan
    implements MoveSpecifiedClassSourceThruIdentifiedChannel0TraversePlan
{
  public MoveSpecifiedClassSourceThruIdentifiedChannel0TraversePlanImpl(Class<?> reflectionClass, Rid cid) {
    super(reflectionClass, cid);
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.MoveSpecifiedClassSourceThruIdentifiedChannel0;
  }

  @Override
  public Object execute(Object source, TraverseExecutor executor) throws TraverseException {
    return executor.execute(this, source);
  }
}
