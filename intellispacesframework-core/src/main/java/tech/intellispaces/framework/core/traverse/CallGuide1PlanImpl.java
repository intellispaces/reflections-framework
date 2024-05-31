package tech.intellispaces.framework.core.traverse;

import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;
import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.n1.Guide1;

public class CallGuide1PlanImpl implements CallGuide1Plan {
  private final Guide1<Object, Object, Object> guide;

  @SuppressWarnings("unchecked")
  public CallGuide1PlanImpl(Guide1<?, ?, ?> guide) {
    this.guide = (Guide1<Object, Object, Object>) guide;
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.CallGuide1;
  }

  @Override
  public Guide1<?, ?, ?> guide() {
    return guide;
  }

  @Override
  public Object execute(Object source, TraverseExecutor traverseExecutor) {
    throw UnexpectedViolationException.withMessage("Expected traverse with one transition qualifier");
  }

  @Override
  public Object execute(Object source, Object qualifier, TraverseExecutor traverseExecutor) throws TraverseException {
    return traverseExecutor.execute(this, source, qualifier);
  }
}
