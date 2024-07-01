package tech.intellispaces.framework.core.guide;

import tech.intellispaces.framework.core.guide.n1.Mover1;

import java.util.function.BiConsumer;

public abstract class AbstractMover1<S, B, Q> implements Mover1<S, B, Q> {

  @Override
  public GuideKind kind() {
    return GuideKinds.Mover1;
  }

  @Override
  public BiConsumer<S, Q> asBiConsumer() {
    return this::move;
  }
}
