package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class MapSpecifiedClassSourceThruIdentifiedChannel0TraversePlanImpl
    extends AbstractTraverseSpecifiedClassSourceThruIdentifierChannelTraversePlan
    implements MapSpecifiedClassSourceThruIdentifiedChannel0TraversePlan
{
  public MapSpecifiedClassSourceThruIdentifiedChannel0TraversePlanImpl(Class<?> reflectionClass, Rid cid) {
    super(reflectionClass, cid);
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.MapSpecifiedClassSourceThruIdentifiedChannel0;
  }

  @Override
  public Object execute(Object source, TraverseExecutor executor) throws TraverseException {
    return executor.execute(this, source);
  }

  @Override
  public int executeReturnInt(Object source, TraverseExecutor executor) throws TraverseException {
    return executor.executeReturnInt(this, source);
  }

  @Override
  public double executeReturnDouble(Object source, TraverseExecutor executor) throws TraverseException {
    return executor.executeReturnDouble(this, source);
  }
}
