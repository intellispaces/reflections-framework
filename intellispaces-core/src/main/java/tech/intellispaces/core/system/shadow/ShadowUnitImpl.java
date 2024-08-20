package tech.intellispaces.core.system.shadow;

import tech.intellispaces.actions.Action;
import tech.intellispaces.actions.Actions;
import tech.intellispaces.actions.getter.ResettableGetter;
import tech.intellispaces.core.guide.Guide;
import tech.intellispaces.core.system.Injection;
import tech.intellispaces.core.system.UnitProjectionDefinition;
import tech.intellispaces.core.system.UnitWrapper;
import tech.intellispaces.core.system.action.InvokeUnitMethodAction;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class ShadowUnitImpl implements ShadowUnit {
  private final boolean main;
  private final Class<?> unitClass;
  private List<ResettableGetter<Action>> guideActions = List.of();
  private UnitWrapper instance;
  private List<UnitProjectionDefinition> projectionDefinitions;
  private List<Guide<?, ?>> guides;
  private Method startupMethod;
  private Method shutdownMethod;
  private Action startupAction;
  private Action shutdownAction;

  ShadowUnitImpl(boolean main, Class<?> unitClass) {
    this.main = main;
    this.unitClass = unitClass;
  }

  public void setInstance(UnitWrapper instance) {
    this.instance = instance;
  }

  public void setProjectionDefinitions(List<UnitProjectionDefinition> projectionDefinitions) {
    this.projectionDefinitions = projectionDefinitions;
  }

  public void setGuides(List<Guide<?, ?>> guides) {
    this.guides = guides;
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
  public List<UnitProjectionDefinition> projectionDefinitions() {
    return projectionDefinitions;
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

  @Override
  public void setGuideActions(Action... actions) {
    if (actions == null) {
      guideActions = List.of();
      return;
    }
    guideActions = Arrays.stream(actions)
        .map(Actions::resettableGetter)
        .toList();
  }

  @Override
  public int numberGuides() {
    return guideActions.size();
  }

  @Override
  public Action getGuideAction(int index) {
    return guideActions.get(index).get();
  }
}
