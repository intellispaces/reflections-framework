package intellispaces.framework.core.traverse;

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
   * Actual traverse plan to call two times parametrized guide.
   */
  CallGuide2,

  /**
   * Actual traverse plan to call three times parametrized guide.
   */
  CallGuide3,

  /**
   * Declarative traverse plan to map object handle through not-parametrized transition.
   */
  MapObjectHandleThruTransition0,

  /**
   * Declarative traverse plan to map object handle through one-parametrized transition.
   */
  MapObjectHandleThruTransition1,

  /**
   * Declarative traverse plan to map object handle through two times parametrized transition.
   */
  MapObjectHandleThruTransition2,

  /**
   * Declarative traverse plan to map object handle through three times parametrized transition.
   */
  MapObjectHandleThruTransition3,

  /**
   * Declarative traverse plan to move object handle through not-parametrized transition.
   */
  MoveObjectHandleThruTransition0,

  /**
   * Declarative traverse plan to move object handle through one-parametrized transition.
   */
  MoveObjectHandleThruTransition1,

  /**
   * Declarative traverse plan to move object handle through two times parametrized transition.
   */
  MoveObjectHandleThruTransition2,

  /**
   * Declarative traverse plan to move object handle through three times parametrized transition.
   */
  MoveObjectHandleThruTransition3
}
