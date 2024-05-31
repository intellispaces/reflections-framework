package tech.intellispacesframework.core.system;

import tech.intellispacesframework.core.guide.Guide;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class UnitImpl implements Unit {
  private final boolean main;
  private final Class<?> unitClass;
  private final UnitWrapper instance;
  private final List<UnitProjectionDefinition> projectionProviders;
  private final List<Guide<?, ?>> guides;
  private final Method startupMethod;
  private final Method shutdownMethod;

  public UnitImpl(
      boolean main,
      Class<?> unitClass,
      UnitWrapper instance,
      List<UnitProjectionDefinition> projectionProviders,
      List<Guide<?, ?>> guides,
      Method startupMethod,
      Method shutdownMethod
  ) {
    this.main = main;
    this.unitClass = unitClass;
    this.instance = instance;
    this.projectionProviders = Collections.unmodifiableList(projectionProviders);
    this.guides = Collections.unmodifiableList(guides);
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
  public List<Injection> injections() {
    return instance.getInjections();
  }

  @Override
  public List<UnitProjectionDefinition> projectionProviders() {
    return projectionProviders;
  }

  @Override
  public List<Guide<?, ?>> guides() {
    return guides;
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
