package tech.intellispaces.core.system.shadow;

import tech.intellispaces.actions.Action;
import tech.intellispaces.core.system.Unit;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Internal representation of the module unit.
 */
public interface ShadowUnit extends Unit {

  Optional<Method> startupMethod();

  Optional<Method> shutdownMethod();

  Optional<Action> startupAction();

  Optional<Action> shutdownAction();

  void setStartupAction(Action action);

  void setShutdownAction(Action action);

  int numberGuides();

  Action getGuideAction(int index);

  void setGuideActions(Action... actions);
}
