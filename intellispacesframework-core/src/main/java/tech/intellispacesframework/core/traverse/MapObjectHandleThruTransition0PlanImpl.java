package tech.intellispacesframework.core.traverse;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.exception.TraverseException;

public class MapObjectHandleThruTransition0PlanImpl extends AbstractObjectHandleTraversePlan
    implements MapObjectHandleThruTransition0Plan
{
  public MapObjectHandleThruTransition0PlanImpl(Class<?> objectHandleClass, String tid) {
    super(objectHandleClass, tid);
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.MapObjectHandleThruTransition0;
  }

  @Override
  public Object execute(Object source, TraverseExecutor traverseExecutor) throws TraverseException {
    return traverseExecutor.execute(this, source);
  }

  @Override
  public Object execute(Object source, Object qualifier, TraverseExecutor traverseExecutor) {
    throw UnexpectedViolationException.withMessage("Expected traverse with no qualifier");
  }
}
