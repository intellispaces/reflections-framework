package tech.intellispaces.reflectionsframework.channel;

/**
 * The channel function with two qualifiers.
 *
 * @param <S> the source domain type.
 * @param <R> the result domain type.
 * @param <Q1> the first qualifier domain type.
 * @param <Q2> the second qualifier domain type.
 */
@FunctionalInterface
public interface ChannelFunction2<S, R, Q1, Q2> {

  R traverse(S source, Q1 qualifier1, Q2 qualifier2);
}
