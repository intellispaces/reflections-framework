package intellispaces.framework.core.guide.n2;

import intellispaces.framework.core.guide.GuideKind;
import intellispaces.framework.core.guide.GuideKinds;

public interface AbstractMover2<S, R, Q1, Q2> extends Mover2<S, R, Q1, Q2> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mover2;
  }
}
