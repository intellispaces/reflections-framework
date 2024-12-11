package tech.intellispaces.jaquarius.engine.impl;

import tech.intellispaces.action.Action;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleMethodDescriptor;
import tech.intellispaces.jaquarius.system.Injection;

import java.util.List;

public class ObjectHandleTypeDescriptor implements tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleTypeDescriptor {
  private final Class<?> objctHandleClass;
  private final Class<?> objctHandleWrapperClass;
  private final List<ObjectHandleMethodDescriptor> methods;
  private final Action[] methodActions;
  private final Action[] guideActions;
  private final Injection[] injections;

  public ObjectHandleTypeDescriptor(
      Class<?> objctHandleClass,
      Class<?> objctHandleWrapperClass,
      List<ObjectHandleMethodDescriptor> methods,
      Action[] methodActions,
      Action[] guideActions,
      Injection[] injections
  ) {
    this.objctHandleClass = objctHandleClass;
    this.objctHandleWrapperClass = objctHandleWrapperClass;
    this.methods = methods;
    this.methodActions = methodActions;
    this.guideActions = guideActions;
    this.injections = injections;
  }

  @Override
  public Class<?> objctHandleClass() {
    return objctHandleClass;
  }

  @Override
  public Class<?> objctHandleWrapperClass() {
    return objctHandleWrapperClass;
  }

  @Override
  public List<ObjectHandleMethodDescriptor> methods() {
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
