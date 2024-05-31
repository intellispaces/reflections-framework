package tech.intellispacesframework.core.guide;

import tech.intellispacesframework.core.guide.n0.Mapper0;

import java.util.function.Function;

public abstract class AbstractMapper0<S, T> implements Mapper0<S, T> {

  @Override
  public GuideKind kind() {
    return GuideKinds.Mapper0;
  }

  @Override
  public Function<S, T> asFunction() {
    return this::map;
  }
}
