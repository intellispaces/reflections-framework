package intellispaces.jaquarius.traverse.plan;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.jaquarius.exception.TraverseException;
import intellispaces.jaquarius.guide.n3.Guide3;

public class CallGuide3PlanImpl implements CallGuide3Plan {
  private final Guide3<Object, Object, Object, Object, Object> guide;

  @SuppressWarnings("unchecked")
  public CallGuide3PlanImpl(Guide3<?, ?, ?, ?, ?> guide) {
    this.guide = (Guide3<Object, Object, Object, Object, Object>) guide;
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.CallGuide3;
  }

  @Override
  public Guide3<?, ?, ?, ?, ?> guide() {
    return guide;
  }

  @Override
  public Object execute(
      Object source, TraverseExecutor executor
  ) {
    throw UnexpectedViolationException.withMessage("Expected traverse with three channel qualifier");
  }

  @Override
  public int executeReturnInt(Object source, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedViolationException.withMessage("Expected traverse with three channel qualifier");
  }

  @Override
  public double executeReturnDouble(Object source, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedViolationException.withMessage("Expected traverse with three channel qualifier");
  }

  @Override
  public Object execute(
      Object source, Object qualifier, TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedViolationException.withMessage("Expected traverse with three channel qualifiers");
  }

  @Override
  public Object execute(
          Object source, Object qualifier1, Object qualifier2, TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedViolationException.withMessage("Expected traverse with three channel qualifiers");
  }

  @Override
  public Object execute(
      Object source, Object qualifier1, Object qualifier2, Object qualifier3, TraverseExecutor executor
  ) throws TraverseException {
    return executor.execute(this, source, qualifier1, qualifier2, qualifier3);
  }

  @Override
  public Object execute(
      Object source, Object qualifier1, Object qualifier2, Object qualifier3, Object qualifier4, TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedViolationException.withMessage("Expected traverse with three channel qualifiers");
  }
}
