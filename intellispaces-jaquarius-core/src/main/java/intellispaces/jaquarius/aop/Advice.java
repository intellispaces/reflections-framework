package intellispaces.jaquarius.aop;

import intellispaces.common.action.Action;
import intellispaces.common.action.wrapper.WrapperAction;

public interface Advice extends WrapperAction {

  Action joinAction();
}
