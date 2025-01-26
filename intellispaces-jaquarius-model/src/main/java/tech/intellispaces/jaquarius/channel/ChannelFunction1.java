package tech.intellispaces.jaquarius.channel;

/**
 * The channel function with one qualifier.
 *
 * @param <S> the source domain type.
 * @param <R> the result domain type.
 * @param <Q> the qualifier domain type.
 */
@FunctionalInterface
public interface ChannelFunction1<S, R, Q> {

  R traverse(S source, Q qualifier);
}
