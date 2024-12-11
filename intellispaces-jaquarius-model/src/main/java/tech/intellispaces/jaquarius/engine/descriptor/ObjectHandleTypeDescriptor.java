package tech.intellispaces.jaquarius.engine.descriptor;

import java.util.List;

/**
 * The object handle type descriptor.
 */
public interface ObjectHandleTypeDescriptor {

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
  List<ObjectHandleMethodDescriptor> methods();
}
