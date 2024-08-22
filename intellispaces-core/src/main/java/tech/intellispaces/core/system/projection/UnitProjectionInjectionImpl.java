package tech.intellispaces.core.system.projection;

import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.core.system.Modules;
import tech.intellispaces.core.system.UnitProjectionInjection;

class UnitProjectionInjectionImpl implements UnitProjectionInjection {
  private final Class<?> unitClass;
  private final String name;
  private final Class<?> targetClass;
  private Object projectionTarget;

  UnitProjectionInjectionImpl(Class<?> unitClass, String name, Class<?> targetClass) {
    this.unitClass = unitClass;
    this.name = name;
    this.targetClass = targetClass;
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
        throw UnexpectedViolationException.withMessage("Value of injection '{}' in unit {} is not defined",
            name(), unitClass.getCanonicalName());
      }
    }
    return projectionTarget;
  }
}
