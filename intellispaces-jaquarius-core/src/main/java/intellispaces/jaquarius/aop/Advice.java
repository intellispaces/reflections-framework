package intellispaces.jaquarius.aop;

import tech.intellispaces.action.Action;
import tech.intellispaces.action.wrapper.WrapperAction;

public interface Advice extends WrapperAction {

  Action joinAction();
}
