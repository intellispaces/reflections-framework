package tech.intellispaces.core.system;

import tech.intellispaces.core.guide.Guide;
import tech.intellispaces.core.system.action.ShutdownAction;
import tech.intellispaces.core.system.action.StartupAction;

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

  Optional<StartupAction> startupAction();

  Optional<ShutdownAction> shutdownAction();
}
