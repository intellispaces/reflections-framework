package tech.intellispaces.jaquarius.guide.n1;

import tech.intellispaces.jaquarius.guide.GuideKind;
import tech.intellispaces.jaquarius.guide.GuideKinds;

import java.util.function.BiFunction;

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
