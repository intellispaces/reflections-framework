package tech.intellispacesframework.core.guide;

import tech.intellispacesframework.commons.exception.CoveredCheckedException;
import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.guide.n0.Mover0;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AbstractMover0<S> implements Mover0<S> {

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
