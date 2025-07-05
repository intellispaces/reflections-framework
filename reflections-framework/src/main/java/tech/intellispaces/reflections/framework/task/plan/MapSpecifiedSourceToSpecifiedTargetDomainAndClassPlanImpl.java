package tech.intellispaces.reflections.framework.task.plan;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.ReflectionPoint;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlanImpl
    implements MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan
{
  private final ReflectionPoint source;
  private final ReflectionDomain targetDomain;
  private final Class<?> targetClas;
  private ExecutionTraversePlan executionPlan;

  public MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlanImpl(
      ReflectionPoint source,
      ReflectionDomain targetDomain,
      Class<?> targetClas
  ) {
    this.source = source;
    this.targetDomain = targetDomain;
    this.targetClas = targetClas;
  }

  @Override
  public ReflectionPoint source() {
    return source;
  }

  @Override
  public ReflectionDomain targetDomain() {
    return targetDomain;
  }

  @Override
  public Class<?> targetClass() {
    return targetClas;
  }

  @Override
  public TaskPlanType type() {
    return TaskPlanTypes.MapSpecifiedSourceToSpecifiedTargetDomainAndClass;
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
