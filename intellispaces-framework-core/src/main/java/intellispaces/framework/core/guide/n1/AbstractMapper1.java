package intellispaces.framework.core.guide.n1;

import intellispaces.framework.core.guide.GuideKind;
import intellispaces.framework.core.guide.GuideKinds;

import java.util.function.BiFunction;

public interface AbstractMapper1<S, T, Q> extends Mapper1<S, T, Q> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mapper1;
  }

  @Override
  default BiFunction<S, Q, T> asBiFunction() {
    return this::map;
  }
}
