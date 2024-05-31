package tech.intellispaces.framework.core.system;

import tech.intellispaces.framework.commons.action.Getter;
import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;

public class ProjectionInjectionImpl implements ProjectionInjection {
  private final String name;
  private final Class<?> unitClass;
  private final Class<?> targetClass;
  private final Getter<ProjectionRegistry> projectionRegistryGetter;
  private Object value;

  public ProjectionInjectionImpl(
      String name, Class<?> unitClass, Class<?> targetClass, Getter<ProjectionRegistry> projectionRegistryGetter
  ) {
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
      value = projectionRegistryGetter.get().getProjectionTarget(name, targetClass);
      if (value == null) {
        throw UnexpectedViolationException.withMessage("Value of injection '{}' in unit {} is not defined",
            name(), unitClass.getCanonicalName());
      }
    }
    return value;
  }
}
