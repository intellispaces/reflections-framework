package tech.intellispaces.reflections.guide.n3;

import tech.intellispaces.reflections.guide.GuideKind;
import tech.intellispaces.reflections.guide.GuideKinds;

public interface AbstractMover3<S, Q1, Q2, Q3> extends Mover3<S, Q1, Q2, Q3> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mover3;
  }
}
