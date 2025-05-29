package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.reflections.framework.system.TraverseExecutor;

public class AscendAndExecutePlan1Impl extends AbstractTraversePlan implements AscendAndExecutePlan1 {
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
  public Object execute(Object source, Object qualifier, TraverseExecutor executor) {
    return executor.execute(this, source, qualifier);
  }
}
