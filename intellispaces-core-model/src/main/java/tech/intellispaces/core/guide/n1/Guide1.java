package tech.intellispaces.core.guide.n1;

import tech.intellispaces.core.exception.TraverseException;
import tech.intellispaces.core.guide.Guide;

/**
 * Guide with one qualifier.
 *
 * @param <S> source object type.
 * @param <B> backward object handle type.
 * @param <Q> qualifier object type.
 */
public interface Guide1<S, B, Q> extends Guide<S, B> {

  B traverse(S source, Q qualifier) throws TraverseException;
}
