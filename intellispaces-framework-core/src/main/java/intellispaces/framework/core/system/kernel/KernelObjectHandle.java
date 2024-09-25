package intellispaces.framework.core.system.kernel;

import intellispaces.common.action.Action;
import intellispaces.framework.core.system.Injection;

import java.util.List;

/**
 * Internal kernel representation of the object handle.
 */
public interface KernelObjectHandle {

  int numberTransitions();

  Action getTransitionAction(int index);

  Action getGuideAction(int index);

  void setTransitionActions(Action... actions);

  void setGuideActions(Action... actions);

  Injection injection(int ordinal);

  List<Injection> injections();

  void setInjections(Injection... injections);
}
