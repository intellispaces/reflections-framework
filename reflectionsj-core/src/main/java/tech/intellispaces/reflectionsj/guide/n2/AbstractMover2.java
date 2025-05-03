package tech.intellispaces.reflectionsj.guide.n2;

import tech.intellispaces.reflectionsj.guide.GuideKind;
import tech.intellispaces.reflectionsj.guide.GuideKinds;

public interface AbstractMover2<S, Q1, Q2> extends Mover2<S, Q1, Q2> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mover2;
  }
}
