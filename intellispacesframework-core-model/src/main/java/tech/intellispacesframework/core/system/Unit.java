package tech.intellispacesframework.core.system;

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

  List<UnitProjectionProvider> projectionProviders();

  Optional<Method> startupMethod();

  Optional<Method> shutdownMethod();
}
