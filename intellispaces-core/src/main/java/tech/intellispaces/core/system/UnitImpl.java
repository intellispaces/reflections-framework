package tech.intellispaces.core.system;

import tech.intellispaces.core.guide.Guide;
import tech.intellispaces.core.system.action.ShutdownAction;
import tech.intellispaces.core.system.action.StartupAction;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

class UnitImpl implements Unit {
  private final boolean main;
  private final Class<?> unitClass;
  private final UnitWrapper instance;
  private final List<UnitProjectionDefinition> projectionProviders;
  private final List<Guide<?, ?>> guides;
  private final StartupAction startupAction;
  private final ShutdownAction shutdownAction;

  public UnitImpl(
      boolean main,
      Class<?> unitClass,
      UnitWrapper instance,
      List<UnitProjectionDefinition> projectionProviders,
      List<Guide<?, ?>> guides,
      StartupAction startupAction,
      ShutdownAction shutdownAction
  ) {
    this.main = main;
    this.unitClass = unitClass;
    this.instance = instance;
    this.projectionProviders = Collections.unmodifiableList(projectionProviders);
    this.guides = Collections.unmodifiableList(guides);
    this.startupAction = startupAction;
    this.shutdownAction = shutdownAction;
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
  public Optional<StartupAction> startupAction() {
    return Optional.ofNullable(startupAction);
  }

  @Override
  public Optional<ShutdownAction> shutdownAction() {
    return Optional.ofNullable(shutdownAction);
  }
}
