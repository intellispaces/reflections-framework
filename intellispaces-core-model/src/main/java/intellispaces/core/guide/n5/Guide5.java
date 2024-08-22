package intellispaces.core.guide.n5;

import intellispaces.core.exception.TraverseException;
import intellispaces.core.guide.Guide;

/**
 * Guide with five qualifiers.
 *
 * @param <S> source object type.
 * @param <B> backward object handle type.
 * @param <Q1> first qualifier object type.
 * @param <Q2> second qualifier object type.
 * @param <Q3> third qualifier object type.
 * @param <Q4> fourth qualifier object type.
 * @param <Q5> fifth qualifier object type.
 */
public interface Guide5<S, B, Q1, Q2, Q3, Q4, Q5> extends Guide<S, B> {

  B traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4, Q5 qualifier5) throws TraverseException;
}
