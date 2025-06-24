package tech.intellispaces.reflections.framework.guide.n1;

import java.util.function.BiConsumer;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.core.Channel;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.guide.GuideKinds;
import tech.intellispaces.reflections.framework.guide.n0.SystemGuide0;
import tech.intellispaces.reflections.framework.guide.n2.SystemGuide2;
import tech.intellispaces.reflections.framework.guide.n3.SystemGuide3;
import tech.intellispaces.reflections.framework.guide.n4.SystemGuide4;
import tech.intellispaces.reflections.framework.guide.n5.SystemGuide5;

public interface AbstractMover1<S, Q> extends Mover1<S, Q> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mover1;
  }

  @Override
  default BiConsumer<S, Q> asBiConsumer() {
    return this::move;
  }

  @Override
  default Channel channel() {
    throw NotImplementedExceptions.withCode("ibx3PA");
  }

  @Override
  default SystemGuide0<S, S> asGuide0() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 1. Requested level is 0");
  }

  @Override
  @SuppressWarnings("unchecked")
  default <_Q> SystemGuide1<S, S, _Q> asGuide1() {
    return (SystemGuide1<S, S, _Q>) this;
  }

  @Override
  default <Q1, Q2> SystemGuide2<S, S, Q1, Q2> asGuide2() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 1. Requested level is 2");
  }

  @Override
  default <Q1, Q2, Q3> SystemGuide3<S, S, Q1, Q2, Q3> asGuide3() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 1. Requested level is 3");
  }

  @Override
  default <Q1, Q2, Q3, Q4> SystemGuide4<S, S, Q1, Q2, Q3, Q4> asGuide4() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 1. Requested level is 4");
  }

  @Override
  default <Q1, Q2, Q3, Q4, Q5> SystemGuide5<S, S, Q1, Q2, Q3, Q4, Q5> asGuide5() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 1. Requested level is 5");
  }
}
