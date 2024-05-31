package tech.intellispaces.framework.core.transition;

import java.util.function.Function;

@FunctionalInterface
public interface TransitionMethod0<S, T> extends Function<S, T> {

  T apply(S source);
}
