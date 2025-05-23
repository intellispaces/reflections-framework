package tech.intellispaces.reflections.framework.guide.n2;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.core.Channel;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.guide.GuideKinds;

public interface AbstractMover2<S, Q1, Q2> extends Mover2<S, Q1, Q2> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mover2;
  }

  @Override
  default Channel channel() {
    throw NotImplementedExceptions.withCode("GzEFKQ");
  }
}
