package tech.intellispaces.reflectionsj.guide.n1;

import java.util.function.BiFunction;

import tech.intellispaces.reflectionsj.guide.GuideKind;
import tech.intellispaces.reflectionsj.guide.GuideKinds;

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
