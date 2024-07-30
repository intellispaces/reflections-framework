package tech.intellispaces.core.guide.n2;

import tech.intellispaces.core.guide.GuideKind;
import tech.intellispaces.core.guide.GuideKinds;

public interface AbstractMover2<S, B, Q1, Q2> extends Mover2<S, B, Q1, Q2> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mover2;
  }
}
