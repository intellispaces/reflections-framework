package tech.intellispacesframework.core.system;

import java.lang.reflect.Method;

class UnitProjectionProviderDefault implements UnitProjectionProvider {
  private final String name;
  private final Class<?> type;
  private final Unit unit;
  private final Method providerMethod;

  public UnitProjectionProviderDefault(String name, Class<?> type, Unit unit, Method providerMethod) {
    this.type = type;
    this.name = name;
    this.unit = unit;
    this.providerMethod = providerMethod;
  }

  @Override
  public ProjectionProviderType providerType() {
    return ProjectionProviderTypes.UnitMethod;
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
  public Method providerMethod() {
    return providerMethod;
  }
}
