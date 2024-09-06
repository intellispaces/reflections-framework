package intellispaces.framework.core.guide.n0;

import intellispaces.framework.core.guide.GuideKind;
import intellispaces.framework.core.guide.GuideKinds;

import java.util.function.Function;

public interface AbstractMapper0<S, T> extends Mapper0<S, T> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mapper0;
  }

  @Override
  default Function<S, T> asFunction() {
    return this::map;
  }
}
