package tech.intellispaces.reflections.framework.system;

import java.util.List;

/**
 * The module projection registry.
 */
public interface ProjectionRegistry {

  void onStartup();

  void onShutdown();

  <T> T getProjection(String name, Class<T> targetReflectionClass);

  <T> List<T> findProjections(Class<T> targetReflectionClass);

  void addProjection(ProjectionDefinition projectionDefinition);

  <T> void addContextProjection(String name, Class<T> targetReflectionClass, T target);

  void removeContextProjection(String name);
}
