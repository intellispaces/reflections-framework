package tech.intellispacesframework.core.system;

import java.lang.reflect.Method;

class UnitProjectionDefinitionImpl implements UnitProjectionDefinition {
  private final String name;
  private final Class<?> type;
  private final Unit unit;
  private final boolean lazy;
  private final Method projectionMethod;

  public UnitProjectionDefinitionImpl(String name, Class<?> type, Unit unit, boolean lazy, Method projectionMethod) {
    this.type = type;
    this.name = name;
    this.unit = unit;
    this.lazy = lazy;
    this.projectionMethod = projectionMethod;
  }

  @Override
  public ProjectionDefinitionKind kind() {
    return ProjectionDefinitionTypes.UnitMethod;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Class<?> type() {
    return type;
  }

  @Override
  public Unit unit() {
    return unit;
  }

  @Override
  public boolean isLazy() {
    return lazy;
  }

  @Override
  public Method projectionMethod() {
    return projectionMethod;
  }
}
