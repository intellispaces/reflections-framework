package tech.intellispaces.framework.core.guide.n3;

import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.Guide;

/**
 * Guide with three qualifiers.
 *
 * @param <S> source object type.
 * @param <B> backward object handle type.
 * @param <Q1> first qualifier object type.
 * @param <Q2> second qualifier object type.
 * @param <Q3> third qualifier object type.
 */
public interface Guide3<S, B, Q1, Q2, Q3> extends Guide<S, B> {

  B traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3) throws TraverseException;
}
