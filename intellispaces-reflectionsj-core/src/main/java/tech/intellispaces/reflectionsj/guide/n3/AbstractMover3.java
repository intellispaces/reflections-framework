package tech.intellispaces.reflectionsj.guide.n3;

import tech.intellispaces.reflectionsj.guide.GuideKind;
import tech.intellispaces.reflectionsj.guide.GuideKinds;

public interface AbstractMover3<S, Q1, Q2, Q3> extends Mover3<S, Q1, Q2, Q3> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mover3;
  }
}
