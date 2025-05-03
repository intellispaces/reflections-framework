package tech.intellispaces.reflections.traverse;

import tech.intellispaces.commons.abstraction.Enumerable;

/**
 * The traverse type.
 */
public interface TraverseType extends Enumerable<TraverseType> {

  /**
   * The mapping traverse.
   */
  boolean isMapping();

  /**
   * The moving traverse.
   */
  boolean isMoving();

  /**
   * The mapping related to a specific movement.
   */
  boolean isMappingOfMoving();

  boolean isMovingBased();
}
