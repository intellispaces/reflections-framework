package tech.intellispaces.reflections.guide.n2;

import tech.intellispaces.reflections.exception.TraverseException;
import tech.intellispaces.reflections.guide.Mover;
import tech.intellispaces.reflections.guide.n3.Mover3;
import tech.intellispaces.reflections.guide.n4.Mover4;
import tech.intellispaces.reflections.guide.n5.Mover5;

/**
 * Mover guide with two qualifiers.
 *
 * @param <S> source handle type.
 * @param <Q1> first qualifier handle type.
 * @param <Q2> second qualifier handle type.
 */
public interface Mover2<S, Q1, Q2> extends
    Guide2<S, S, Q1, Q2>,
    Mover<S>,
    Mover3<S, Q1, Q2, Void>,
    Mover4<S, Q1, Q2, Void, Void>,
    Mover5<S, Q1, Q2, Void, Void, Void>
{
  default S move(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException {
    return traverse(source, qualifier1, qualifier2);
  }

  @Override
  default S move(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3) throws TraverseException {
    return traverse(source, qualifier1, qualifier2);
  }

  @Override
  default S move(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return traverse(source, qualifier1, qualifier2);
  }

  @Override
  default S move(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return traverse(source, qualifier1, qualifier2);
  }

  @SuppressWarnings("unchecked")
  default S traverse(S source, Object... qualifiers) throws TraverseException {
    return traverse(source, (Q1) qualifiers[0], (Q2) qualifiers[1]);
  }

  @Override
  default S traverse(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3) throws TraverseException {
    return traverse(source, qualifier1, qualifier2);
  }

  @Override
  default S traverse(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return traverse(source, qualifier1, qualifier2);
  }

  @Override
  default S traverse(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return traverse(source, qualifier1, qualifier2);
  }
}
