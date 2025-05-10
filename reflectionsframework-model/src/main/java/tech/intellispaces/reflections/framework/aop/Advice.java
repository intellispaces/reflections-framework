package tech.intellispaces.reflections.framework.aop;

import tech.intellispaces.actions.Action;
import tech.intellispaces.actions.wrapper.WrapperAction;

public interface Advice extends WrapperAction {

  Action joinAction();
}
