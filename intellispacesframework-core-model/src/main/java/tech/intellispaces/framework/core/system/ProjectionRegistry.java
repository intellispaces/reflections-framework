package tech.intellispaces.framework.core.system;

import java.util.Collection;

/**
 * Projection register.
 */
public interface ProjectionRegistry {

  void load();

  <T> T getProjectionTarget(String name, Class<T> targetClass);

  Collection<SystemProjection> projections();
}
