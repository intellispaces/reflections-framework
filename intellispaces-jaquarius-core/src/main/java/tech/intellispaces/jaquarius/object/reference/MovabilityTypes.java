package tech.intellispaces.jaquarius.object.reference;

import tech.intellispaces.commons.entity.Enumeration;

/**
 * The object movability type.
 */
public enum MovabilityTypes implements MovabilityType, Enumeration<MovabilityType> {

  Undefined,

  Movable,

  Unmovable;

  public static MovabilityTypes from(MovabilityType value) {
    return VALUES[value.ordinal()];
  }

  private static final MovabilityTypes[] VALUES = values();
}
