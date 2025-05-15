package tech.intellispaces.reflections.framework.reflection;

/**
 * This interface is a marker of reflection bound to existing Java object.
 */
public interface NativeReflection<D> extends BoundReflection<D> {

  /**
   * Returns bound Java object.
   */
  Object boundObject();
}
