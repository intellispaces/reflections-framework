package tech.intellispaces.framework.core.traverse;

import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;
import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.n0.Guide0;

public class CallGuide0PlanImpl implements CallGuide0Plan {
  private final Guide0<Object, Object> guide;

  @SuppressWarnings("unchecked")
  public CallGuide0PlanImpl(Guide0<?, ?> guide) {
    this.guide = (Guide0<Object, Object>) guide;
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.CallGuide0;
  }

  @Override
  public Guide0<?, ?> guide() {
    return guide;
  }

  @Override
  public Object execute(Object source, TraverseExecutor traverseExecutor) throws TraverseException {
    return traverseExecutor.execute(this, source);
  }

  @Override
  public Object execute(Object source, Object qualifier, TraverseExecutor traverseExecutor) {
    throw UnexpectedViolationException.withMessage("Expected traverse without transition qualifier");
  }
}
