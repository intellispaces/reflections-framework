package intellispaces.framework.core.system.kernel;

import intellispaces.common.action.Action;
import intellispaces.common.action.Actions;
import intellispaces.common.action.getter.ResettableGetter;
import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.system.Injection;

import java.util.Arrays;
import java.util.List;

class ObjectHandleImpl implements KernelObjectHandle {
  private List<ResettableGetter<Action>> methodActions = List.of();
  private List<Action> guideActions = List.of();
  private List<Injection> injections = List.of();

  @Override
  public Action getMethodAction(int index) {
    return methodActions.get(index).get();
  }

  @Override
  public Action getGuideAction(int index) {
    Action action = guideActions.get(index);
    if (action == null) {
      throw UnexpectedViolationException.withMessage("Guide action os not defined");
    }
    return action;
  }

  @Override
  public void setMethodActions(Action... actions) {
    if (actions == null) {
      methodActions = List.of();
      return;
    }
    methodActions = Arrays.stream(actions)
        .map(Actions::resettableGetter)
        .toList();
  }

  @Override
  public void setGuideActions(Action... actions) {
    if (actions == null) {
      guideActions = List.of();
      return;
    }
    guideActions = Arrays.stream(actions).toList();
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
}
