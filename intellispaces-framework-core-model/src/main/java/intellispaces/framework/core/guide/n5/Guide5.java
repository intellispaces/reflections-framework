package intellispaces.framework.core.guide.n5;

import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.Guide;

/**
 * Guide with five qualifiers.
 *
 * @param <S> source object type.
 * @param <R> result object handle type.
 * @param <Q1> first qualifier object type.
 * @param <Q2> second qualifier object type.
 * @param <Q3> third qualifier object type.
 * @param <Q4> fourth qualifier object type.
 * @param <Q5> fifth qualifier object type.
 */
public interface Guide5<S, R, Q1, Q2, Q3, Q4, Q5> extends Guide<S, R> {

  R traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4, Q5 qualifier5) throws TraverseException;
}
