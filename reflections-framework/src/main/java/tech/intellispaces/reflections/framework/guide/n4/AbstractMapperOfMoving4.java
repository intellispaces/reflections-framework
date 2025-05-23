package tech.intellispaces.reflections.framework.guide.n4;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.core.Channel;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.guide.GuideKinds;

public interface AbstractMapperOfMoving4<S, T, Q1, Q2, Q3, Q4> extends MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> {

  @Override
  default GuideKind kind() {
    return GuideKinds.MapperOfMoving4;
  }

  @Override
  default Channel channel() {
    throw NotImplementedExceptions.withCode("eXtbbw");
  }
}
