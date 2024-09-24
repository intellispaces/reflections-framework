package intellispaces.framework.core.guide.n3;

import intellispaces.common.base.function.QuadFunction;
import intellispaces.framework.core.guide.GuideKind;
import intellispaces.framework.core.guide.GuideKinds;

public interface AbstractMapperOfMoving3<S, T, Q1, Q2, Q3> extends MapperOfMoving3<S, T, Q1, Q2, Q3> {

  @Override
  default GuideKind kind() {
    return GuideKinds.MapperOfMoving3;
  }

  @Override
  default QuadFunction<S, Q1, Q2, Q3, T> asQuadFunction() {
    return this::traverse;
  }
}
