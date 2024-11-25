package tech.intellispaces.jaquarius.guide.n5;

import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.guide.Guide;

/**
 * Guide with five qualifiers.
 *
 * @param <S> source handle type.
 * @param <R> result handle type.
 * @param <Q1> first qualifier handle type.
 * @param <Q2> second qualifier handle type.
 * @param <Q3> third qualifier handle type.
 * @param <Q4> fourth qualifier handle type.
 * @param <Q5> fifth qualifier handle type.
 */
public interface Guide5<S, R, Q1, Q2, Q3, Q4, Q5> extends Guide<S, R> {

  R traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4, Q5 qualifier5) throws TraverseException;
}
