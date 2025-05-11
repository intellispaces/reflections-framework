package tech.intellispaces.reflections.framework.guide.n2;

import tech.intellispaces.commons.function.Function3;
import tech.intellispaces.commons.function.Function4;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.guide.GuideKinds;

public interface AbstractMapperOfMoving2<S, T, Q1, Q2> extends MapperOfMoving2<S, T, Q1, Q2> {

  @Override
  default GuideKind kind() {
    return GuideKinds.MapperOfMoving2;
  }

  @Override
  default Function3<S, Q1, Q2, T> asFunction3() {
    return this::traverse;
  }

  @Override
  default Function4<S, Q1, Q2, Void, T> asQuadFunction() {
    return this::traverse;
  }
}
