package tech.intellispaces.core.system.shadow;

import tech.intellispaces.actions.Action;
import tech.intellispaces.core.system.Unit;
import tech.intellispaces.core.system.UnitProjectionDefinition;
import tech.intellispaces.core.system.UnitProjectionInjection;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * Internal representation of the module unit.
 */
public interface ShadowUnit extends Unit {

  Object instance();

  Optional<Method> startupMethod();

  Optional<Method> shutdownMethod();

  Optional<Action> startupAction();

  Optional<Action> shutdownAction();

  void setStartupAction(Action action);

  void setShutdownAction(Action action);

  void setProjectionDefinitions(UnitProjectionDefinition... projectionDefinitions);

  UnitProjectionInjection projectionInjection(int injectionIndex);

  List<UnitProjectionInjection> projectionInjections();

  void setProjectionInjections(UnitProjectionInjection... injections);

  int numberGuides();

  Action getGuideAction(int index);

  void setGuideActions(Action... actions);
}
