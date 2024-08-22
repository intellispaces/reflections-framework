package intellispaces.core.guide.n0;

import intellispaces.core.exception.TraverseException;
import intellispaces.core.guide.Guide;

/**
 * Non-parameterized guide.
 *
 * @param <S> source object type.
 * @param <B> backward object handle type.
 */
public interface Guide0<S, B> extends Guide<S, B> {

  B traverse(S source) throws TraverseException;
}
