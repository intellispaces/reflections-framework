package tech.intellispacesframework.core.system;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

class SystemUnitDefault implements SystemUnit {
  private final boolean main;
  private final Class<?> unitClass;
  private final Object instance;
  private final List<UnitProjectionProvider> projectionProviders;
  private final Method startupMethod;
  private final Method shutdownMethod;

  public SystemUnitDefault(
      boolean main,
      Class<?> unitClass,
      Object instance,
      List<UnitProjectionProvider> projectionProviders,
      Method startupMethod,
      Method shutdownMethod
  ) {
    this.main = main;
    this.unitClass = unitClass;
    this.instance = instance;
    this.projectionProviders = projectionProviders;
    this.startupMethod = startupMethod;
    this.shutdownMethod = shutdownMethod;
  }

  @Override
  public boolean isMain() {
    return main;
  }

  @Override
  public Class<?> unitClass() {
    return unitClass;
  }

  @Override
  public Object instance() {
    return instance;
  }

  @Override
  public List<UnitProjectionProvider> projectionProviders() {
    return projectionProviders;
  }

  @Override
  public Optional<Method> startupMethod() {
    return Optional.ofNullable(startupMethod);
  }

  @Override
  public Optional<Method> shutdownMethod() {
    return Optional.ofNullable(shutdownMethod);
  }
}
