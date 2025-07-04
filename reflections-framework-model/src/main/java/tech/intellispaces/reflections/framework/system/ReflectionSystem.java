package tech.intellispaces.reflections.framework.system;

import java.util.List;

import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.ReflectionPoint;
import tech.intellispaces.core.System;

public interface ReflectionSystem extends System {

  /**
   * Cast reflection.
   *
   * @param reflection the origin reflection.
   * @param reflectionClass the reflection class.
   * @return the reflection in the context of the system.
   * @param <T> the reflection type.
   */
  <T> T castReflection(ReflectionPoint reflection, Class<T> reflectionClass);

  /**
   * Searches for factories that create reflections of a given domain.
   *
   * @param domain the reflection domain.
   * @return the list of factories.
   */
  List<ReflectionFactory> findFactories(ReflectionDomain domain);
}
