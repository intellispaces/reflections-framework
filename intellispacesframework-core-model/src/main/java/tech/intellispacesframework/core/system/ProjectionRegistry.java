package tech.intellispacesframework.core.system;

import java.util.Collection;
import java.util.Optional;

/**
 * Projections register.
 */
public interface ProjectionRegistry {

  <T> Optional<T> getProjection(String name, Class<T> targetClass);

  void addProjection(SystemProjection projection);

  Collection<SystemProjection> allProjections();
}
