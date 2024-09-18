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
   * Combined transition that sequentially combines directly related movement and then mapping traverses.
   */
  MovingThenMapping(true);

  private final boolean moving;

  TraverseTypes(boolean moving) {
    this.moving = moving;
  }

  @Override
  public boolean isMovingRelated() {
    return moving;
  }
}
