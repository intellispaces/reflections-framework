package tech.intellispaces.reflections.guide.n0;

import java.util.function.Function;

import tech.intellispaces.reflections.guide.GuideKind;
import tech.intellispaces.reflections.guide.GuideKinds;

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
