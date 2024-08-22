package intellispaces.core.aop;

import intellispaces.actions.Action;
import intellispaces.actions.wrapper.Wrapper;

public interface Advice extends Wrapper {

  Action joinAction();
}
