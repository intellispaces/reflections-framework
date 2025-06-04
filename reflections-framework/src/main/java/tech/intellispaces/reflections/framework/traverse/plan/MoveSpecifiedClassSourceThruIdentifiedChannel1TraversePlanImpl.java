package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class MoveSpecifiedClassSourceThruIdentifiedChannel1TraversePlanImpl
    extends AbstractTraverseSpecifiedClassSourceThruIdentifierChannelTraversePlan
    implements MoveSpecifiedClassSourceThruIdentifiedChannel1TraversePlan
{
  public MoveSpecifiedClassSourceThruIdentifiedChannel1TraversePlanImpl(Class<?> reflectionClass, Rid cid) {
    super(reflectionClass, cid);
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.MoveSpecifiedClassSourceThruIdentifiedChannel1;
  }

  @Override
  public Object execute(Object source, Object qualifier, TraverseExecutor executor) throws TraverseException {
    return executor.execute(this, source, qualifier);
  }
}
