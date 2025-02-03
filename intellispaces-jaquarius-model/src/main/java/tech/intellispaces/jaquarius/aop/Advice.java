package tech.intellispaces.jaquarius.aop;

import tech.intellispaces.commons.action.Action;
import tech.intellispaces.commons.action.wrapper.WrapperAction;

public interface Advice extends WrapperAction {

  Action joinAction();
}
