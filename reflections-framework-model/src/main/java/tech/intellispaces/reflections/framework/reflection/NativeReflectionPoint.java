package tech.intellispaces.reflections.framework.reflection;

/**
 * The interface-marker of reflection point bound to real Java object.
 */
public interface NativeReflectionPoint extends BoundReflectionPoint {

  /**
   * Returns bound Java object.
   */
  Object boundObject();
}
