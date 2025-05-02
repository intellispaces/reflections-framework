package tech.intellispaces.reflectionsj.traverse.plan;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflectionsj.exception.TraverseException;
import tech.intellispaces.reflectionsj.guide.n0.Guide0;

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
  public Object execute(Object source, TraverseExecutor executor) throws TraverseException {
    return executor.execute(this, source);
  }

  @Override
  public int executeReturnInt(Object source, TraverseExecutor executor) throws TraverseException {
    return executor.executeReturnInt(this, source);
  }

  @Override
  public double executeReturnDouble(Object source, TraverseExecutor executor) throws TraverseException {
    return executor.executeReturnInt(this, source);
  }

  @Override
  public Object execute(Object source, Object qualifier, TraverseExecutor executor) {
    throw UnexpectedExceptions.withMessage("Expected traverse without channel qualifier");
  }

  @Override
  public Object execute(Object source, Object qualifier1, Object qualifier2, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse without channel qualifier");
  }

  @Override
  public Object execute(
    Object source, Object qualifier1, Object qualifier2, Object qualifier3, TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse without channel qualifier");
  }

  @Override
  public Object execute(
      Object source, Object qualifier1, Object qualifier2, Object qualifier3, Object qualifier4, TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse without channel qualifier");
  }
}
