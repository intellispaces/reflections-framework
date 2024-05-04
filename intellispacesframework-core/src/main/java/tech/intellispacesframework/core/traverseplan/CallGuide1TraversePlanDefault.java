package tech.intellispacesframework.core.traverseplan;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.guide.n1.Guide1;

public class CallGuide1TraversePlanDefault implements CallGuide1TraversePlan {
  private final Guide1<Object, Object, Object> guide;

  @SuppressWarnings("unchecked")
  public CallGuide1TraversePlanDefault(Guide1<?, ?, ?> guide) {
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
  public Object traverse(Object source) {
    throw UnexpectedViolationException.withMessage("Expected traverse with one qualifier");
  }

  @Override
  public Object traverse(Object source, Object qualifier) throws TraverseException {
    return guide.traverse(source, qualifier);
  }
}
