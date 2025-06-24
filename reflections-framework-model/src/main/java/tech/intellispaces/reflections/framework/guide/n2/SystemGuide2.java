package tech.intellispaces.reflections.framework.guide.n2;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.guide.SystemGuide;

/**
 * Guide with two qualifiers.
 *
 * @param <S> the source reflection type.
 * @param <R> the result reflection type.
 * @param <Q1> the first qualifier reflection type.
 * @param <Q2> the second qualifier reflection type.
 */
public interface SystemGuide2<S, R, Q1, Q2> extends SystemGuide<S, R> {

  R traverse(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException;
}
