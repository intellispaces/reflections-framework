package tech.intellispaces.framework.core.guide.n2;

import tech.intellispaces.framework.commons.function.QuadFunction;
import tech.intellispaces.framework.commons.function.TriFunction;
import tech.intellispaces.framework.core.guide.GuideKind;
import tech.intellispaces.framework.core.guide.GuideKinds;

public interface BasicMapper2<S, T, Q1, Q2> extends Mapper2<S, T, Q1, Q2> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mapper2;
  }

  @Override
  default TriFunction<S, Q1, Q2, T> asTriFunction() {
    return this::map;
  }

  @Override
  default QuadFunction<S, Q1, Q2, Void, T> asQuadFunction() {
    return this::map;
  }
}
