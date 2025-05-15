package tech.intellispaces.reflections.framework.reflection;

/**
 * This interface is a marker of reflection bound to existing object.
 */
public interface BoundReflection<D> extends Reflection<D> {

  default boolean isBound() {
    return true;
  }
}
