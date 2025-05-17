package tech.intellispaces.reflections.framework.system;

import java.util.List;
import java.util.Optional;

import tech.intellispaces.actions.Action;
import tech.intellispaces.actions.Actions;
import tech.intellispaces.actions.supplier.ResettableSupplierAction;

public class UnitHandleImpl implements UnitHandle {
  private boolean main;
  private Class<?> unitClass;
  private List<ResettableSupplierAction<Action>> guideActions = List.of();
  private UnitWrapper unitInstance;
  private List<Injection> injections = List.of();
  private List<UnitProjectionDefinition> projectionDefinitions = List.of();
  private List<UnitGuide<?, ?>> guides = List.of();
  private Action startupAction;
  private Action shutdownAction;

  public void setUnitClass(Class<?> unitClass) {
    this.unitClass = unitClass;
  }

  public void setMain(boolean main) {
    this.main = main;
  }

  public void setUnitInstance(UnitWrapper instance) {
    this.unitInstance = instance;
  }

  public void setProjectionDefinitions(List<UnitProjectionDefinition> projectionDefinitions) {
    if (projectionDefinitions == null) {
      this.projectionDefinitions = List.of();
    } else {
      this.projectionDefinitions = List.copyOf(projectionDefinitions);
    }
  }

  public void setInjections(List<Injection> injections) {
    if (injections == null) {
      this.injections = List.of();
    } else {
      this.injections = List.copyOf(injections);
    }
  }

  public void setGuides(List<UnitGuide<?, ?>> guides) {
    this.guides = guides;
  }

  @Override
  public void setStartupAction(Action action) {
    this.startupAction = action;
  }

  @Override
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

  @Override
  public void setGuideAction(int index, Action action) {
    guideActions.get(index).set(action);
  }

  @Override
  public UnitWrapper unitInstance() {
    return unitInstance;
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

  @Override
  public List<Injection> injections() {
    return injections;
  }

  @Override
  public List<UnitGuide<?, ?>> guides() {
    return guides;
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
  @SuppressWarnings("unchecked, rawtypes")
  public List<Action> guideActions() {
    return (List) guideActions;
  }

  @Override
  public Action guideAction(int ordinal) {
    return guideActions.get(ordinal).get();
  }
}
