package tech.intellispacesframework.core.traverseplan;

public interface MoveObjectHandleThruTransition1TraversePlan extends TraversePlan {

  /**
   * Source object handle class.
   */
  Class<?> objectHandleClass();

  /**
   * Transition ID.
   */
  String tid();

  TraversePlan delegate();
}
