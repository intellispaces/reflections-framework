package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.Action;

import java.util.List;

class ObjectHandleMethodImpl implements ObjectHandleMethod {
  private final String name;
  private final List<Class<?>> paramClasses;
  private final Action guideAction;
  private final List<Class<?>> guideParamClasses;

  ObjectHandleMethodImpl(
      String name, List<Class<?>> paramClasses, Action guideAction, List<Class<?>> guideParamClasses
  ) {
    this.name = name;
    this.paramClasses = paramClasses;
    this.guideAction = guideAction;
    this.guideParamClasses = guideParamClasses;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public List<Class<?>> paramClasses() {
    return paramClasses;
  }

  @Override
  public Action guideAction() {
    return guideAction;
  }

  @Override
  public List<Class<?>> guideParamClasses() {
    return guideParamClasses;
  }
}
