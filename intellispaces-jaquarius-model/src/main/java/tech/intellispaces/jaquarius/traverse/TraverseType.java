package tech.intellispaces.jaquarius.traverse;

public interface TraverseType {

  /**
   * Mapping traverse.
   */
  boolean isMapping();

  /**
   * Moving traverse.
   */
  boolean isMoving();

  /**
   * Mapping related to a specific movement.<p/>
   *
   * Combined channel that sequentially combines movement and then directly related mapping traverses.
   */
  boolean isMappingOfMoving();

  boolean isMovingBased();
}
