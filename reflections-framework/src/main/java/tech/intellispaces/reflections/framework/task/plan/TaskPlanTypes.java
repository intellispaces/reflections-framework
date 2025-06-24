package tech.intellispaces.reflections.framework.task.plan;

import tech.intellispaces.commons.abstraction.Enumeration;

/**
 * Task plan types.
 */
public enum TaskPlanTypes implements TaskPlanType, Enumeration<TaskPlanType> {

  /**
   * Call local system guide.
   */
  CallLocalGuide,

  /**
   * Map source of specified class through identified channel.
   */
  MapSourceSpecifiedClassThruIdentifiedChannel,

  /**
   * Move source of the specified class through identified channel.
   */
  MoveSourceSpecifiedClassThruIdentifiedChannel,

  /**
   * Map of moving of the source of specified class through identified channel.
   */
  MapOfMovingSourceSpecifiedClassThruIdentifiedChannel,

  /**
   * The execution plan to ascend to overlying reflection and call guide.
   */
  AscendAndExecute,

  /**
   * Map specified source to specified target domain and specified target reflection class.
   */
  MapSpecifiedSourceToSpecifiedTargetDomainAndClass;


  public static TaskPlanTypes of(TaskPlanType value) {
    return VALUES[value.ordinal()];
  }

  private static final TaskPlanTypes[] VALUES = values();
}
