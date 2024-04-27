package tech.intellispacesframework.core.system;

import java.lang.reflect.Method;

class UnitProjectionProviderDefault implements UnitProjectionProvider {
  private final Class<?> type;
  private final String name;
  private final SystemUnit unit;
  private final Method providerMethod;

  public UnitProjectionProviderDefault(Class<?> type, String name, SystemUnit unit, Method providerMethod) {
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
  public Class<?> type() {
    return type;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public SystemUnit unit() {
    return unit;
  }

  @Override
  public Method providerMethod() {
    return providerMethod;
  }
}
