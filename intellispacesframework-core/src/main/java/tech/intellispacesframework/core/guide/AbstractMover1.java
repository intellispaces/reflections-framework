package tech.intellispacesframework.core.guide;

import tech.intellispacesframework.core.guide.n1.Mover1;

import java.util.function.BiConsumer;

public abstract class AbstractMover1<S, Q> implements Mover1<S, Q> {

  @Override
  public GuideKind kind() {
    return GuideKinds.Mover1;
  }

  @Override
  public BiConsumer<S, Q> asBiConsumer() {
    return this::move;
  }
}
