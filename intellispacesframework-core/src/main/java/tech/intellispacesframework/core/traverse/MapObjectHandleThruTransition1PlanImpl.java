package tech.intellispacesframework.core.traverse;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.exception.TraverseException;

public class MapObjectHandleThruTransition1PlanImpl extends AbstractObjectHandleTraversePlan
    implements MapObjectHandleThruTransition1Plan
{
  public MapObjectHandleThruTransition1PlanImpl(Class<?> objectHandleClass, String tid) {
    super(objectHandleClass, tid);
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.MapObjectHandleThruTransition1;
  }

  @Override
  public Object execute(Object source, TraverseExecutor traverseExecutor) {
    throw UnexpectedViolationException.withMessage("Expected traverse with one qualifier");
  }

  @Override
  public Object execute(Object source, Object qualifier, TraverseExecutor traverseExecutor) throws TraverseException {
    return traverseExecutor.execute(this, source, qualifier);
  }
}
