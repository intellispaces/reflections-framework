package intellispaces.core.guide.n4;

import intellispaces.core.exception.TraverseException;
import intellispaces.core.guide.Mover;
import intellispaces.core.guide.n5.Mover5;

/**
 * Mover guide with four qualifiers.
 *
 * @param <S> source object handle type.
 * @param <B> backward object handle type.
 * @param <Q1> first qualifier handle type.
 * @param <Q2> second qualifier handle type.
 * @param <Q3> third qualifier handle type.
 * @param <Q4> fourth qualifier handle type.
 */
public interface Mover4<S, B, Q1, Q2, Q3, Q4> extends
    Guide4<S, B, Q1, Q2, Q3, Q4>,
    Mover<S, B>,
    Mover5<S, B, Q1, Q2, Q3, Q4, Void>
{
  B move(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4) throws TraverseException;

  @Override
  default B move(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4, Void qualifier5) throws TraverseException {
    return move(source, qualifier1, qualifier2, qualifier3, qualifier4);
  }

  @Override
  default B traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4) throws TraverseException {
    return move(source, qualifier1, qualifier2, qualifier3, qualifier4);
  }

  @SuppressWarnings("unchecked")
  default B traverse(S source, Object... qualifiers) throws TraverseException {
    return move(source, (Q1) qualifiers[0], (Q2) qualifiers[1], (Q3) qualifiers[2], (Q4) qualifiers[3]);
  }
}
