package intellispaces.core.traverse;

import intellispaces.commons.exception.UnexpectedViolationException;
import intellispaces.core.exception.TraverseException;

public class MoveObjectHandleThruTransition1PlanImpl extends AbstractObjectHandleTraversePlan
    implements MoveObjectHandleThruTransition1Plan
{
  public MoveObjectHandleThruTransition1PlanImpl(Class<?> objectHandleClass, String tid) {
    super(objectHandleClass, tid);
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.MoveObjectHandleThruTransition1;
  }

  @Override
  public Object execute(Object source, TraverseExecutor traverseExecutor) {
    throw UnexpectedViolationException.withMessage("Expected traverse with one qualifier");
  }

  @Override
  public Object execute(Object source, Object qualifier, TraverseExecutor traverseExecutor) throws TraverseException {
    return traverseExecutor.execute(this, source, qualifier);
  }

  @Override
  public Object execute(
      Object source, Object qualifier1, Object qualifier2, TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedViolationException.withMessage("Expected traverse with one qualifier");
  }
}
