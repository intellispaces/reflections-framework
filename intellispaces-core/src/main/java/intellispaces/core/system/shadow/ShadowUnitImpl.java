package intellispaces.core.system.shadow;

import intellispaces.actions.Action;
import intellispaces.actions.Actions;
import intellispaces.actions.getter.ResettableGetter;
import intellispaces.core.guide.Guide;
import intellispaces.core.system.UnitProjectionDefinition;
import intellispaces.core.system.UnitProjectionInjection;
import intellispaces.core.system.UnitWrapper;
import intellispaces.core.system.action.InvokeUnitMethodAction;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class ShadowUnitImpl implements ShadowUnit {
  private final boolean main;
  private final Class<?> unitClass;
  private List<ResettableGetter<Action>> guideActions = List.of();
  private UnitWrapper instance;
  private List<UnitProjectionInjection> projectionInjections = List.of();
  private List<UnitProjectionDefinition> projectionDefinitions = List.of();
  private List<Guide<?, ?>> guides = List.of();
  private Method startupMethod;
  private Method shutdownMethod;
  private Action startupAction;
  private Action shutdownAction;

  ShadowUnitImpl(boolean main, Class<?> unitClass) {
    this.main = main;
    this.unitClass = unitClass;
  }

  @Override
  public Object instance() {
    return instance;
  }

  @Override
  public boolean isMain() {
    return main;
  }

  @Override
  public Class<?> unitClass() {
    return unitClass;
  }

  public void setInstance(UnitWrapper instance) {
    this.instance = instance;
  }

  @Override
  public List<UnitProjectionDefinition> projectionDefinitions() {
    return projectionDefinitions;
  }

  @Override
  public void setProjectionDefinitions(UnitProjectionDefinition... projectionDefinitions) {
    if (projectionDefinitions == null) {
      this.projectionDefinitions = List.of();
    } else {
      this.projectionDefinitions = Arrays.stream(projectionDefinitions).toList();
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
  public UnitProjectionInjection projectionInjection(int injectionIndex) {
    return projectionInjections.get(injectionIndex);
  }

  @Override
  public List<UnitProjectionInjection> projectionInjections() {
    return projectionInjections;
  }

  @Override
  public void setProjectionInjections(UnitProjectionInjection... projectionInjections) {
    if (projectionInjections == null) {
      this.projectionInjections = List.of();
    } else {
      this.projectionInjections = Arrays.stream(projectionInjections).toList();
    }
  }

  @Override
  public List<Guide<?, ?>> guides() {
    return guides;
  }

  public void setGuides(List<Guide<?, ?>> guides) {
    this.guides = guides;
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
