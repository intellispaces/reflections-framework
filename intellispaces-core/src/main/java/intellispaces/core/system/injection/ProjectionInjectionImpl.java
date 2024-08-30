package intellispaces.core.system.injection;

import intellispaces.commons.exception.UnexpectedViolationException;
import intellispaces.core.system.InjectionType;
import intellispaces.core.system.Modules;
import intellispaces.core.system.ProjectionInjection;

class ProjectionInjectionImpl implements ProjectionInjection {
  private final Class<?> unitClass;
  private final String name;
  private final Class<?> targetClass;
  private Object projectionTarget;

  ProjectionInjectionImpl(Class<?> unitClass, String name, Class<?> targetClass) {
    this.unitClass = unitClass;
    this.name = name;
    this.targetClass = targetClass;
  }

  @Override
  public InjectionType type() {
    return InjectionTypes.ProjectionInjection;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Class<?> unitClass() {
    return unitClass;
  }

  @Override
  public Class<?> targetClass() {
    return targetClass;
  }

  @Override
  public boolean isDefined() {
    return projectionTarget != null;
  }

  @Override
  public Object value() {
    if (projectionTarget == null) {
      projectionTarget = Modules.current().getProjection(name, targetClass);
      if (projectionTarget == null) {
        throw UnexpectedViolationException.withMessage("Target of projection injection ''{0}'' in unit {1} " +
                "is not defined", name(), unitClass.getCanonicalName());
      }
    }
    return projectionTarget;
  }
}
