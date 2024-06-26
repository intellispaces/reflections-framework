package tech.intellispaces.framework.core.guide.n4;

import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.Mover;
import tech.intellispaces.framework.core.guide.n5.Mover5;

/**
 * Mover guide with four qualifiers.
 *
 * @param <S> source object type.
 * @param <Q1> first qualifier type.
 * @param <Q2> second qualifier type.
 * @param <Q3> third qualifier type.
 * @param <Q4> fourth qualifier type.
 */
public interface Mover4<S, Q1, Q2, Q3, Q4> extends
    Guide4<S, S, Q1, Q2, Q3, Q4>,
    Mover<S>,
    Mover5<S, Q1, Q2, Q3, Q4, Void>
{
  S move(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4) throws TraverseException;

  @Override
  default S move(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4, Void qualifier5) throws TraverseException {
    return move(source, qualifier1, qualifier2, qualifier3, qualifier4);
  }

  @Override
  default S traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4) throws TraverseException {
    return move(source, qualifier1, qualifier2, qualifier3, qualifier4);
  }

  @SuppressWarnings("unchecked")
  default S traverse(S source, Object... qualifiers) throws TraverseException {
    return move(source, (Q1) qualifiers[0], (Q2) qualifiers[1], (Q3) qualifiers[2], (Q4) qualifiers[3]);
  }
}
