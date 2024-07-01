package tech.intellispaces.framework.core.guide.n2;

import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.Guide;

/**
 * Guide with two qualifiers.
 *
 * @param <S> source object type.
 * @param <B> backward object handle type.
 * @param <Q1> first qualifier object type.
 * @param <Q2> second qualifier object type.
 */
public interface Guide2<S, B, Q1, Q2> extends Guide<S, B> {

  B traverse(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException;
}
