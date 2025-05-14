package tech.intellispaces.reflections.framework.guide.n4;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.guide.Guide;

/**
 * Guide with four qualifiers.
 *
 * @param <S> the source reflection type.
 * @param <R> the result reflection type.
 * @param <Q1> the first qualifier reflection type.
 * @param <Q2> the second qualifier reflection type.
 * @param <Q3> the third qualifier reflection type.
 * @param <Q4> the fourth qualifier reflection type.
 */
public interface Guide4<S, R, Q1, Q2, Q3, Q4> extends Guide<S, R> {

  R traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4) throws TraverseException;
}
