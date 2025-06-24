package tech.intellispaces.reflections.framework.guide.n4;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.guide.Mover;
import tech.intellispaces.reflections.framework.guide.n5.Mover5;

/**
 * Mover guide with four qualifiers.
 *
 * @param <S> the source reflection type.
 * @param <Q1> the first qualifier reflection type.
 * @param <Q2> the second qualifier reflection type.
 * @param <Q3> the third qualifier reflection type.
 * @param <Q4> the fourth qualifier reflection type.
 */
public interface Mover4<S, Q1, Q2, Q3, Q4> extends
    SystemGuide4<S, S, Q1, Q2, Q3, Q4>,
    Mover<S>,
    Mover5<S, Q1, Q2, Q3, Q4, Void>
{
  default S move(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4) throws TraverseException {
    return traverse(source, qualifier1, qualifier2, qualifier3, qualifier4);
  }

  @Override
  default S move(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4, Void qualifier5) throws TraverseException {
    return traverse(source, qualifier1, qualifier2, qualifier3, qualifier4);
  }

  @SuppressWarnings("unchecked")
  default S traverse(S source, Object[] qualifiers) throws TraverseException {
    return traverse(source, (Q1) qualifiers[0], (Q2) qualifiers[1], (Q3) qualifiers[2], (Q4) qualifiers[3]);
  }

  @Override
  default S traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4, Void qualifier5) throws TraverseException {
    return traverse(source, qualifier1, qualifier2, qualifier3, qualifier4);
  }
}
