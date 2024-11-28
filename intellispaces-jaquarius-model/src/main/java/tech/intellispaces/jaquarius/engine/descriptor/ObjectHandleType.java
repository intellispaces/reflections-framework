package tech.intellispaces.jaquarius.engine.descriptor;

import java.util.List;

/**
 * Object handle type descriptor.
 */
public interface ObjectHandleType {

  Class<?> objctHandleClass();

  List<ObjectHandleMethod> methods();
}
