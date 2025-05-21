package tech.intellispaces.reflections.framework.reflection;

import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.reflections.framework.system.Injection;

public class ReflectionRealizationTypeImpl implements ReflectionRealizationType {
  private final Class<?> realizationClass;
  private final Class<?> wrapperClass;
  private final List<ReflectionRealizationMethod> methods;
  private final Action[] methodActions;
  private final Action[] guideActions;
  private final Injection[] injections;

  ReflectionRealizationTypeImpl(
      Class<?> realizationClass,
      Class<?> wrapperClass,
      List<ReflectionRealizationMethod> methods,
      Action[] methodActions,
      Action[] guideActions,
      Injection[] injections
  ) {
    this.realizationClass = realizationClass;
    this.wrapperClass = wrapperClass;
    this.methods = methods;
    this.methodActions = methodActions;
    this.guideActions = guideActions;
    this.injections = injections;
  }

  @Override
  public Class<?> realizationClass() {
    return realizationClass;
  }

  @Override
  public Class<?> wrapperClass() {
    return wrapperClass;
  }

  @Override
  public List<ReflectionRealizationMethod> methods() {
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
