package intellispaces.framework.core.guide.n2;

import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.Mover;
import intellispaces.framework.core.guide.n3.Mover3;
import intellispaces.framework.core.guide.n4.Mover4;
import intellispaces.framework.core.guide.n5.Mover5;

/**
 * Mover guide with two qualifiers.
 *
 * @param <S> source object handle type.
 * @param <R> result object handle type.
 * @param <Q1> first qualifier handle type.
 * @param <Q2> second qualifier handle type.
 */
public interface Mover2<S, R, Q1, Q2> extends
    Guide2<S, R, Q1, Q2>,
    Mover<S, R>,
    Mover3<S, R, Q1, Q2, Void>,
    Mover4<S, R, Q1, Q2, Void, Void>,
    Mover5<S, R, Q1, Q2, Void, Void, Void>
{
  R move(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException;

  @Override
  default R move(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3) throws TraverseException {
    return move(source, qualifier1, qualifier2);
  }

  @Override
  default R move(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return move(source, qualifier1, qualifier2);
  }

  @Override
  default R move(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return move(source, qualifier1, qualifier2);
  }

  @Override
  default R traverse(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException {
    return move(source, qualifier1, qualifier2);
  }

  @SuppressWarnings("unchecked")
  default R traverse(S source, Object... qualifiers) throws TraverseException {
    return move(source, (Q1) qualifiers[0], (Q2) qualifiers[1]);
  }
}
