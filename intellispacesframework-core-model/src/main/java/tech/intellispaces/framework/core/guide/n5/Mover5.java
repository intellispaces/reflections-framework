package tech.intellispaces.framework.core.guide.n5;

import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.Mover;

/**
 * Mover guide with five qualifiers.
 *
 * @param <S> source object handle type.
 * @param <B> backward object handle type.
 * @param <Q1> first qualifier handle type.
 * @param <Q2> second qualifier handle type.
 * @param <Q3> third qualifier handle type.
 * @param <Q4> fourth qualifier handle type.
 * @param <Q5> fifth qualifier handle type.
 */
public interface Mover5<S, B, Q1, Q2, Q3, Q4, Q5> extends
    Guide5<S, B, Q1, Q2, Q3, Q4, Q5>,
    Mover<S, B>
{
  B move(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4, Q5 qualifier5) throws TraverseException;

  @Override
  default B traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4, Q5 qualifier5) throws TraverseException {
    return move(source, qualifier1, qualifier2, qualifier3, qualifier4, qualifier5);
  }

  @Override
  @SuppressWarnings("unchecked")
  default B traverse(S source, Object... qualifiers) throws TraverseException {
    return move(source, (Q1) qualifiers[0], (Q2) qualifiers[1], (Q3) qualifiers[2], (Q4) qualifiers[3], (Q5) qualifiers[4]);
  }
}
