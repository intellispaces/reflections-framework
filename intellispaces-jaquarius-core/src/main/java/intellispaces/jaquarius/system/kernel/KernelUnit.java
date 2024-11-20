package intellispaces.jaquarius.system.kernel;

import intellispaces.jaquarius.system.Injection;
import intellispaces.jaquarius.system.Unit;
import intellispaces.jaquarius.system.UnitProjectionDefinition;
import tech.intellispaces.action.Action;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * Internal kernel representation of the module unit.
 */
public interface KernelUnit extends Unit {

  Object instance();

  Optional<Method> startupMethod();

  Optional<Method> shutdownMethod();

  Optional<Action> startupAction();

  Optional<Action> shutdownAction();

  void setStartupAction(Action action);

  void setShutdownAction(Action action);

  void setProjectionDefinitions(UnitProjectionDefinition... projectionDefinitions);

  Injection injection(int ordinal);

  List<Injection> injections();

  void setInjections(Injection... injections);

  int numberGuides();

  Action getGuideAction(int index);

  void setGuideActions(Action... actions);

  void setGuideAction(int index, Action action);
}
