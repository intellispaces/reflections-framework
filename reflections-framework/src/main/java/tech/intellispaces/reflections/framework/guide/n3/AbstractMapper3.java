package tech.intellispaces.reflections.framework.guide.n3;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.function.Function4;
import tech.intellispaces.core.Channel;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.guide.GuideKinds;

public interface AbstractMapper3<S, T, Q1, Q2, Q3> extends Mapper3<S, T, Q1, Q2, Q3> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mapper3;
  }

  @Override
  default Function4<S, Q1, Q2, Q3, T> asFunction4() {
    return this::map;
  }

  @Override
  default Channel channel() {
    throw NotImplementedExceptions.withCode("tZLQ+g");
  }
}
