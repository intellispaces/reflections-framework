package tech.intellispacesframework.core.system;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;

class ForcedProjectionInjection implements ProjectionInjection {
  private final String name;
  private final Class<?> unitClass;
  private final Class<?> targetClass;
  private Object value;

  ForcedProjectionInjection(String name, Class<?> unitClass, Class<?> targetClass) {
    this.name = name;
    this.unitClass = unitClass;
    this.targetClass = targetClass;
  }

  @Override
  public InjectionType type() {
    return InjectionTypes.ProjectionInjection;
  }

  @Override
  public boolean isLazy() {
    return false;
  }

  @Override
  public boolean isDefined() {
    return value != null;
  }

  @Override
  public Object value() {
    if (value == null) {
      throw UnexpectedViolationException.withMessage("Value of injection '{}' is not defined. Unit {}",
          name(), unitClass.getCanonicalName());
    }
    return value;
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

  void setValue(Object value) {
    this.value = value;
  }
}
