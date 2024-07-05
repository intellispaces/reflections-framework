package tech.intellispaces.framework.core.guide.n1;

import tech.intellispaces.framework.core.guide.GuideKind;
import tech.intellispaces.framework.core.guide.GuideKinds;

import java.util.function.BiFunction;

public interface BasicMapper1<S, T, Q> extends Mapper1<S, T, Q> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mapper1;
  }

  @Override
  default BiFunction<S, Q, T> asBiFunction() {
    return this::map;
  }
}
