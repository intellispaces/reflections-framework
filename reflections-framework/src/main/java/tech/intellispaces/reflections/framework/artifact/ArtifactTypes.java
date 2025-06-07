package tech.intellispaces.reflections.framework.artifact;

import tech.intellispaces.commons.abstraction.Enumeration;
import tech.intellispaces.reflections.framework.ArtifactType;

/**
 * The artifact types.
 */
public enum ArtifactTypes implements ArtifactType, Enumeration<ArtifactType> {

  /**
   * The general reflection interface.
   */
  Reflection,

  /**
   * The movable reflection interface.
   */
  MovableReflection,

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
   * The object assistant handle.
   */
  ObjectAssistantHandle,

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
