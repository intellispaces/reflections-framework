package tech.intellispaces.reflections.framework.guide;

import tech.intellispaces.commons.abstraction.Enumerable;

/**
 * The guide type.
 */
public interface GuideType extends Enumerable<GuideType> {

  boolean isMapper();

  boolean isMover();

  boolean isMapperOfMoving();
}
