package tech.intellispaces.jaquarius.guide;

public interface GuideKind {

  String name();

  int order();

  boolean isMapper();

  boolean isMover();

  boolean isMapperOfMoving();

  boolean isMovingBased();
}
