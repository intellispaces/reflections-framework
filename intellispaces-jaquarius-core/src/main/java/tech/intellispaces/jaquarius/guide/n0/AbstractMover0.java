package tech.intellispaces.jaquarius.guide.n0;

import tech.intellispaces.entity.exception.WrappedExceptions;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.exception.TraverseExceptions;
import tech.intellispaces.jaquarius.guide.GuideKind;
import tech.intellispaces.jaquarius.guide.GuideKinds;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
}
