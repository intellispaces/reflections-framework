package intellispaces.framework.core.transition;

import java.util.function.Function;

@FunctionalInterface
public interface TransitionMethod0<S, B> extends Function<S, B> {

  B apply(S source);
}
