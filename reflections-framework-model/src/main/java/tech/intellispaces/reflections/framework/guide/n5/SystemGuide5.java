package tech.intellispaces.reflections.framework.guide.n5;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.guide.SystemGuide;

/**
 * Guide with five qualifiers.
 *
 * @param <S> the source reflection type.
 * @param <R> the result reflection type.
 * @param <Q1> the first qualifier reflection type.
 * @param <Q2> the second qualifier reflection type.
 * @param <Q3> the third qualifier reflection type.
 * @param <Q4> the fourth qualifier reflection type.
 * @param <Q5> the fifth qualifier reflection type.
 */
public interface SystemGuide5<S, R, Q1, Q2, Q3, Q4, Q5> extends SystemGuide<S, R> {

  R traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4, Q5 qualifier5) throws TraverseException;
}
