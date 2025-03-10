package tech.intellispaces.jaquarius.traverse;

import tech.intellispaces.commons.entity.Enumeration;

/**
 * The traverse types.
 */
public enum TraverseTypes implements TraverseType, Enumeration<TraverseType> {

  /**
   * The mapping traverse.
   */
  Mapping,

  /**
   * The moving traverse.
   */
  Moving,

  /**
   * The mapping related to a specific movement.
   */
  MappingOfMoving;


  @Override
  public boolean isMapping() {
    return (this == TraverseTypes.Mapping);
  }

  @Override
  public boolean isMoving() {
    return (this == TraverseTypes.Moving);
  }

  @Override
  public boolean isMappingOfMoving() {
    return (this == TraverseTypes.MappingOfMoving);
  }

  @Override
  public boolean isMovingBased() {
    return (this == TraverseTypes.Moving || this == TraverseTypes.MappingOfMoving);
  }
}
