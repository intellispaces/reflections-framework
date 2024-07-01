package tech.intellispaces.framework.core.guide.n3;

import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.Mover;
import tech.intellispaces.framework.core.guide.n4.Mover4;
import tech.intellispaces.framework.core.guide.n5.Mover5;

/**
 * Mover guide with three qualifiers.
 *
 * @param <S> source object handle type.
 * @param <B> backward object handle type.
 * @param <Q1> first qualifier handle type.
 * @param <Q2> second qualifier handle type.
 * @param <Q3> third qualifier handle type.
 */
public interface Mover3<S, B, Q1, Q2, Q3> extends
    Guide3<S, B, Q1, Q2, Q3>,
    Mover<S, B>,
    Mover4<S, B, Q1, Q2, Q3, Void>,
    Mover5<S, B, Q1, Q2, Q3, Void, Void>
{
  B move(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3) throws TraverseException;

  @Override
  default B move(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Void qualifier4) throws TraverseException {
    return move(source, qualifier1, qualifier2, qualifier3);
  }

  @Override
  default B move(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return move(source, qualifier1, qualifier2, qualifier3);
  }

  @Override
  default B traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3) throws TraverseException {
    return move(source, qualifier1, qualifier2, qualifier3);
  }

  @SuppressWarnings("unchecked")
  default B traverse(S source, Object... qualifiers) throws TraverseException {
    return move(source, (Q1) qualifiers[0], (Q2) qualifiers[1], (Q3) qualifiers[2]);
  }
}
