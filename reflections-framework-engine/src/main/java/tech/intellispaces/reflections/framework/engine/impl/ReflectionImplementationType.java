package tech.intellispaces.reflections.framework.engine.impl;

import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.reflections.framework.engine.description.ReflectionImplementationMethodDescription;
import tech.intellispaces.reflections.framework.system.Injection;

public class ReflectionImplementationType implements tech.intellispaces.reflections.framework.engine.description.ReflectionImplementationType {
  private final Class<?> reflectionImplementationClass;
  private final Class<?> reflectionWrapperClass;
  private final List<ReflectionImplementationMethodDescription> methods;
  private final Action[] methodActions;
  private final Action[] guideActions;
  private final Injection[] injections;

  public ReflectionImplementationType(
      Class<?> reflectionImplementationClass,
      Class<?> reflectionWrapperClass,
      List<ReflectionImplementationMethodDescription> methods,
      Action[] methodActions,
      Action[] guideActions,
      Injection[] injections
  ) {
    this.reflectionImplementationClass = reflectionImplementationClass;
    this.reflectionWrapperClass = reflectionWrapperClass;
    this.methods = methods;
    this.methodActions = methodActions;
    this.guideActions = guideActions;
    this.injections = injections;
  }

  @Override
  public Class<?> reflectionImplementationClass() {
    return reflectionImplementationClass;
  }

  @Override
  public Class<?> reflectionWrapperClass() {
    return reflectionWrapperClass;
  }

  @Override
  public List<ReflectionImplementationMethodDescription> methods() {
    return methods;
  }

  public Action[] methodActions() {
    return methodActions;
  }

  public Action[] guideActions() {
    return guideActions;
  }

  public Injection[] injections() {
    return injections;
  }
}
