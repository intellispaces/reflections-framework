package tech.intellispaces.jaquarius.engine.impl;

import tech.intellispaces.action.Action;
import tech.intellispaces.action.Actions;
import tech.intellispaces.action.supplier.ResettableSupplierAction;
import tech.intellispaces.jaquarius.engine.UnitBroker;
import tech.intellispaces.jaquarius.system.Injection;
import tech.intellispaces.jaquarius.system.UnitGuide;
import tech.intellispaces.jaquarius.system.UnitProjectionDefinition;
import tech.intellispaces.jaquarius.system.UnitWrapper;

import java.util.List;
import java.util.Optional;

class Unit implements tech.intellispaces.jaquarius.system.Unit, UnitBroker {
  private boolean main;
  private final Class<?> unitClass;
  private List<ResettableSupplierAction<Action>> guideActions = List.of();
  private UnitWrapper wrapper;
  private List<Injection> injections = List.of();
  private List<UnitProjectionDefinition> projectionDefinitions = List.of();
  private List<UnitGuide<?, ?>> guides = List.of();
  private Action startupAction;
  private Action shutdownAction;

  Unit(Class<?> unitClass) {
    this.unitClass = unitClass;
  }

  void setMain(boolean main) {
    this.main = main;
  }

  void setWrapper(UnitWrapper instance) {
    this.wrapper = instance;
  }

  void setProjectionDefinitions(List<UnitProjectionDefinition> projectionDefinitions) {
    if (projectionDefinitions == null) {
      this.projectionDefinitions = List.of();
    } else {
      this.projectionDefinitions = List.copyOf(projectionDefinitions);
    }
  }

  void setInjections(List<Injection> injections) {
    if (injections == null) {
      this.injections = List.of();
    } else {
      this.injections = List.copyOf(injections);
    }
  }

  void setGuides(List<UnitGuide<?, ?>> guides) {
    this.guides = guides;
  }

  void setStartupAction(Action action) {
    this.startupAction = action;
  }

  void setShutdownAction(Action action) {
    this.shutdownAction = action;
  }

  void setGuideActions(List<Action> actions) {
    if (actions == null) {
      guideActions = List.of();
      return;
    }
    guideActions = actions.stream()
        .map(Actions::resettableSupplierAction)
        .toList();
  }

  void setGuideAction(int index, Action action) {
    guideActions.get(index).set(action);
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

  @Override
  public List<UnitProjectionDefinition> projectionDefinitions() {
    return projectionDefinitions;
  }

  @Override
  public Injection injection(int ordinal) {
    return injections.get(ordinal);
  }

  public List<Injection> injections() {
    return injections;
  }

  @Override
  public List<UnitGuide<?, ?>> guides() {
    return guides;
  }

  public Optional<Action> startupAction() {
    return Optional.ofNullable(startupAction);
  }

  public Optional<Action> shutdownAction() {
    return Optional.ofNullable(shutdownAction);
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
