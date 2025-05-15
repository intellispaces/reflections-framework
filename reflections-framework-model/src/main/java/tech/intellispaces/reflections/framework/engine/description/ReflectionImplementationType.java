package tech.intellispaces.reflections.framework.engine.description;

import java.util.List;

/**
 * The reflection implementation description.
 */
public interface ReflectionImplementationType {

  /**
   * The reflection implementation class.
   */
  Class<?> reflectionImplementationClass();

  /**
   * The reflection wrapper class.
   */
  Class<?> reflectionWrapperClass();

  /**
   * Reflection implementation methods.
   */
  List<ReflectionImplementationMethodDescription> methods();
}
