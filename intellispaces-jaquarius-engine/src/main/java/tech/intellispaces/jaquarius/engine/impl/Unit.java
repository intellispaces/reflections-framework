package tech.intellispaces.jaquarius.engine.impl;

import tech.intellispaces.action.Action;
import tech.intellispaces.action.Actions;
import tech.intellispaces.action.supplier.ResettableSupplierAction;
import tech.intellispaces.jaquarius.action.InvokeUnitMethodAction;
import tech.intellispaces.jaquarius.engine.UnitAgent;
import tech.intellispaces.jaquarius.system.Injection;
import tech.intellispaces.jaquarius.system.UnitGuide;
import tech.intellispaces.jaquarius.system.UnitProjectionDefinition;
import tech.intellispaces.jaquarius.system.UnitWrapper;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class Unit implements tech.intellispaces.jaquarius.system.Unit, UnitAgent {
  private final boolean main;
  private final Class<?> unitClass;
  private List<ResettableSupplierAction<Action>> guideActions = List.of();
  private UnitWrapper wrapper;
  private List<Injection> injections = List.of();
  private List<UnitProjectionDefinition> projectionDefinitions = List.of();
  private List<UnitGuide<?, ?>> guides = List.of();
  private Method startupMethod;
  private Method shutdownMethod;
  private Action startupAction;
  private Action shutdownAction;

  Unit(boolean main, Class<?> unitClass) {
    this.main = main;
    this.unitClass = unitClass;
  }

  public Object wrapper() {
    return wrapper;
  }

  @Override
  public boolean isMain() {
    return main;
  }

  @Override
  public Class<?> unitClass() {
    return unitClass;
  }

  public void setWrapper(UnitWrapper instance) {
    this.wrapper = instance;
  }

  @Override
  public List<UnitProjectionDefinition> projectionDefinitions() {
    return projectionDefinitions;
  }

  public void setProjectionDefinitions(List<UnitProjectionDefinition> projectionDefinitions) {
    if (projectionDefinitions == null) {
      this.projectionDefinitions = List.of();
    } else {
      this.projectionDefinitions = List.copyOf(projectionDefinitions);
    }
  }

  public void setStartupAction(InvokeUnitMethodAction<Void> startupAction) {
    this.startupAction = startupAction;
    if (startupAction != null) {
      this.startupMethod = startupAction.getUnitMethod();
    }
  }

  public void setShutdownAction(InvokeUnitMethodAction<Void> shutdownAction) {
    this.shutdownAction = shutdownAction;
    if (shutdownAction != null) {
      this.shutdownMethod = shutdownAction.getUnitMethod();
    }
  }

  @Override
  public Injection injection(int ordinal) {
    return injections.get(ordinal);
  }

  public List<Injection> injections() {
    return injections;
  }

  public void setInjections(List<Injection> injections) {
    if (injections == null) {
      this.injections = List.of();
    } else {
      this.injections = List.copyOf(injections);
    }
  }

  @Override
  public List<UnitGuide<?, ?>> guides() {
    return guides;
  }

  void setGuides(List<UnitGuide<?, ?>> guides) {
    this.guides = guides;
  }

  public Optional<Method> startupMethod() {
    return Optional.ofNullable(startupMethod);
  }

  public Optional<Method> shutdownMethod() {
    return Optional.ofNullable(shutdownMethod);
  }

  public Optional<Action> startupAction() {
    return Optional.ofNullable(startupAction);
  }

  public Optional<Action> shutdownAction() {
    return Optional.ofNullable(shutdownAction);
  }

  public void setStartupAction(Action action) {
    this.startupAction = action;
  }

  public void setShutdownAction(Action action) {
    this.shutdownAction = action;
  }

  public void setGuideActions(List<Action> actions) {
    if (actions == null) {
      guideActions = List.of();
      return;
    }
    guideActions = actions.stream()
        .map(Actions::resettableSupplierAction)
        .toList();
  }

  public void setGuideAction(int index, Action action) {
    guideActions.get(index).set(action);
  }

  @SuppressWarnings("unchecked, rawtypes")
  public List<Action> guideActions() {
    return (List) guideActions;
  }

  public int numberGuides() {
    return guideActions.size();
  }

  @Override
  public Action guideAction(int guideOrdinal) {
    return guideActions.get(guideOrdinal).get();
  }
}
