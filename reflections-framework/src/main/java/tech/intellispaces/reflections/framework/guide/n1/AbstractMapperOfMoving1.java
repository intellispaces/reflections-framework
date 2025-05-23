package tech.intellispaces.reflections.framework.guide.n1;

import java.util.function.BiFunction;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.core.Channel;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.guide.GuideKinds;

public interface AbstractMapperOfMoving1<S, T, Q> extends MapperOfMoving1<S, T, Q> {

  @Override
  default GuideKind kind() {
    return GuideKinds.MapperOfMoving1;
  }

  @Override
  default BiFunction<S, Q, T> asBiFunction() {
    return this::traverse;
  }

  @Override
  default Channel channel() {
    throw NotImplementedExceptions.withCode("bvveEA");
  }
}
