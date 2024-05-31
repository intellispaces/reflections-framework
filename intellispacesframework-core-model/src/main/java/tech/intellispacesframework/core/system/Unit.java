package tech.intellispacesframework.core.system;

import tech.intellispacesframework.core.guide.Guide;

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
}
