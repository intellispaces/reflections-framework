package tech.intellispaces.reflections.framework.reflection;

/**
 * This interface is a marker of reflection bound to existing Java object.
 */
public interface NativePoint extends BoundPoint {

  /**
   * Returns bound Java object.
   */
  Object boundObject();
}
