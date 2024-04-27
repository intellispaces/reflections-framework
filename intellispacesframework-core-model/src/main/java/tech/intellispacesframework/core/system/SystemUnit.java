package tech.intellispacesframework.core.system;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public interface SystemUnit {

  boolean isMain();

  Class<?> unitClass();

  Object instance();

  Optional<Method> startupMethod();

  Optional<Method> shutdownMethod();

  List<UnitProjectionProvider> projectionProviders();
}
