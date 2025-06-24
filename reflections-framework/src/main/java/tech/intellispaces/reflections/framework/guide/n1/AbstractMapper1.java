package tech.intellispaces.reflections.framework.guide.n1;

import java.util.function.BiFunction;

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

public interface AbstractMapper1<S, T, Q> extends Mapper1<S, T, Q> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mapper1;
  }

  @Override
  default BiFunction<S, Q, T> asBiFunction() {
    return this::map;
  }

  @Override
  default Channel channel() {
    throw NotImplementedExceptions.withCode("pzyMyQ");
  }

  @Override
  default SystemGuide0<S, T> asGuide0() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 1. Requested level is 0");
  }

  @Override
  @SuppressWarnings("unchecked")
  default <_Q> SystemGuide1<S, T, _Q> asGuide1() {
    return (SystemGuide1<S, T, _Q>) this;
  }

  @Override
  default <Q1, Q2> SystemGuide2<S, T, Q1, Q2> asGuide2() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 1. Requested level is 2");
  }

  @Override
  default <Q1, Q2, Q3> SystemGuide3<S, T, Q1, Q2, Q3> asGuide3() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 1. Requested level is 3");
  }

  @Override
  default <Q1, Q2, Q3, Q4> SystemGuide4<S, T, Q1, Q2, Q3, Q4> asGuide4() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 1. Requested level is 4");
  }

  @Override
  default <Q1, Q2, Q3, Q4, Q5> SystemGuide5<S, T, Q1, Q2, Q3, Q4, Q5> asGuide5() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 1. Requested level is 5");
  }
}
