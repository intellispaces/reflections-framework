package tech.intellispacesframework.core.traverse;

public enum TraversePlanTypes implements TraversePlanType {

  /**
   * Traverse plan to call not-parametrized guide.
   */
  CallGuide0,

  /**
   * Traverse plan to call one-parametrized guide.
   */
  CallGuide1,

  /**
   * Declarative traverse plan to move object handle defined class through not-parametrized transition.
   */
  MoveObjectHandleThruTransition0,

  /**
   * Declarative traverse plan to move object handle defined class through one-parametrized transition.
   */
  MoveObjectHandleThruTransition1
}
