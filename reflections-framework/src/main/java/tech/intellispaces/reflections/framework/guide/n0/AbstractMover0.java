package tech.intellispaces.reflections.framework.guide.n0;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.WrappedExceptions;
import tech.intellispaces.core.Channel;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.exception.TraverseExceptions;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.guide.GuideKinds;

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
}
