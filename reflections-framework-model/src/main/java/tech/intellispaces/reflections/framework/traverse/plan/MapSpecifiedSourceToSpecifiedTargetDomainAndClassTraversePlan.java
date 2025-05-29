package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.core.Domain;
import tech.intellispaces.core.Reflection;

/**
 * The declarative traverse plan to map specified source to specified target domain
 * and specified target reflection class.
 */
public interface MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlan
    extends SpecifiedSourceDeclarativeTraversePlan
{
  Reflection source();

  Domain targetDomain();

  Class<?> targetClass();
}
