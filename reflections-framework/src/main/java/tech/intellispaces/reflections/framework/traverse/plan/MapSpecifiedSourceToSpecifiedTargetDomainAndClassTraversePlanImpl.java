package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.core.Domain;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlanImpl
  extends AbstractTraversePlan
    implements MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlan
{
  private final Reflection source;
  private final Domain targetDomain;
  private final Class<?> targetClas;
  private ExecutionTraversePlan executionPlan;

  public MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlanImpl(
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
    return TraversePlanTypes.MapSpecifiedSourceToSpecifiedTargetDomainAndClass;
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
}
