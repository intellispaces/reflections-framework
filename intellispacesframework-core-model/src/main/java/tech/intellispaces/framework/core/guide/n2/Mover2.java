package tech.intellispaces.framework.core.guide.n2;

import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.Mover;
import tech.intellispaces.framework.core.guide.n3.Mover3;
import tech.intellispaces.framework.core.guide.n4.Mover4;
import tech.intellispaces.framework.core.guide.n5.Mover5;

/**
 * Mover guide with two qualifiers.
 *
 * @param <S> source object type.
 * @param <Q1> first qualifier type.
 * @param <Q2> second qualifier type.
 */
public interface Mover2<S, Q1, Q2> extends
    Guide2<S, S, Q1, Q2>,
    Mover<S>,
    Mover3<S, Q1, Q2, Void>,
    Mover4<S, Q1, Q2, Void, Void>,
    Mover5<S, Q1, Q2, Void, Void, Void>
{
  S move(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException;

  @Override
  default S move(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3) throws TraverseException {
    return move(source, qualifier1, qualifier2);
  }

  @Override
  default S move(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return move(source, qualifier1, qualifier2);
  }

  @Override
  default S move(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return move(source, qualifier1, qualifier2);
  }

  @Override
  default S traverse(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException {
    return move(source, qualifier1, qualifier2);
  }

  @SuppressWarnings("unchecked")
  default S traverse(S source, Object... qualifiers) throws TraverseException {
    return move(source, (Q1) qualifiers[0], (Q2) qualifiers[1]);
  }
}
