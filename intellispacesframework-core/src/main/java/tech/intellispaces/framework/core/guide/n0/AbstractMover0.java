package tech.intellispaces.framework.core.guide.n0;

import tech.intellispaces.framework.commons.exception.CoveredCheckedException;
import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.GuideKind;
import tech.intellispaces.framework.core.guide.GuideKinds;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface AbstractMover0<S, B> extends Mover0<S, B> {

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
        throw CoveredCheckedException.withCause(e);
      }
    };
  }

  @Override
  default BiConsumer<S, Void> asBiConsumer() {
    return this::move;
  }
}
