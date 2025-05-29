package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.commons.abstraction.Enumeration;

/**
 * Task plan types.
 */
public enum TraversePlanTypes implements TraversePlanType, Enumeration<TraversePlanType> {

  /**
   * Call local not-parametrized guide.
   */
  CallLocalGuide0,

  /**
   * Call local guide with one unspecified qualifier.
   */
  CallLocalGuide1,

  /**
   * Call local guide with two unspecified qualifiers.
   */
  CallLocalGuide2,

  /**
   * Call local guide with three unspecified qualifiers.
   */
  CallLocalGuide3,

  /**
   * Call local guide with four unspecified qualifiers.
   */
  CallLocalGuide4,

  /**
   * Map source of specified class through identified not-parametrized channel.
   */
  MapSpecifiedClassSourceThruIdentifiedChannel0,

  /**
   * Map source of specified class through identified channel with one unspecified qualifiers.
   */
  MapSpecifiedClassSourceThruIdentifiedChannel1,

  /**
   * Map source of specified class through identified channel with two unspecified qualifiers.
   */
  MapSpecifiedClassSourceThruIdentifiedChannel2,

  /**
   * Map source of specified class through identified channel with three unspecified qualifiers.
   */
  MapSpecifiedClassSourceThruIdentifiedChannel3,

  /**
   * Move source of specified class through identified not-parametrized channel.
   */
  MoveSpecifiedClassSourceThruIdentifiedChannel0,

  /**
   * Map source of specified class through identified channel with one unspecified qualifiers.
   */
  MoveSpecifiedClassSourceThruIdentifiedChannel1,

  /**
   * Map source of specified class through identified channel with ywo unspecified qualifiers.
   */
  MoveSpecifiedClassSourceThruIdentifiedChannel2,

  /**
   * Map source of specified class through identified channel with three unspecified qualifiers.
   */
  MoveSpecifiedClassSourceThruIdentifiedChannel3,

  /**
   * Map of moving of the source of specified class through identified not-parametrized channel.
   */
  MapOfMovingSpecifiedClassSourceThruIdentifiedChannel0,

  /**
   * Map of moving of the source of specified class through identified channel with one unspecified qualifiers.
   */
  MapOfMovingSpecifiedClassSourceThruIdentifiedChannel1,

  /**
   * Map of moving of the source of specified class through identified channel with two unspecified qualifiers.
   */
  MapOfMovingSpecifiedClassSourceThruIdentifiedChannel2,

  /**
   * Map of moving of the source of specified class through identified channel with three unspecified qualifiers.
   */
  MapOfMovingSpecifiedClassSourceThruIdentifiedChannel3,

  /**
   * Map of moving of the source of specified class through identified channel with four unspecified qualifiers.
   */
  MapOfMovingSpecifiedClassSourceThruIdentifiedChannel4,

  /**
   * The execution plan to ascend to overlying reflection and call guide.
   */
  AscendAndExecute0,

  /**
   * The execution plan to ascend to overlying reflection and call guide.
   */
  AscendAndExecute1,

  /**
   * Map specified source to specified target domain and specified target reflection class.
   */
  MapSpecifiedSourceToSpecifiedTargetDomainAndClass;


  public static TraversePlanTypes of(TraversePlanType value) {
    return VALUES[value.ordinal()];
  }

  private static final TraversePlanTypes[] VALUES = values();
}
