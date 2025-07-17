package tech.intellispaces.reflections.framework.task.plan;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.core.ReflectionChannel;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class MoveSpecifiedSourceAndQualifierThruChannel1PlanImpl
    implements MoveSpecifiedSourceAndQualifierThruChannel1Plan
{
  private final Object source;
  private final ReflectionChannel channel;
  private final Object qualifier;
  private ExecutionTraversePlan executionPlan;

  public MoveSpecifiedSourceAndQualifierThruChannel1PlanImpl(
      Object source,
      ReflectionChannel channel,
      Object qualifier
  ) {
    this.source = source;
    this.channel = channel;
    this.qualifier = qualifier;
  }

  @Override
  public Object source() {
    return source;
  }

  @Override
  public ReflectionChannel channel() {
    return channel;
  }

  @Override
  public Object qualifier() {
    return qualifier;
  }

  @Override
  public TaskPlanType type() {
    return TaskPlanTypes.MoveSpecifiedSourceAndQualifierThruChannel1;
  }

  @Override
  public Object execute(TraverseExecutor executor) throws TraverseException {
    return executor.execute(this);
  }

  @Override
  public ExecutionTraversePlan executionPlan() {
    return executionPlan;
  }

  @Override
  public void setExecutionPlan(ExecutionTraversePlan executionPlan) {
    this.executionPlan = executionPlan;
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
  public Object execute(Object source, Object qualifier, TraverseExecutor executor) throws TraverseException {
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
