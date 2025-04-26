package tech.intellispaces.jaquarius.traverse.plan;

import tech.intellispaces.commons.abstraction.Enumeration;

public enum TraversePlanTypes implements TraversePlanType, Enumeration<TraversePlanType> {

  /**
   * The execution plan to call not-parametrized guide.
   */
  CallGuide0,

  /**
   * The execution plan to call one-parametrized guide.
   */
  CallGuide1,

  /**
   * The execution plan to call two times parametrized guide.
   */
  CallGuide2,

  /**
   * The execution plan to call three times parametrized guide.
   */
  CallGuide3,

  /**
   * The execution plan to call four times parametrized guide.
   */
  CallGuide4,

  /**
   * The declarative plan to map object handle through not-parametrized channel.
   */
  MapObjectHandleThruChannel0,

  /**
   * The declarative traverse plan to map object handle through one-parametrized channel.
   */
  MapObjectHandleThruChannel1,

  /**
   * The declarative traverse plan to map object handle through two times parametrized channel.
   */
  MapObjectHandleThruChannel2,

  /**
   * The declarative traverse plan to map object handle through three times parametrized channel.
   */
  MapObjectHandleThruChannel3,

  /**
   * The declarative traverse plan to move object handle through not-parametrized channel.
   */
  MoveObjectHandleThruChannel0,

  /**
   * The declarative traverse plan to move object handle through one-parametrized channel.
   */
  MoveObjectHandleThruChannel1,

  /**
   * The declarative traverse plan to move object handle through two times parametrized channel.
   */
  MoveObjectHandleThruChannel2,

  /**
   * The declarative traverse plan to move object handle through three times parametrized channel.
   */
  MoveObjectHandleThruChannel3,

  /**
   * The declarative traverse plan to map of moving object handle through not-parametrized channel.
   */
  MapOfMovingObjectHandleThruChannel0,

  /**
   * The declarative traverse plan to map of moving object handle through one-parametrized channel.
   */
  MapOfMovingObjectHandleThruChannel1,

  /**
   * The declarative traverse plan to map of moving object handle through two times parametrized channel.
   */
  MapOfMovingObjectHandleThruChannel2,

  /**
   * The declarative traverse plan to map of moving object handle through three times parametrized channel.
   */
  MapOfMovingObjectHandleThruChannel3,

  /**
   * The declarative traverse plan to map of moving object handle through four times parametrized channel.
   */
  MapOfMovingObjectHandleThruChannel4,

  /**
   * The execution plan to ascend to overlying object handle and call guide.
   */
  AscendAndExecute0,

  /**
   * The execution plan to ascend to overlying object handle and call guide.
   */
  AscendAndExecute1;

  public static TraversePlanTypes of(TraversePlanType value) {
    return VALUES[value.ordinal()];
  }

  private static final TraversePlanTypes[] VALUES = values();
}
