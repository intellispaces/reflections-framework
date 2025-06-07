package tech.intellispaces.reflections.framework.reflection;

/**
 * This interface is a marker of reflection bound to existing object.
 */
public interface BoundReflection extends SystemReflection {

  default boolean isBound() {
    return true;
  }
}
