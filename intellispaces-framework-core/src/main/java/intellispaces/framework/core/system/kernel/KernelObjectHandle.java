package intellispaces.framework.core.system.kernel;

import intellispaces.common.action.Action;

/**
 * Internal representation of the object handle.
 */
public interface KernelObjectHandle {

  int numberTransitions();

  Action getTransitionAction(int index);

  Action getGuideAction(int index);

  void setTransitionActions(Action... actions);

  void setGuideActions(Action... actions);
}
