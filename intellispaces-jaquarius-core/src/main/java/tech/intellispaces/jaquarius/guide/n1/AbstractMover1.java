package tech.intellispaces.jaquarius.guide.n1;

import tech.intellispaces.jaquarius.guide.GuideKind;
import tech.intellispaces.jaquarius.guide.GuideKinds;

import java.util.function.BiConsumer;

public interface AbstractMover1<S, Q> extends Mover1<S, Q> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mover1;
  }

  @Override
  default BiConsumer<S, Q> asBiConsumer() {
    return this::move;
  }
}
