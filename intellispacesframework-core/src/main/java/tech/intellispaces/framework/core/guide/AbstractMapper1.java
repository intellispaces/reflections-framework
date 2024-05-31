package tech.intellispaces.framework.core.guide;

import tech.intellispaces.framework.core.guide.n1.Mapper1;

import java.util.function.BiFunction;

public abstract class AbstractMapper1<S, T, Q> implements Mapper1<S, T, Q> {

  @Override
  public GuideKind kind() {
    return GuideKinds.Mapper1;
  }

  @Override
  public BiFunction<S, Q, T> asBiFunction() {
    return this::map;
  }
}
