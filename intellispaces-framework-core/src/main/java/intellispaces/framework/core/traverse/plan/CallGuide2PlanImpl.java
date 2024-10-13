package intellispaces.framework.core.traverse.plan;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.n2.Guide2;

public class CallGuide2PlanImpl implements CallGuide2Plan {
  private final Guide2<Object, Object, Object, Object> guide;

  @SuppressWarnings("unchecked")
  public CallGuide2PlanImpl(Guide2<?, ?, ?, ?> guide) {
    this.guide = (Guide2<Object, Object, Object, Object>) guide;
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.CallGuide2;
  }

  @Override
  public Guide2<?, ?, ?, ?> guide() {
    return guide;
  }

  @Override
  public Object execute(
      Object source, TraverseExecutor executor
  ) {
    throw UnexpectedViolationException.withMessage("Expected traverse with two channel qualifiers");
  }

  @Override
  public int executeReturnInt(Object source, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedViolationException.withMessage("Expected traverse with two channel qualifiers");
  }

  @Override
  public double executeReturnDouble(Object source, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedViolationException.withMessage("Expected traverse with two channel qualifiers");
  }

  @Override
  public Object execute(
      Object source, Object qualifier, TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedViolationException.withMessage("Expected traverse with two channel qualifiers");
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
    throw UnexpectedViolationException.withMessage("Expected traverse with two channel qualifiers");
  }
}
