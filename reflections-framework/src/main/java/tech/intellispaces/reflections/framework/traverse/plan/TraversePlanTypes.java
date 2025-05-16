package tech.intellispaces.reflections.framework.traverse.plan;

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
   * The declarative plan to map reflection through not-parametrized channel.
   */
  MapThruChannel0,

  /**
   * The declarative traverse plan to map reflection through one-parametrized channel.
   */
  MapThruChannel1,

  /**
   * The declarative traverse plan to map reflection through two times parametrized channel.
   */
  MapThruChannel2,

  /**
   * The declarative traverse plan to map reflection through three times parametrized channel.
   */
  MapThruChannel3,

  /**
   * The declarative traverse plan to move reflection through not-parametrized channel.
   */
  MoveThruChannel0,

  /**
   * The declarative traverse plan to move reflection through one-parametrized channel.
   */
  MoveThruChannel1,

  /**
   * The declarative traverse plan to move reflection through two times parametrized channel.
   */
  MoveThruChannel2,

  /**
   * The declarative traverse plan to move reflection through three times parametrized channel.
   */
  MoveThruChannel3,

  /**
   * The declarative traverse plan to map of moving reflection through not-parametrized channel.
   */
  MapOfMovingThruChannel0,

  /**
   * The declarative traverse plan to map of moving reflection through one-parametrized channel.
   */
  MapOfMovingThruChannel1,

  /**
   * The declarative traverse plan to map of moving reflection through two times parametrized channel.
   */
  MapOfMovingChannel2,

  /**
   * The declarative traverse plan to map of moving reflection through three times parametrized channel.
   */
  MapOfMovingThruChannel3,

  /**
   * The declarative traverse plan to map of moving reflection through four times parametrized channel.
   */
  MapOfMovingThruChannel4,

  /**
   * The execution plan to ascend to overlying reflection and call guide.
   */
  AscendAndExecute0,

  /**
   * The execution plan to ascend to overlying reflection and call guide.
   */
  AscendAndExecute1;

  public static TraversePlanTypes of(TraversePlanType value) {
    return VALUES[value.ordinal()];
  }

  private static final TraversePlanTypes[] VALUES = values();
}
