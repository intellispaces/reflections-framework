package tech.intellispaces.reflections.framework.guide;

import tech.intellispaces.commons.abstraction.Enumeration;

/**
 * Guide types.
 */
public enum GuideTypes implements GuideType, Enumeration<GuideType> {

  /**
   * The mapper guide.
   */
  Mapper,

  /**
   * The mover guide.
   */
  Mover,

  /**
   * The mapper of moving guide.
   */
  MapperOfMoving;

  @Override
  public boolean isMapper() {
    return (Mapper == this);
  }

  @Override
  public boolean isMover() {
    return (Mover == this);
  }

  @Override
  public boolean isMapperOfMoving() {
    return (MapperOfMoving == this);
  }
}
