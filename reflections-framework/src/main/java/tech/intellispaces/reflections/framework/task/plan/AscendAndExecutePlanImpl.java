package tech.intellispaces.reflections.framework.task.plan;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class AscendAndExecutePlanImpl implements AscendAndExecutePlan {
  private final ExecutionTraversePlan executionPlan;

  public AscendAndExecutePlanImpl(ExecutionTraversePlan executionPlan) {
    this.executionPlan = executionPlan;
  }

  @Override
  public TaskPlanType type() {
    return TaskPlanTypes.AscendAndExecute;
  }

  @Override
  public ExecutionTraversePlan executionPlan() {
    return executionPlan;
  }

  @Override
  public Object execute(Object source, Object qualifier, TraverseExecutor executor) {
    return executor.execute(this, source, qualifier);
  }

  @Override
  public Object execute(TraverseExecutor executor) throws TraverseException {
    throw UnexpectedExceptions.withMessage("The method being called does not match this plan");
  }

  @Override
  public Object execute(Object source, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedExceptions.withMessage("The method being called does not match this plan");
  }

  @Override
  public int executeReturnInt(Object source, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedExceptions.withMessage("The method being called does not match this plan");
  }

  @Override
  public double executeReturnDouble(Object source, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedExceptions.withMessage("The method being called does not match this plan");
  }

  @Override
  public Object execute(Object source, Object qualifier1, Object qualifier2, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedExceptions.withMessage("The method being called does not match this plan");
  }

  @Override
  public Object execute(Object source, Object qualifier1, Object qualifier2, Object qualifier3, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedExceptions.withMessage("The method being called does not match this plan");
  }

  @Override
  public Object execute(Object source, Object qualifier1, Object qualifier2, Object qualifier3, Object qualifier4, TraverseExecutor executor) throws TraverseException {
    throw UnexpectedExceptions.withMessage("The method being called does not match this plan");
  }
}
