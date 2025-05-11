package tech.intellispaces.reflections.framework.system.injection;

import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflections.framework.system.InjectionKind;
import tech.intellispaces.reflections.framework.system.Modules;
import tech.intellispaces.reflections.framework.system.ProjectionInjection;

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
  public InjectionKind kind() {
    return InjectionKinds.Projection;
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
        throw UnexpectedExceptions.withMessage("Target of projection injection '{0}' in unit {1} " +
                "is not defined", name(), unitClass.getCanonicalName());
      }
    }
    return projectionTarget;
  }
}
