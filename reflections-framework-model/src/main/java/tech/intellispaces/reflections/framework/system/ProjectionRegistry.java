package tech.intellispaces.reflections.framework.system;

import java.util.Collection;
import java.util.List;

/**
 * The module projection registry.
 */
public interface ProjectionRegistry {

  void onStartup();

  void onShutdown();

  <T> T getProjection(String name, Class<T> type);

  <T> List<T> findProjections(Class<T> targetClass);

  void addProjection(ProjectionDefinition projectionDefinition);

  <T> void addContextProjection(String name, Class<T> targetClass, T target);

  void removeContextProjection(String name);

  Collection<ModuleProjection> moduleProjections();
}
