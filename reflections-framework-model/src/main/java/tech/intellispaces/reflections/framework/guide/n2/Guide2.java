package tech.intellispaces.reflections.framework.guide.n2;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.guide.Guide;

/**
 * Guide with two qualifiers.
 *
 * @param <S> the source reflection type.
 * @param <R> the result reflection type.
 * @param <Q1> the first qualifier reflection type.
 * @param <Q2> the second qualifier reflection type.
 */
public interface Guide2<S, R, Q1, Q2> extends Guide<S, R> {

  R traverse(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException;
}
