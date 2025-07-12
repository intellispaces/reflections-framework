package tech.intellispaces.reflections.framework.task.plan;

import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.ReflectionPoint;

/**
 * The declarative traverse plan to map specified source and specified qualifier to specified target domain
 * and specified target reflection class.
 */
public interface MapSpecifiedSourceAndQualifierToSpecifiedTargetDomainAndClassPlan
    extends SpecifiedSourceDeclarativeTraversePlan
{
  ReflectionPoint source();

  ReflectionDomain targetDomain();

  Object qualifier();

  Class<?> targetClass();
}
