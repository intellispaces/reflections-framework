package tech.intellispaces.reflections.framework.reflection;

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
   * The movable object type.
   */
  Movable;

  public static MovabilityTypes of(MovabilityType value) {
    return VALUES[value.ordinal()];
  }

  private static final MovabilityTypes[] VALUES = values();
}
