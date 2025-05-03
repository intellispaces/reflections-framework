package tech.intellispaces.reflections.guide.n1;

import java.util.function.BiConsumer;

import tech.intellispaces.reflections.guide.GuideKind;
import tech.intellispaces.reflections.guide.GuideKinds;

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
