package tech.intellispaces.jaquarius.guide.n2;

import tech.intellispaces.jaquarius.guide.GuideKind;
import tech.intellispaces.jaquarius.guide.GuideKinds;

public interface AbstractMover2<S, Q1, Q2> extends Mover2<S, Q1, Q2> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mover2;
  }
}
