package tech.intellispaces.framework.core.transition;

import java.util.function.BiFunction;

@FunctionalInterface
public interface TransitionMethod1<S, B, Q> extends BiFunction<S, Q, B> {

  B apply(S source, Q qualifier);
}
