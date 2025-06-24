package tech.intellispaces.reflections.framework.guide.n0;

import java.util.function.Function;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.core.Channel;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.guide.GuideKinds;
import tech.intellispaces.reflections.framework.guide.n1.SystemGuide1;
import tech.intellispaces.reflections.framework.guide.n2.SystemGuide2;
import tech.intellispaces.reflections.framework.guide.n3.SystemGuide3;
import tech.intellispaces.reflections.framework.guide.n4.SystemGuide4;
import tech.intellispaces.reflections.framework.guide.n5.SystemGuide5;

public interface AbstractMapper0<S, T> extends Mapper0<S, T> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mapper0;
  }

  @Override
  default Function<S, T> asFunction() {
    return this::map;
  }

  @Override
  default Channel channel() {
    throw NotImplementedExceptions.withCode("47zmOg");
  }

  @Override
  default SystemGuide0<S, T> asGuide0() {
    return this;
  }

  @Override
  default <_Q> SystemGuide1<S, T, _Q> asGuide1() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 0. Requested level is 1");
  }

  @Override
  default <Q1, Q2> SystemGuide2<S, T, Q1, Q2> asGuide2() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 0. Requested level is 2");
  }

  @Override
  default <Q1, Q2, Q3> SystemGuide3<S, T, Q1, Q2, Q3> asGuide3() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 0. Requested level is 3");
  }

  @Override
  default <Q1, Q2, Q3, Q4> SystemGuide4<S, T, Q1, Q2, Q3, Q4> asGuide4() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 0. Requested level is 4");
  }

  @Override
  default <Q1, Q2, Q3, Q4, Q5> SystemGuide5<S, T, Q1, Q2, Q3, Q4, Q5> asGuide5() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 0. Requested level is 5");
  }
}
