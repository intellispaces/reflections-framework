package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.Action;

public class ObjectHandleInstanceImpl implements ObjectHandleInstance {
  private final ObjectHandleType type;
  private final Action[] methodActions;
  private final Action[] guideActions;

  public ObjectHandleInstanceImpl(ObjectHandleType type, Action[] methodActions, Action[] guideActions) {
    this.type = type;
    this.methodActions = methodActions;
    this.guideActions = guideActions;
  }

  @Override
  public ObjectHandleType type() {
    return type;
  }

  @Override
  public Action getMethodAction(int ordinal) {
    return methodActions[ordinal];
  }

  @Override
  public Action getGuideAction(int ordinal) {
    return guideActions[ordinal];
  }
}
