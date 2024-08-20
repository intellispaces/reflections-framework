package tech.intellispaces.core.aop;

import tech.intellispaces.actions.Action;
import tech.intellispaces.actions.wrapper.Wrapper;

public interface Advice extends Wrapper {

  Action joinAction();
}
