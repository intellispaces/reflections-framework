package tech.intellispaces.core.system;

import java.util.Collection;
import java.util.List;

/**
 * Projection register.
 */
public interface ProjectionRegistry {

  void load();

  <T> T getProjection(String name, Class<T> targetClass);

  <T> List<T> getProjections(Class<T> targetClass);

  Collection<ModuleProjection> allProjections();
}
