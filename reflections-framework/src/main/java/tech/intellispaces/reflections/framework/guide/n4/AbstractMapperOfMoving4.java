package tech.intellispaces.reflections.framework.guide.n4;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.core.Channel;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.guide.GuideKinds;
import tech.intellispaces.reflections.framework.guide.n0.SystemGuide0;
import tech.intellispaces.reflections.framework.guide.n1.SystemGuide1;
import tech.intellispaces.reflections.framework.guide.n2.SystemGuide2;
import tech.intellispaces.reflections.framework.guide.n3.SystemGuide3;
import tech.intellispaces.reflections.framework.guide.n5.SystemGuide5;

public interface AbstractMapperOfMoving4<S, T, Q1, Q2, Q3, Q4> extends MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> {

  @Override
  default GuideKind kind() {
    return GuideKinds.MapperOfMoving4;
  }

  @Override
  default Channel channel() {
    throw NotImplementedExceptions.withCode("eXtbbw");
  }

  @Override
  default SystemGuide0<S, T> asGuide0() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 4. Requested level is 0");
  }

  @Override
  default <_Q> SystemGuide1<S, T, _Q> asGuide1() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 4. Requested level is 1");
  }

  @Override
  default <_Q1, _Q2> SystemGuide2<S, T, _Q1, _Q2> asGuide2() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 4. Requested level is 1");
  }

  @Override
  default <_Q1, _Q2, _Q3> SystemGuide3<S, T, _Q1, _Q2, _Q3> asGuide3() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 4. Requested level is 3");
  }

  @Override
  @SuppressWarnings("unchecked")
  default <_Q1, _Q2, _Q3, _Q4> SystemGuide4<S, T, _Q1, _Q2, _Q3, _Q4> asGuide4() {
    return (SystemGuide4<S, T, _Q1, _Q2, _Q3, _Q4>) this;
  }

  @Override
  default <_Q1, _Q2, _Q3, _Q4, _Q5> SystemGuide5<S, T, _Q1, _Q2, _Q3, _Q4, _Q5> asGuide5() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 4. Requested level is 5");
  }
}
