package tech.intellispaces.framework.core.traverse;

import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;
import tech.intellispaces.framework.core.exception.TraverseException;

public class MapObjectHandleThruTransition2PlanImpl extends AbstractObjectHandleTraversePlan
    implements MapObjectHandleThruTransition2Plan
{
  public MapObjectHandleThruTransition2PlanImpl(Class<?> objectHandleClass, String tid) {
    super(objectHandleClass, tid);
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.MapObjectHandleThruTransition2;
  }

  @Override
  public Object execute(Object source, TraverseExecutor traverseExecutor) {
    throw UnexpectedViolationException.withMessage("Expected traverse with two qualifiers");
  }

  @Override
  public Object execute(Object source, Object qualifier, TraverseExecutor traverseExecutor) throws TraverseException {
    throw UnexpectedViolationException.withMessage("Expected traverse with two qualifiers");
  }

  @Override
  public Object execute(
      Object source, Object qualifier1, Object qualifier2, TraverseExecutor traverseExecutor
  ) throws TraverseException {
    return traverseExecutor.execute(this, source, qualifier1, qualifier2);
  }
}
