package tech.intellispaces.reflections.engine.description;

import java.util.List;

/**
 * The object handle type description.
 */
public interface ObjectHandleTypeDescription {

  /**
   * The object handle class.
   */
  Class<?> objctHandleClass();

  /**
   * The object handle wrapper class.
   */
  Class<?> objctHandleWrapperClass();

  /**
   * Object handle wrapper class methods.
   */
  List<ObjectHandleMethodDescription> methods();
}
