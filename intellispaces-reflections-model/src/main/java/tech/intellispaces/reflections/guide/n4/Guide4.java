package tech.intellispaces.reflections.guide.n4;

import tech.intellispaces.reflections.exception.TraverseException;
import tech.intellispaces.reflections.guide.Guide;

/**
 * Guide with four qualifiers.
 *
 * @param <S> source type.
 * @param <R> result handle type.
 * @param <Q1> first qualifier handle type.
 * @param <Q2> second qualifier handle type.
 * @param <Q3> third qualifier handle type.
 * @param <Q4> fourth qualifier handle type.
 */
public interface Guide4<S, R, Q1, Q2, Q3, Q4> extends Guide<S, R> {

  R traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4) throws TraverseException;
}
