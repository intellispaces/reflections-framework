package intellispaces.core.system.shadow;

import intellispaces.actions.Action;
import intellispaces.core.system.Unit;
import intellispaces.core.system.UnitProjectionDefinition;
import intellispaces.core.system.UnitProjectionInjection;

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
