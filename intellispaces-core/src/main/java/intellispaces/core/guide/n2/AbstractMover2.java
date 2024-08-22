package intellispaces.core.guide.n2;

import intellispaces.core.guide.GuideKind;
import intellispaces.core.guide.GuideKinds;

public interface AbstractMover2<S, B, Q1, Q2> extends Mover2<S, B, Q1, Q2> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mover2;
  }
}
