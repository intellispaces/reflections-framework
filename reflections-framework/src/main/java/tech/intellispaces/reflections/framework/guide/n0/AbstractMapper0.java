package tech.intellispaces.reflections.framework.guide.n0;

import java.util.function.Function;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.core.Channel;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.guide.GuideKinds;

public interface AbstractMapper0<S, T> extends Mapper0<S, T> {

  @Override
  default GuideKind kind() {
    return GuideKinds.Mapper0;
  }

  @Override
  default Function<S, T> asFunction() {
    return this::map;
  }

  @Override
  default Channel channel() {
    throw NotImplementedExceptions.withCode("47zmOg");
  }
}
