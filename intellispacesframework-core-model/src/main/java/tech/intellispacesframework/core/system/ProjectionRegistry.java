package tech.intellispacesframework.core.system;

import java.util.Collection;

/**
 * Projections register.
 */
public interface ProjectionRegistry {

  <T> T projection(String name, Class<T> targetClass);

  Collection<SystemProjection> loadedProjections();
}
