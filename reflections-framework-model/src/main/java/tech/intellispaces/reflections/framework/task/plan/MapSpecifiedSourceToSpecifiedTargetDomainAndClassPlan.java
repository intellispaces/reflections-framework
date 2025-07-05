package tech.intellispaces.reflections.framework.task.plan;

import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.ReflectionPoint;

/**
 * The declarative traverse plan to map specified source to specified target domain
 * and specified target reflection class.
 */
public interface MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan
    extends SpecifiedSourceDeclarativeTraversePlan
{
  ReflectionPoint source();

  ReflectionDomain targetDomain();

  Class<?> targetClass();
}
