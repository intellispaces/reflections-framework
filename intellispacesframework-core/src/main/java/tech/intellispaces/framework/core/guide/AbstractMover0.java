package tech.intellispaces.framework.core.guide;

import tech.intellispaces.framework.commons.exception.CoveredCheckedException;
import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.n0.Mover0;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AbstractMover0<S, B> implements Mover0<S, B> {

  @Override
  public GuideKind kind() {
    return GuideKinds.Mover0;
  }

  @Override
  public Consumer<S> asConsumer() {
    return (source) -> {
      try {
        move(source);
      } catch (TraverseException e) {
        throw CoveredCheckedException.withCause(e);
      }
    };
  }

  @Override
  public BiConsumer<S, Void> asBiConsumer() {
    return this::move;
  }
}
