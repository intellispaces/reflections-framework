package tech.intellispaces.reflections.framework.reflection;

import java.util.List;

/**
 * The reflection realization type description.
 */
public interface ReflectionRealizationType {

  /**
   * The realization class.
   */
  Class<?> realizationClass();

  /**
   * The wrapper class.
   */
  Class<?> wrapperClass();

  /**
   * Reflection realization methods.
   */
  List<ReflectionRealizationMethod> methods();
}
