package intellispaces.core.guide.n2;

import intellispaces.commons.function.QuadFunction;
import intellispaces.commons.function.TriFunction;
import intellispaces.core.guide.GuideKind;
import intellispaces.core.guide.GuideKinds;

public interface AbstractMapper2<S, T, Q1, Q2> extends Mapper2<S, T, Q1, Q2> {

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
