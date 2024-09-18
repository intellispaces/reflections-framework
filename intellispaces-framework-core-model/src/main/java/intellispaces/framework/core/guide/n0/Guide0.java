package intellispaces.framework.core.guide.n0;

import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.Guide;

/**
 * Non-parameterized guide.
 *
 * @param <S> source object type.
 * @param <R> result object handle type.
 */
public interface Guide0<S, R> extends Guide<S, R> {

  R traverse(S source) throws TraverseException;
}
