package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.Action;
import tech.intellispaces.entity.exception.UnexpectedExceptions;
import tech.intellispaces.jaquarius.system.Injection;

import java.util.HashMap;
import java.util.Map;

public class ObjectHandleInstanceImpl implements ObjectHandleInstance {
  private final ObjectHandleType type;
  private final Action[] methodActions;
  private final Action[] guideActions;
  private final Injection[] injections;
  private final Map<Class<?>, Object> projections = new HashMap<>();

  public ObjectHandleInstanceImpl(
      ObjectHandleType type,
      Action[] methodActions,
      Action[] guideActions,
      Injection[] injections
  ) {
    this.type = type;
    this.methodActions = methodActions;
    this.guideActions = guideActions;
    this.injections = injections;
  }

  @Override
  public ObjectHandleType type() {
    return type;
  }

  @Override
  public Action methodAction(int ordinal) {
    return methodActions[ordinal];
  }

  @Override
  public Action guideAction(int ordinal) {
    Action action = guideActions[ordinal];
    if (action == null) {
      throw UnexpectedExceptions.withMessage("The guide with index {0} was not found", ordinal);
    }
    return action;
  }

  @Override
  public Injection injection(int ordinal) {
    return injections[ordinal];
  }

  @Override
  public <D, H> void addProjection(Class<D> targetDomain, H target) {
    projections.put(targetDomain, target);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <D, H> H mapTo(Class<D> targetDomain) {
    return (H) projections.get(targetDomain);
  }
}
