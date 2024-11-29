package tech.intellispaces.jaquarius.engine.descriptor;

import java.util.List;

/**
 * Object handle type descriptor.
 */
public interface ObjectHandleType {

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
  List<ObjectHandleMethod> methods();
}
