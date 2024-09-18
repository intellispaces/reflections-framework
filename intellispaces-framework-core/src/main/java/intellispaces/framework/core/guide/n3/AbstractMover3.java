package intellispaces.framework.core.guide.n3;

import intellispaces.framework.core.guide.GuideKind;
import intellispaces.framework.core.guide.GuideKinds;

public interface AbstractMover3<S, B, Q1, Q2, Q3> extends Mover3<S, B, Q1, Q2, Q3> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mover3;
  }
}
