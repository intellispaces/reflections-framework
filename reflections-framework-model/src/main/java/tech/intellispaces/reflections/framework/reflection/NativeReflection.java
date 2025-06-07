package tech.intellispaces.reflections.framework.reflection;

/**
 * This interface is a marker of reflection bound to existing Java object.
 */
public interface NativeReflection extends BoundReflection {

  /**
   * Returns bound Java object.
   */
  Object boundObject();
}
