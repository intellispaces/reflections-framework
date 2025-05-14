package tech.intellispaces.reflections.framework.guide.n3;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.guide.Mover;
import tech.intellispaces.reflections.framework.guide.n4.Mover4;
import tech.intellispaces.reflections.framework.guide.n5.Mover5;

/**
 * Mover guide with three qualifiers.
 *
 * @param <S> the source reflection type.
 * @param <Q1> the first qualifier reflection type.
 * @param <Q2> the second qualifier reflection type.
 * @param <Q3> the third qualifier reflection type.
 */
public interface Mover3<S, Q1, Q2, Q3> extends
    Guide3<S, S, Q1, Q2, Q3>,
    Mover<S>,
    Mover4<S, Q1, Q2, Q3, Void>,
    Mover5<S, Q1, Q2, Q3, Void, Void>
{
  default S move(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3) throws TraverseException {
    return traverse(source, qualifier1, qualifier2, qualifier3);
  }

  @Override
  default S move(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Void qualifier4) throws TraverseException {
    return traverse(source, qualifier1, qualifier2, qualifier3);
  }

  @Override
  default S move(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return traverse(source, qualifier1, qualifier2, qualifier3);
  }

  @SuppressWarnings("unchecked")
  default S traverse(S source, Object... qualifiers) throws TraverseException {
    return traverse(source, (Q1) qualifiers[0], (Q2) qualifiers[1], (Q3) qualifiers[2]);
  }

  @Override
  default S traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Void qualifier4) throws TraverseException {
    return traverse(source, qualifier1, qualifier2, qualifier3);
  }

  @Override
  default S traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return traverse(source, qualifier1, qualifier2, qualifier3);
  }
}
