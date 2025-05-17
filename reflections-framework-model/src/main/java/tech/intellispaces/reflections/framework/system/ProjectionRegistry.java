package tech.intellispaces.reflections.framework.system;

import java.util.List;

public interface ProjectionRegistry {

  <T> T getProjection(String name, Class<T> targetReflectionClass);

  <T> List<T> findProjections(Class<T> targetReflectionClass);

  <T> void addContextProjection(String name, Class<T> targetReflectionClass, T target);

  void removeContextProjection(String name);
}
