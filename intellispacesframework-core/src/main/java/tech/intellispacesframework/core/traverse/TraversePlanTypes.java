package tech.intellispacesframework.core.traverse;

public enum TraversePlanTypes implements TraversePlanType {

  /**
   * Actual traverse plan to call not-parametrized guide.
   */
  CallGuide0,

  /**
   * Actual traverse plan to call one-parametrized guide.
   */
  CallGuide1,

  /**
   * Declarative traverse plan to map object handle through not-parametrized transition.
   */
  MapObjectHandleThruTransition0,

  /**
   * Declarative traverse plan to map object handle through one-parametrized transition.
   */
  MapObjectHandleThruTransition1,

  /**
   * Declarative traverse plan to move object handle through not-parametrized transition.
   */
  MoveObjectHandleThruTransition0,

  /**
   * Declarative traverse plan to move object handle through one-parametrized transition.
   */
  MoveObjectHandleThruTransition1
}
