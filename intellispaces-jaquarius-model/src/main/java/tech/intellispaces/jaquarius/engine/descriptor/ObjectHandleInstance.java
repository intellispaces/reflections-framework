package tech.intellispaces.jaquarius.engine.descriptor;

import tech.intellispaces.action.Action;

/**
 * Object handle instance descriptor.
 */
public interface ObjectHandleInstance {

  ObjectHandleType type();

  Action getMethodAction(int ordinal);

  Action getGuideAction(int ordinal);
}
