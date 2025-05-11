package tech.intellispaces.reflections.framework.artifact;

import tech.intellispaces.commons.abstraction.Enumeration;
import tech.intellispaces.reflections.framework.ArtifactType;

/**
 * The artifact types.
 */
public enum ArtifactTypes implements ArtifactType, Enumeration<ArtifactType> {

  /**
   * The movable regular object interface.
   */
  MovableRegularObject,

  /**
   * The unmovable regular object interface.
   */
  UnmovableRegularObject,

  /**
   * The general regular object interface.
   */
  RegularObject,

  /**
   * The movable object handle interface.
   */
  MovableObjectHandle,

  /**
   * The unmovable object handle interface.
   */
  UnmovableObjectHandle,

  /**
   * The general object handle interface.
   */
  ObjectHandle,

  /**
   * The channel interface.
   */
  Channel,

  /**
   * The mapper guide interface.
   */
  Mapper,

  /**
   * The mover guide interface.
   */
  Mover,

  /**
   * The mapper of moving guide interface.
   */
  MapperOfMoving,

  /**
   * The unmovable dataset interface.
   */
  UnmovableDataset,

  /**
   * The unmovable downward object class.
   */
  UnmovableDownwardObject,

  /**
   * The movable downward object class.
   */
  MovableDownwardObject,

  /**
   * The unmovable object wrapper class.
   */
  UnmovableObjectWrapper,

  /**
   * The movable object wrapper class.
   */
  MovableObjectWrapper,

  /**
   * The object assistant.
   */
  ObjectAssistant,

  /**
   * The object assistant broker.
   */
  ObjectAssistantBroker,

  /**
   * The dataset builder.
   */
  DatasetBuilder,

  NotSpecified;

  public static ArtifactTypes of(ArtifactType value) {
    return VALUES[value.ordinal()];
  }

  private static final ArtifactTypes[] VALUES = values();
}
