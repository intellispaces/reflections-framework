package intellispaces.core.system.kernel;

import intellispaces.core.system.ModuleProjection;

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

  <T> void addContextProjection(String name, Class<T> type, T target);

  void removeContextProjection(String name);
}
