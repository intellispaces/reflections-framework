package tech.intellispaces.reflections.framework.reflection;

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
  List<ReflectionImplementationMethod> methods();
}
