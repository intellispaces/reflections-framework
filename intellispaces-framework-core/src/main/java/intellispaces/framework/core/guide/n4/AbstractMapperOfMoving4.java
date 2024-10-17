package intellispaces.framework.core.guide.n4;

import intellispaces.framework.core.guide.GuideKind;
import intellispaces.framework.core.guide.GuideKinds;

public interface AbstractMapperOfMoving4<S, T, Q1, Q2, Q3, Q4> extends MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> {

  @Override
  default GuideKind kind() {
    return GuideKinds.MapperOfMoving4;
  }
}
