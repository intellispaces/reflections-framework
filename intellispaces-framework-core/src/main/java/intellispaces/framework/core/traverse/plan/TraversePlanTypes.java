package intellispaces.framework.core.traverse.plan;

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
   * Declarative traverse plan to map object handle through not-parametrized channel.
   */
  MapObjectHandleThruChannel0,

  /**
   * Declarative traverse plan to map object handle through one-parametrized channel.
   */
  MapObjectHandleThruChannel1,

  /**
   * Declarative traverse plan to map object handle through two times parametrized channel.
   */
  MapObjectHandleThruChannel2,

  /**
   * Declarative traverse plan to map object handle through three times parametrized channel.
   */
  MapObjectHandleThruChannel3,

  /**
   * Declarative traverse plan to move object handle through not-parametrized channel.
   */
  MoveObjectHandleThruChannel0,

  /**
   * Declarative traverse plan to move object handle through one-parametrized channel.
   */
  MoveObjectHandleThruChannel1,

  /**
   * Declarative traverse plan to move object handle through two times parametrized channel.
   */
  MoveObjectHandleThruChannel2,

  /**
   * Declarative traverse plan to move object handle through three times parametrized channel.
   */
  MoveObjectHandleThruChannel3,

  /**
   * Declarative traverse plan to map of moving object handle through not-parametrized channel.
   */
  MapOfMovingObjectHandleThruChannel0,

  /**
   * Declarative traverse plan to map of moving object handle through one-parametrized channel.
   */
  MapOfMovingObjectHandleThruChannel1,

  /**
   * Declarative traverse plan to map of moving object handle through two times parametrized channel.
   */
  MapOfMovingObjectHandleThruChannel2,

  /**
   * Declarative traverse plan to map of moving object handle through three times parametrized channel.
   */
  MapOfMovingObjectHandleThruChannel3,

  /**
   * Declarative traverse plan to map of moving object handle through four times parametrized channel.
   */
  MapOfMovingObjectHandleThruChannel4
}
