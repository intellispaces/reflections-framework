package tech.intellispaces.reflectionsframework.channel;

/**
 * The channel function without qualifiers.
 *
 * @param <S> the source domain type.
 * @param <R> the result domain type.
 */
@FunctionalInterface
public interface ChannelFunction0<S, R> {

  R traverse(S source);
}
