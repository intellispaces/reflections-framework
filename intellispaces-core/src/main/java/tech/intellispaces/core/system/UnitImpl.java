package tech.intellispaces.core.system;

import tech.intellispaces.actions.Action;
import tech.intellispaces.core.guide.Guide;
import tech.intellispaces.core.system.action.InvokeUnitMethodAction;

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
  private Method startupMethod;
  private Method shutdownMethod;
  private Action startupAction;
  private Action shutdownAction;

  public UnitImpl(
      boolean main,
      Class<?> unitClass,
      UnitWrapper instance,
      List<UnitProjectionDefinition> projectionProviders,
      List<Guide<?, ?>> guides,
      InvokeUnitMethodAction<Void> startupAction,
      InvokeUnitMethodAction<Void> shutdownAction
  ) {
    this.main = main;
    this.unitClass = unitClass;
    this.instance = instance;
    this.projectionProviders = Collections.unmodifiableList(projectionProviders);
    this.guides = Collections.unmodifiableList(guides);
    this.startupAction = startupAction;
    this.shutdownAction = shutdownAction;
    if (startupAction != null) {
      this.startupMethod = startupAction.getUnitMethod();
    }
    if (shutdownAction != null) {
      this.shutdownMethod = shutdownAction.getUnitMethod();
    }
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

  @Override
  public Optional<Action> startupAction() {
    return Optional.ofNullable(startupAction);
  }

  @Override
  public Optional<Action> shutdownAction() {
    return Optional.ofNullable(shutdownAction);
  }

  @Override
  public void setStartupAction(Action action) {
    this.startupAction = action;
  }

  @Override
  public void setShutdownAction(Action action) {
    this.shutdownAction = action;
  }
}
