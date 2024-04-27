package tech.intellispacesframework.core.system;

import java.util.Collection;
import java.util.Optional;

/**
 * Module projections register.
 */
public interface ProjectionRegistry {

  <T> Optional<T> getProjection(String name, Class<T> targetClass);

  void addProjection(SystemProjection projection);

  Collection<SystemProjection> allProjections();
}
