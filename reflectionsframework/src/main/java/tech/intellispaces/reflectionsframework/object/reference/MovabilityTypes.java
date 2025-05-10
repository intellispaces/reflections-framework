package tech.intellispaces.reflectionsframework.object.reference;

import tech.intellispaces.commons.abstraction.Enumeration;

/**
 * The object movability type.
 */
public enum MovabilityTypes implements MovabilityType, Enumeration<MovabilityType> {

  /**
   * The general object type (movable or unmovable).
   */
  General,

  /**
   * The unmovable object type.
   */
  Unmovable,

  /**
   * The movable object type.
   */
  Movable;

  public static MovabilityTypes of(MovabilityType value) {
    return VALUES[value.ordinal()];
  }

  private static final MovabilityTypes[] VALUES = values();
}
