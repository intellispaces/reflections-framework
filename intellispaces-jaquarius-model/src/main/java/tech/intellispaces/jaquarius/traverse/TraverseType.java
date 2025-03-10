package tech.intellispaces.jaquarius.traverse;

import tech.intellispaces.commons.entity.Enumerable;

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
