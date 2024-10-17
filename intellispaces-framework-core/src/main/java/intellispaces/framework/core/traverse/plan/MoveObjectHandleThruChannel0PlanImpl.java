package intellispaces.framework.core.traverse.plan;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.exception.TraverseException;

public class MoveObjectHandleThruChannel0PlanImpl extends AbstractObjectHandleTraversePlan
    implements MoveObjectHandleThruChannel0Plan
{
  public MoveObjectHandleThruChannel0PlanImpl(Class<?> objectHandleClass, String cid) {
    super(objectHandleClass, cid);
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.MoveObjectHandleThruChannel0;
  }

  @Override
  public Object execute(Object source, TraverseExecutor executor) throws TraverseException {
    return executor.execute(this, source);
  }

  @Override
  public int executeReturnInt(Object source, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedViolationException.withMessage("Invalid operation");
  }

  @Override
  public double executeReturnDouble(Object source, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedViolationException.withMessage("Invalid operation");
  }

  @Override
  public Object execute(Object source, Object qualifier, TraverseExecutor executor) {
    throw UnexpectedViolationException.withMessage("Expected traverse with no qualifier");
  }

  @Override
  public Object execute(
      Object source, Object qualifier1, Object qualifier2, TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedViolationException.withMessage("Expected traverse with no qualifier");
  }

  @Override
  public Object execute(
    Object source, Object qualifier1, Object qualifier2, Object qualifier3, TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedViolationException.withMessage("Expected traverse with no qualifier");
  }

  @Override
  public Object execute(
      Object source, Object qualifier1, Object qualifier2, Object qualifier3, Object qualifier4, TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedViolationException.withMessage("Expected traverse with no qualifier");
  }
}
