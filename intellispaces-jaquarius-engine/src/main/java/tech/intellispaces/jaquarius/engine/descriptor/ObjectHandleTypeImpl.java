package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.Action;

import java.util.List;

public class ObjectHandleTypeImpl implements ObjectHandleType {
  private final Class<?> objctHandleClass;
  private final Class<?> objctHandleWrapperClass;
  private final List<ObjectHandleMethod> methods;
  private final Action[] methodActions;
  private final Action[] guideActions;

  public ObjectHandleTypeImpl(
      Class<?> objctHandleClass,
      Class<?> objctHandleWrapperClass,
      List<ObjectHandleMethod> methods,
      Action[] methodActions,
      Action[] guideActions
  ) {
    this.objctHandleClass = objctHandleClass;
    this.objctHandleWrapperClass = objctHandleWrapperClass;
    this.methods = methods;
    this.methodActions = methodActions;
    this.guideActions = guideActions;
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
  public List<ObjectHandleMethod> methods() {
    return methods;
  }

  public Action[] getMethodActions() {
    return methodActions;
  }

  public Action[] getGuideActions() {
    return guideActions;
  }
}
