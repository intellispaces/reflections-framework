package tech.intellispaces.core.object;

import tech.intellispaces.actions.Action;

public interface ObjectHandleWrapper<D> extends ObjectHandle<D> {

  int numberGuides();

  Action getGuideAction(int index);
}
