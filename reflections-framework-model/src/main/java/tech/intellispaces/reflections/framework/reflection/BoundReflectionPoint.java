package tech.intellispaces.reflections.framework.reflection;

import tech.intellispaces.core.ReflectionPoint;

/**
 * The marker-interface of reflection point bound to real object.
 */
public interface BoundReflectionPoint extends SystemReflection, ReflectionPoint {

  default boolean isBound() {
    return true;
  }
}
