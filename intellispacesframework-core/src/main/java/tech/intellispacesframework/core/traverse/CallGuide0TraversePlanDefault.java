package tech.intellispacesframework.core.traverse;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.guide.n0.Guide0;

public class CallGuide0TraversePlanDefault implements CallGuide0TraversePlan {
  private final Guide0<Object, Object> guide;

  @SuppressWarnings("unchecked")
  public CallGuide0TraversePlanDefault(Guide0<?, ?> guide) {
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
