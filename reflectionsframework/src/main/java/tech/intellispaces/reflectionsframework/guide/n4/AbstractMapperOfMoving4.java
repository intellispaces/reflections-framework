package tech.intellispaces.reflectionsframework.guide.n4;

import tech.intellispaces.reflectionsframework.guide.GuideKind;
import tech.intellispaces.reflectionsframework.guide.GuideKinds;

public interface AbstractMapperOfMoving4<S, T, Q1, Q2, Q3, Q4> extends MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> {

  @Override
  default GuideKind kind() {
    return GuideKinds.MapperOfMoving4;
  }
}
