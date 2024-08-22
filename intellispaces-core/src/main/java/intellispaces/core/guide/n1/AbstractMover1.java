package intellispaces.core.guide.n1;

import intellispaces.core.guide.GuideKind;
import intellispaces.core.guide.GuideKinds;

import java.util.function.BiConsumer;

public interface AbstractMover1<S, B, Q> extends Mover1<S, B, Q> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mover1;
  }

  @Override
  default BiConsumer<S, Q> asBiConsumer() {
    return this::move;
  }
}
