package tech.intellispaces.reflections.framework.task.plan;

import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.ReflectionDomain;

/**
 * The declarative traverse plan to map specified source to specified target domain
 * and specified target reflection class.
 */
public interface MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan
    extends SpecifiedSourceDeclarativeTraversePlan
{
  Reflection source();

  ReflectionDomain targetDomain();

  Class<?> targetClass();
}
