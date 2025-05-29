package tech.intellispaces.reflections.framework.traverse.plan;

public interface SpecifiedSourceDeclarativeTraversePlan extends DeclarativeTraversePlan {

  ExecutionTraversePlan executionPlan();

  void setExecutionPlan(ExecutionTraversePlan executionPlan);
}
