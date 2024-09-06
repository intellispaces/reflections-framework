package intellispaces.framework.core.system.kernel;

import intellispaces.common.action.Action;
import intellispaces.common.action.Actions;
import intellispaces.common.action.getter.ResettableGetter;
import intellispaces.framework.core.system.AttachedUnitGuide;
import intellispaces.framework.core.system.Injection;
import intellispaces.framework.core.system.UnitProjectionDefinition;
import intellispaces.framework.core.system.UnitWrapper;
import intellispaces.framework.core.system.action.InvokeUnitMethodAction;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class UnitImpl implements KernelUnit {
  private final boolean main;
  private final Class<?> unitClass;
  private List<ResettableGetter<Action>> guideActions = List.of();
  private UnitWrapper instance;
  private List<Injection> injections = List.of();
  private List<UnitProjectionDefinition> projectionDefinitions = List.of();
  private List<AttachedUnitGuide> guides = List.of();
  private Method startupMethod;
  private Method shutdownMethod;
  private Action startupAction;
  private Action shutdownAction;

  UnitImpl(boolean main, Class<?> unitClass) {
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
  public Injection injection(int ordinal) {
    return injections.get(ordinal);
  }

  @Override
  public List<Injection> injections() {
    return injections;
  }

  @Override
  public void setInjections(Injection... projectionInjections) {
    if (projectionInjections == null) {
      this.injections = List.of();
    } else {
      this.injections = Arrays.stream(projectionInjections).toList();
    }
  }

  @Override
  public List<AttachedUnitGuide> guides() {
    return guides;
  }

  void setGuides(List<AttachedUnitGuide> guides) {
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
