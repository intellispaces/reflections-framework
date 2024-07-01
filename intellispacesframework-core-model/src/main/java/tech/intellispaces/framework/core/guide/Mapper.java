package tech.intellispaces.framework.core.guide;

/**
 * Mapper guide.
 *
 * <p>Mapper guide maps source object to another related object.
 *
 * <p>Mapper guide does not change the source object.
 *
 * @param <S> source object handle type.
 * @param <T> target object handle type.
 */
public interface Mapper<S, T> extends Guide<S, T> {
}
