package tech.intellispaces.core.system;

import java.util.Collection;
import java.util.List;

/**
 * Projection register.
 */
public interface ProjectionRegistry {

  void load();

  <T> T getProjectionTarget(String name, Class<T> targetClass);

  <T> List<T> getProjectionTargets(Class<T> targetClass);

  Collection<SystemProjection> projections();
}
