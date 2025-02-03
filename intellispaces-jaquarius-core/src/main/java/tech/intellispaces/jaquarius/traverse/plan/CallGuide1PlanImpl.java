package tech.intellispaces.jaquarius.traverse.plan;

import tech.intellispaces.commons.base.exception.UnexpectedExceptions;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.guide.n1.Guide1;

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
  public Object execute(Object source, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with one channel qualifier");
  }

  @Override
  public int executeReturnInt(Object source, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with one channel qualifier");
  }

  @Override
  public double executeReturnDouble(Object source, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with one channel qualifier");
  }

  @Override
  public Object execute(Object source, Object qualifier, TraverseExecutor executor) throws TraverseException {
    return executor.execute(this, source, qualifier);
  }

  @Override
  public Object execute(Object source, Object qualifier1, Object qualifier2, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with one channel qualifier");
  }

  @Override
  public Object execute(
    Object source, Object qualifier1, Object qualifier2, Object qualifier3, TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with one channel qualifier");
  }

  @Override
  public Object execute(
      Object source, Object qualifier1, Object qualifier2, Object qualifier3, Object qualifier4, TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with one channel qualifier");
  }
}
