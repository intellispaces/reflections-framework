package tech.intellispacesframework.core.system;

import tech.intellispacesframework.commons.action.Getter;
import tech.intellispacesframework.commons.exception.UnexpectedViolationException;

class ProjectionInjectionDefault implements ProjectionInjection {
  private final String name;
  private final Class<?> unitClass;
  private final Class<?> targetClass;
  private final Getter<ProjectionRegistry> projectionRegistryGetter;
  private Object value;

  ProjectionInjectionDefault(String name, Class<?> unitClass, Class<?> targetClass, Getter<ProjectionRegistry> projectionRegistryGetter) {
    this.name = name;
    this.unitClass = unitClass;
    this.targetClass = targetClass;
    this.projectionRegistryGetter = projectionRegistryGetter;
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
    return value != null;
  }

  @Override
  public Object value() {
    if (value == null) {
      value = projectionRegistryGetter.get().projection(name, targetClass);
      if (value == null) {
        throw UnexpectedViolationException.withMessage("Target of projection injection '{}' is not defined. Unit {}",
            name(), unitClass.getCanonicalName());
      }
    }
    return value;
  }
}
