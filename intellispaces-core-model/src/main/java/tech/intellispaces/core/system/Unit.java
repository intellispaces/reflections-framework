package tech.intellispaces.core.system;

import tech.intellispaces.actions.Action;
import tech.intellispaces.core.guide.Guide;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * Module unit.
 */
public interface Unit {

  boolean isMain();

  Class<?> unitClass();

  Object instance();

  List<Injection> injections();

  List<UnitProjectionDefinition> projectionProviders();

  List<Guide<?, ?>> guides();

  Optional<Method> startupMethod();

  Optional<Method> shutdownMethod();

  Optional<Action> startupAction();

  Optional<Action> shutdownAction();

  void setStartupAction(Action action);

  void setShutdownAction(Action action);
}
