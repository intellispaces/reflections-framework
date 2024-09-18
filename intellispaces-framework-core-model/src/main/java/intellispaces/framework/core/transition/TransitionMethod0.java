package intellispaces.framework.core.transition;

import java.util.function.Function;

@FunctionalInterface
public interface TransitionMethod0<S, R> extends Function<S, R> {

  R apply(S source);
}
