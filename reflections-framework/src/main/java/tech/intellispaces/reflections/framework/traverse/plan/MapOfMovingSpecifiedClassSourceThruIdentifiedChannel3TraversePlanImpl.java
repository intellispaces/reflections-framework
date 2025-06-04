package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class MapOfMovingSpecifiedClassSourceThruIdentifiedChannel3TraversePlanImpl
    extends AbstractTraverseSpecifiedClassSourceThruIdentifierChannelTraversePlan
    implements MapOfMovingSpecifiedClassSourceThruIdentifiedChannel3TraversePlan
{
  public MapOfMovingSpecifiedClassSourceThruIdentifiedChannel3TraversePlanImpl(Class<?> reflectionClass, Rid cid) {
    super(reflectionClass, cid);
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel3;
  }

  @Override
  public Object execute(
      Object source, Object qualifier1, Object qualifier2, Object qualifier3, TraverseExecutor executor
  ) throws TraverseException {
    return executor.execute(this, source, qualifier1, qualifier2, qualifier3);
  }
}
