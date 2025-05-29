package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.core.Domain;
import tech.intellispaces.core.Reflection;

/**
 * The declarative traverse plan to map specific reflection to specific domain and specific target class.
 */
public interface MapSpecificReflectionToSpecificDomainAndClassTraversePlan extends DeclarativeTraversePlan {

  Reflection source();

  Domain targetDomain();

  Class<?> targetClass();

  ExecutionTraversePlan executionPlan();

  void setExecutionPlan(ExecutionTraversePlan executionPlan);
}
