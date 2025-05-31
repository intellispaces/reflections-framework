package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.Rid;

/**
 * The reflection registry.
 */
public interface ReflectionRegistry {

  Reflection register(Reflection reflection);

  Reflection get(Rid rid);
}
