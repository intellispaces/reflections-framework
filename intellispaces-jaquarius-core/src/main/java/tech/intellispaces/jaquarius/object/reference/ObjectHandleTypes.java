package tech.intellispaces.jaquarius.object.reference;

import tech.intellispaces.general.entity.Enumeration;

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
