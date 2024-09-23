package intellispaces.framework.core.traverse;

public enum TraverseTypes implements TraverseType {

  /**
   * Mapping traverse.
   */
  Mapping(false),

  /**
   * Moving traverse.
   */
  Moving(true),

  /**
   * Mapping traverse related to a specific movement.<p/>
   *
   * Combined transition that sequentially combines directly related movement and then mapping traverses.
   */
  MappingRelatedToMoving(true);

  private final boolean movingBased;

  TraverseTypes(boolean movingBased) {
    this.movingBased = movingBased;
  }

  @Override
  public boolean isMovingBased() {
    return movingBased;
  }
}
