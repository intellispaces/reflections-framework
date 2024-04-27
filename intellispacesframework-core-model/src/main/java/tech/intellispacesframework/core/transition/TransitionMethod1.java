package tech.intellispacesframework.core.transition;

import java.util.function.BiFunction;

@FunctionalInterface
public interface TransitionMethod1<S, T, Q> extends BiFunction<S, Q, T> {

  T apply(S source, Q qualifier);
}
