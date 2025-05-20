package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class AscendAndExecutePlan1Impl implements AscendAndExecutePlan1 {
  private final ExecutionTraversePlan executionPlan;

  public AscendAndExecutePlan1Impl(ExecutionTraversePlan executionPlan) {
    this.executionPlan = executionPlan;
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.AscendAndExecute1;
  }

  @Override
  public ExecutionTraversePlan executionPlan() {
    return executionPlan;
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
  public Object execute(Object source, Object qualifier, TraverseExecutor executor) {
    return executor.execute(this, source, qualifier);
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
