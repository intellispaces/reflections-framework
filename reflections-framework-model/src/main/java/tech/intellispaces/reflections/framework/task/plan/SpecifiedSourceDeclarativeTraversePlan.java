package tech.intellispaces.reflections.framework.task.plan;

public interface SpecifiedSourceDeclarativeTraversePlan extends DeclarativeTraversePlan {

  ExecutionTraversePlan executionPlan();

  void setExecutionPlan(ExecutionTraversePlan executionPlan);
}
