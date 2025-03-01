package tech.intellispaces.jaquarius.object.reference;

import tech.intellispaces.commons.base.entity.Enumeration;

public enum ObjectHandleTypes implements ObjectHandleType, Enumeration<ObjectHandleType> {

  /**
   * The unmovable pure object.
   */
  UnmovablePureObject,

  /**
   * The movable pure object.
   */
  MovablePureObject,

  /**
   * The undefined pure object.
   */
  UndefinedPureObject,

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
