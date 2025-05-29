package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.core.Domain;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class MapSpecificReflectionToSpecificDomainAndClassTraversePlanImpl
    implements MapSpecificReflectionToSpecificDomainAndClassTraversePlan
{
  private final Reflection source;
  private final Domain targetDomain;
  private final Class<?> targetClas;
  private ExecutionTraversePlan executionPlan;

  public MapSpecificReflectionToSpecificDomainAndClassTraversePlanImpl(
      Reflection source,
      Domain targetDomain,
      Class<?> targetClas
  ) {
    this.source = source;
    this.targetDomain = targetDomain;
    this.targetClas = targetClas;
  }

  @Override
  public Reflection source() {
    return source;
  }

  @Override
  public Domain targetDomain() {
    return targetDomain;
  }

  @Override
  public Class<?> targetClass() {
    return targetClas;
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.MapSpecificSourceToSpecificDomainAndSpecificTargetClass;
  }

  @Override
  public Object execute(
      Object source, TraverseExecutor executor
  ) throws TraverseException {
    return executor.execute(this);
  }

  @Override
  public int executeReturnInt(
      Object source, TraverseExecutor executor
  ) throws TraverseException {
    return executor.executeReturnInt(this);
  }

  @Override
  public double executeReturnDouble(
      Object source, TraverseExecutor executor
  ) throws TraverseException {
    return executor.executeReturnDouble(this);
  }

  @Override
  public Object execute(
      Object source,
      Object qualifier,
      TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with no qualifiers");
  }

  @Override
  public Object execute(
      Object source,
      Object qualifier1,
      Object qualifier2,
      TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with no qualifiers");
  }

  @Override
  public Object execute(
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3,
      TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with no qualifiers");
  }

  @Override
  public Object execute(
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3,
      Object qualifier4,
      TraverseExecutor executor
  ) throws TraverseException {
    throw UnexpectedExceptions.withMessage("Expected traverse with no qualifiers");
  }

  @Override
  public ExecutionTraversePlan executionPlan() {
    return executionPlan;
  }

  @Override
  public void setExecutionPlan(ExecutionTraversePlan executionPlan) {
    this.executionPlan = executionPlan;
  }
}
