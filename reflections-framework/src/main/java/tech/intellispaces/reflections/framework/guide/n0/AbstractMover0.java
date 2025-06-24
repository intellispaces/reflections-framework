package tech.intellispaces.reflections.framework.guide.n0;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.exception.WrappedExceptions;
import tech.intellispaces.core.Channel;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.exception.TraverseExceptions;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.guide.GuideKinds;
import tech.intellispaces.reflections.framework.guide.n1.SystemGuide1;
import tech.intellispaces.reflections.framework.guide.n2.SystemGuide2;
import tech.intellispaces.reflections.framework.guide.n3.SystemGuide3;
import tech.intellispaces.reflections.framework.guide.n4.SystemGuide4;
import tech.intellispaces.reflections.framework.guide.n5.SystemGuide5;

public interface AbstractMover0<S> extends Mover0<S> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mover0;
  }

  @Override
  default Consumer<S> asConsumer() {
    return (source) -> {
      try {
        move(source);
      } catch (TraverseException e) {
        throw WrappedExceptions.of(e);
      }
    };
  }

  @Override
  default BiConsumer<S, Void> asBiConsumer() {
    return this::move;
  }

  @Override
  default int traverseToInt(S source) throws TraverseException {
    throw TraverseExceptions.withMessage("Invalid operation");
  }

  @Override
  default double traverseToDouble(S source) throws TraverseException {
    throw TraverseExceptions.withMessage("Invalid operation");
  }

  @Override
  default Channel channel() {
    throw NotImplementedExceptions.withCode("f5zaYQ");
  }

  @Override
  default SystemGuide0<S, S> asGuide0() {
    return this;
  }

  @Override
  default <_Q> SystemGuide1<S, S, _Q> asGuide1() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 0. Requested level is 1");
  }

  @Override
  default <Q1, Q2> SystemGuide2<S, S, Q1, Q2> asGuide2() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 0. Requested level is 2");
  }

  @Override
  default <Q1, Q2, Q3> SystemGuide3<S, S, Q1, Q2, Q3> asGuide3() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 0. Requested level is 3");
  }

  @Override
  default <Q1, Q2, Q3, Q4> SystemGuide4<S, S, Q1, Q2, Q3, Q4> asGuide4() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 0. Requested level is 4");
  }

  @Override
  default <Q1, Q2, Q3, Q4, Q5> SystemGuide5<S, S, Q1, Q2, Q3, Q4, Q5> asGuide5() {
    throw UnexpectedExceptions.withMessage("Incorrect guide level. Actual level is 0. Requested level is 5");
  }
}
