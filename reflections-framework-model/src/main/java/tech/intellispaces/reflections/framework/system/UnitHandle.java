package tech.intellispaces.reflections.framework.system;

import java.util.List;
import java.util.Optional;

import tech.intellispaces.actions.Action;

/**
 * The unit handle.
 */
public interface UnitHandle extends Unit {

  UnitWrapper unitInstance();

  Optional<Action> startupAction();

  void setStartupAction(Action action);

  Optional<Action> shutdownAction();

  void setShutdownAction(Action action);

  List<Action> guideActions();

  Action guideAction(int ordinal);

  void setGuideAction(int index, Action action);

  List<Injection> injections();

  Injection injection(int ordinal);
}
