package tech.intellispaces.reflections.framework.guide;

/**
 * Mapper guide.<p/>
 *
 * Mapper guide maps source object to another related object.<p/>
 *
 * Mapper guides does not change the source object.<p/>
 *
 * Mapper guides always return a reflection to an existing object. Mapper guides does not create new objects.<p/>
 *
 * @param <S> source reflection type.
 * @param <T> target reflection type.
 */
public interface Mapper<S, T> extends Guide<S, T> {
}
