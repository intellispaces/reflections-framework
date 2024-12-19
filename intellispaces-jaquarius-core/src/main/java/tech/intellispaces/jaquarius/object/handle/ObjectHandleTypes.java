package tech.intellispaces.jaquarius.object.handle;

import tech.intellispaces.general.entity.Enumeration;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleType;

public enum ObjectHandleTypes implements ObjectHandleType, Enumeration<ObjectHandleType> {

  /**
   * The movable object handle.
   */
  Movable,

  /**
   * The unmovable object handle.
   */
  Unmovable,

  /**
   * The general object handle.
   */
  General;

  public static ObjectHandleTypes from(ObjectHandleType value) {
    return VALUES[value.ordinal()];
  }

  private static final ObjectHandleTypes[] VALUES = values();
}
