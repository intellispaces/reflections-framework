package intellispaces.core.transition;

import intellispaces.commons.function.TriFunction;

@FunctionalInterface
public interface TransitionMethod2<S, B, Q1, Q2> extends TriFunction<S, Q1, Q2, B> {

  B apply(S source, Q1 qualifier1, Q2 qualifier2);
}
