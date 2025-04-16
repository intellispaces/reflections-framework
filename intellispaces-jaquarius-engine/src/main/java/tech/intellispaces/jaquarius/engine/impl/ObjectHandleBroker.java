package tech.intellispaces.jaquarius.engine.impl;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.jaquarius.engine.description.ObjectHandleTypeDescription;
import tech.intellispaces.jaquarius.object.reference.ObjectHandle;
import tech.intellispaces.jaquarius.system.Injection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectHandleBroker implements tech.intellispaces.jaquarius.engine.ObjectHandleBroker {
  private final ObjectHandleTypeDescription type;
  private final Action[] methodActions;
  private final Action[] guideActions;
  private final Injection[] injections;
  private final Map<Class<?>, Object> projections = new HashMap<>();
  private ObjectHandle<?> overlyingHandle;

  public ObjectHandleBroker(
      ObjectHandleTypeDescription type,
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
  public ObjectHandleTypeDescription type() {
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
  public List<? extends ObjectHandle<?>> underlyingHandles() {
    throw NotImplementedExceptions.withCode("KDpzfXvh");
  }

  @Override
  public ObjectHandle<?> overlyingHandle() {
    return overlyingHandle;
  }

  @Override
  public void setOverlyingHandle(ObjectHandle<?> overlyingHandle) {
    this.overlyingHandle = overlyingHandle;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <D, H> H mapTo(Class<D> targetDomain) {
    return (H) projections.get(targetDomain);
  }
}
