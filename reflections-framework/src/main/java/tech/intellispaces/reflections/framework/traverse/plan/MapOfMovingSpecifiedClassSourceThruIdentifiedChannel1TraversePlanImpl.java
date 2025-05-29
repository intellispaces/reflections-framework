package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class MapOfMovingSpecifiedClassSourceThruIdentifiedChannel1TraversePlanImpl
    extends AbstractTraverseSpecifiedClassSourceThruIdentifierChannelTraversePlan
    implements MapOfMovingSpecifiedClassSourceThruIdentifiedChannel1TraversePlan
{
  public MapOfMovingSpecifiedClassSourceThruIdentifiedChannel1TraversePlanImpl(Class<?> reflectionClass, String cid) {
    super(reflectionClass, cid);
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel1;
  }

  @Override
  public Object execute(Object source, Object qualifier, TraverseExecutor executor) throws TraverseException {
    return executor.execute(this, source, qualifier);
  }
}
