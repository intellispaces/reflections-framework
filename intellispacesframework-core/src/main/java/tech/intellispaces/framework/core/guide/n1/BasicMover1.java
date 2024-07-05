package tech.intellispaces.framework.core.guide.n1;

import tech.intellispaces.framework.core.guide.GuideKind;
import tech.intellispaces.framework.core.guide.GuideKinds;

import java.util.function.BiConsumer;

public interface BasicMover1<S, B, Q> extends Mover1<S, B, Q> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mover1;
  }

  @Override
  default BiConsumer<S, Q> asBiConsumer() {
    return this::move;
  }
}
