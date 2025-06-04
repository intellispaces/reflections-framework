package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class MapSpecifiedClassSourceThruIdentifiedChannel1TraversePlanImpl
    extends AbstractTraverseSpecifiedClassSourceThruIdentifierChannelTraversePlan
    implements MapSpecifiedClassSourceThruIdentifiedChannel1TraversePlan
{
  public MapSpecifiedClassSourceThruIdentifiedChannel1TraversePlanImpl(Class<?> reflectionClass, Rid cid) {
    super(reflectionClass, cid);
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.MapSpecifiedClassSourceThruIdentifiedChannel1;
  }

  @Override
  public Object execute(Object source, Object qualifier, TraverseExecutor executor) throws TraverseException {
    return executor.execute(this, source, qualifier);
  }
}
