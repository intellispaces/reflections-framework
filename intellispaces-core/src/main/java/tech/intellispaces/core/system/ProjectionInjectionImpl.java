package tech.intellispaces.core.system;

import tech.intellispaces.actions.Action2;
import tech.intellispaces.commons.exception.UnexpectedViolationException;

public class ProjectionInjectionImpl implements ProjectionInjection {
  private final String name;
  private final Class<?> unitClass;
  private final Class<?> targetClass;
  private final Action2<Object, String, Class<?>> projectionTargetGetter;
  private Object projectionTarget;

  public ProjectionInjectionImpl(
      String name, Class<?> unitClass, Class<?> targetClass, Action2<Object, String, Class<?>> projectionTargetGetter
  ) {
    this.name = name;
    this.unitClass = unitClass;
    this.targetClass = targetClass;
    this.projectionTargetGetter = projectionTargetGetter;
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
      projectionTarget = projectionTargetGetter.execute(name, targetClass);
      if (projectionTarget == null) {
        throw UnexpectedViolationException.withMessage("Value of injection '{}' in unit {} is not defined",
            name(), unitClass.getCanonicalName());
      }
    }
    return projectionTarget;
  }
}
