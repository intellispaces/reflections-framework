package tech.intellispaces.jaquarius.object.reference;

import tech.intellispaces.commons.entity.Enumeration;

public enum ObjectHandleTypes implements ObjectHandleType, Enumeration<ObjectHandleType> {

  /**
   * The unmovable clear object.
   */
  UnmovableClearObject,

  /**
   * The movable clear object.
   */
  MovableClearObject,

  /**
   * The undefined clear object.
   */
  UndefinedClearObject,

  /**
   * The unmovable object handle.
   */
  UnmovableHandle,

  /**
   * The movable object handle.
   */
  MovableHandle,

  /**
   * The undefined object handle.
   */
  UndefinedHandle;

  public static ObjectHandleTypes from(ObjectHandleType value) {
    return VALUES[value.ordinal()];
  }

  private static final ObjectHandleTypes[] VALUES = values();
}
