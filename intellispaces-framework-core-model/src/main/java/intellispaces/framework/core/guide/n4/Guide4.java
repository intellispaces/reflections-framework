package intellispaces.framework.core.guide.n4;

import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.Guide;

/**
 * Guide with four qualifiers.
 *
 * @param <S> source object type.
 * @param <B> backward object handle type.
 * @param <Q1> first qualifier object type.
 * @param <Q2> second qualifier object type.
 * @param <Q3> third qualifier object type.
 * @param <Q4> fourth qualifier object type.
 */
public interface Guide4<S, B, Q1, Q2, Q3, Q4> extends Guide<S, B> {

  B traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4) throws TraverseException;
}
