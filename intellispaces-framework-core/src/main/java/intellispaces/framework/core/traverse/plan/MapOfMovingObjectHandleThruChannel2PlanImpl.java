package intellispaces.framework.core.traverse.plan;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.exception.TraverseException;

public class MapOfMovingObjectHandleThruChannel2PlanImpl extends AbstractObjectHandleTraversePlan
    implements MapOfMovingObjectHandleThruChannel2Plan
{
  public MapOfMovingObjectHandleThruChannel2PlanImpl(Class<?> objectHandleClass, String cid) {
    super(objectHandleClass, cid);
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.MapOfMovingObjectHandleThruChannel2;
  }

  @Override
  public Object execute(Object source, TraverseExecutor executor) {
    throw UnexpectedViolationException.withMessage("Expected traverse with two qualifiers");
  }

  @Override
  public int executeReturnInt(Object source, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedViolationException.withMessage("Expected traverse with two qualifiers");
  }

  @Override
  public double executeReturnDouble(Object source, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedViolationException.withMessage("Expected traverse with two qualifiers");
  }

  @Override
  public Object execute(Object source, Object qualifier, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedViolationException.withMessage("Expected traverse with two qualifiers");
  }

  @Override
  public Object execute(
      Object source, Object qualifier1, Object qualifier2, TraverseExecutor executor
  ) throws TraverseException {
    return executor.execute(this, source, qualifier1, qualifier2);
  }

  @Override
  public Object execute(
    Object source, Object qualifier1, Object qualifier2, Object qualifier3, TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedViolationException.withMessage("Expected traverse with two qualifiers");
  }
}
