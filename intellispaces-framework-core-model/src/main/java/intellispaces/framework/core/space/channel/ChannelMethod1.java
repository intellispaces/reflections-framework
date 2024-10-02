package intellispaces.framework.core.space.channel;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ChannelMethod1<S, R, Q> extends BiFunction<S, Q, R> {

  R apply(S source, Q qualifier);
}
