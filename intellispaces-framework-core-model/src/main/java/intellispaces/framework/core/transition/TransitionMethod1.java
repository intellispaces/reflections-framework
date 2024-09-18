package intellispaces.framework.core.transition;

import java.util.function.BiFunction;

@FunctionalInterface
public interface TransitionMethod1<S, R, Q> extends BiFunction<S, Q, R> {

  R apply(S source, Q qualifier);
}
