package tech.intellispaces.jaquarius.artifact;

import tech.intellispaces.jaquarius.ArtifactType;

/**
 * The artifact types.
 */
public enum ArtifactTypes implements ArtifactType {

  /**
   * The movable plain object interface.
   */
  MovablePlainObject,

  /**
   * The unmovable plain object interface.
   */
  UnmovablePlainObject,

  /**
   * The undefined plain object interface.
   */
  UndefinedPlainObject,

  /**
   * The movable object handle interface.
   */
  MovableObjectHandle,

  /**
   * The unmovable object handle interface.
   */
  UnmovableObjectHandle,

  /**
   * The undefined object handle interface.
   */
  UndefinedObjectHandle,

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
  DatasetBuilder
}
