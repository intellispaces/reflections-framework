package tech.intellispaces.core.system.shadow;

import tech.intellispaces.actions.Action;
import tech.intellispaces.actions.Actions;
import tech.intellispaces.actions.getter.ResettableGetter;
import tech.intellispaces.commons.exception.UnexpectedViolationException;

import java.util.Arrays;
import java.util.List;

class ObjectHandleImpl implements ShadowObjectHandle {
  private List<ResettableGetter<Action>> transitionActions = List.of();
  private List<Action> guideActions = List.of();

  @Override
  public int numberTransitions() {
    return transitionActions.size();
  }

  @Override
  public Action getTransitionAction(int index) {
    return transitionActions.get(index).get();
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
  public void setTransitionActions(Action... actions) {
    if (actions == null) {
      transitionActions = List.of();
      return;
    }
    transitionActions = Arrays.stream(actions)
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
}
