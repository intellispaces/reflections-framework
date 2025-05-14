package tech.intellispaces.reflections.framework.engine.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.reflections.framework.engine.description.ReflectionImplementationDescription;
import tech.intellispaces.reflections.framework.reflection.Reflection;
import tech.intellispaces.reflections.framework.system.Injection;

public class ReflectionBroker implements tech.intellispaces.reflections.framework.engine.ReflectionBroker {
  private final ReflectionImplementationDescription type;
  private final Action[] methodActions;
  private final Action[] guideActions;
  private final Injection[] injections;
  private final Map<Class<?>, Object> projections = new HashMap<>();
  private Reflection<?> overlyingReflection;

  public ReflectionBroker(
      ReflectionImplementationDescription type,
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
  public ReflectionImplementationDescription type() {
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
  public <D, R> void addProjection(Class<D> targetDomain, R target) {
    projections.put(targetDomain, target);
  }

  @Override
  public List<? extends Reflection<?>> underlyingReflections() {
    throw NotImplementedExceptions.withCode("KDpzfXvh");
  }

  @Override
  public Reflection<?> overlyingReflection() {
    return overlyingReflection;
  }

  @Override
  public void setOverlyingReflection(Reflection<?> overlyingReflection) {
    this.overlyingReflection = overlyingReflection;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <D, R> R mapTo(Class<D> targetDomain) {
    return (R) projections.get(targetDomain);
  }
}
