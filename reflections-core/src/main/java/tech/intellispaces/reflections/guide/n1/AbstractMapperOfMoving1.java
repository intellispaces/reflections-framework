package tech.intellispaces.reflections.guide.n1;

import java.util.function.BiFunction;

import tech.intellispaces.reflections.guide.GuideKind;
import tech.intellispaces.reflections.guide.GuideKinds;

public interface AbstractMapperOfMoving1<S, T, Q> extends MapperOfMoving1<S, T, Q> {

  @Override
  default GuideKind kind() {
    return GuideKinds.MapperOfMoving1;
  }

  @Override
  default BiFunction<S, Q, T> asBiFunction() {
    return this::traverse;
  }
}
