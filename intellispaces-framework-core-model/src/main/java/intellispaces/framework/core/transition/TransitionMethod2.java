package intellispaces.framework.core.transition;

import intellispaces.common.base.function.TriFunction;

@FunctionalInterface
public interface TransitionMethod2<S, R, Q1, Q2> extends TriFunction<S, Q1, Q2, R> {

  R apply(S source, Q1 qualifier1, Q2 qualifier2);
}
