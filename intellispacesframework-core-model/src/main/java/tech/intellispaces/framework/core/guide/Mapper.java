package tech.intellispaces.framework.core.guide;

/**
 * Mapper guide.<p/>
 *
 * Mapper guide maps source object to another related object.<p/>
 *
 * Mapper guides does not change the source object.<p/>
 *
 * Mapper guides always return a handle to an existing object. Mapper guides does not create new objects.<p/>
 *
 * @param <S> source object handle type.
 * @param <T> target object handle type.
 */
public interface Mapper<S, T> extends Guide<S, T> {
}
