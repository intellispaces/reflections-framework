package tech.intellispaces.jaquarius.guide.n3;

import tech.intellispaces.jaquarius.guide.GuideKind;
import tech.intellispaces.jaquarius.guide.GuideKinds;

public interface AbstractMover3<S, Q1, Q2, Q3> extends Mover3<S, Q1, Q2, Q3> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mover3;
  }
}
