package tech.intellispaces.reflections.framework.guide.n3;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.core.Channel;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.guide.GuideKinds;

public interface AbstractMover3<S, Q1, Q2, Q3> extends Mover3<S, Q1, Q2, Q3> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mover3;
  }

  @Override
  default Channel channel() {
    throw NotImplementedExceptions.withCode("cmuX0A");
  }
}
