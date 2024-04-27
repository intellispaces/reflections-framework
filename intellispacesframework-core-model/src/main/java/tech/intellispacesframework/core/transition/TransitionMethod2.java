package tech.intellispacesframework.core.transition;

import tech.intellispacesframework.commons.function.TriFunction;

@FunctionalInterface
public interface TransitionMethod2<S, T, Q1, Q2> extends TriFunction<S, Q1, Q2, T> {

  T apply(S source, Q1 qualifier1, Q2 qualifier2);
}
