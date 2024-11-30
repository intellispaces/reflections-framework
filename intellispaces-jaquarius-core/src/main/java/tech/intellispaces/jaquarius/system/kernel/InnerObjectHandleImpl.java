package tech.intellispaces.jaquarius.system.kernel;

import tech.intellispaces.action.Action;
import tech.intellispaces.action.Actions;
import tech.intellispaces.action.supplier.ResettableSupplierAction;
import tech.intellispaces.entity.exception.UnexpectedExceptions;
import tech.intellispaces.jaquarius.system.Injection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InnerObjectHandleImpl implements InnerObjectHandle {
  private List<ResettableSupplierAction<Action>> methodActions = List.of();
  private List<Action> guideActions = List.of();
  private List<Injection> injections = List.of();
  private final Map<Class<?>, Object> projections = new HashMap<>();

  @Override
  public Action getMethodAction(int index) {
    return methodActions.get(index).get();
  }

  @Override
  public Action getGuideAction(int index) {
    Action action = guideActions.get(index);
    if (action == null) {
      throw UnexpectedExceptions.withMessage("Guide action is not defined");
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
        .map(Actions::resettableSupplierAction)
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

  @Override
  public <TD, TH> void addProjection(Class<TD> targetDomain, TH target) {
    projections.put(targetDomain, target);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <TD, TH> TH mapTo(Class<TD> targetDomain) {
    return (TH) projections.get(targetDomain);
  }
}
