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
   * Mapping related to a specific movement.<p/>
   *
   * Combined channel that sequentially combines movement and then directly related mapping traverses.
   */
  MappingOfMoving(true);

  private final boolean movingBased;

  TraverseTypes(boolean movingBased) {
    this.movingBased = movingBased;
  }

  @Override
  public boolean isMovingBased() {
    return movingBased;
  }
}
