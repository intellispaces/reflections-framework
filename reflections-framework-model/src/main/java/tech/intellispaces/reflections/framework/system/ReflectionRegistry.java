package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.core.Reflection;

/**
 * The reflection registry.
 */
public interface ReflectionRegistry {

  Reflection register(Reflection reflection);
}
