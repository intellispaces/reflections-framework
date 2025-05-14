package tech.intellispaces.reflections.framework.guide.n4;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.guide.MapperOfMoving;
import tech.intellispaces.reflections.framework.guide.n5.MapperOfMoving5;

/**
 * Mapper related to moving guide with four qualifiers.
 *
 * @param <S> the source reflection type.
 * @param <T> the target reflection type.
 * @param <Q1> the first qualifier reflection type.
 * @param <Q2> the second qualifier reflection type.
 * @param <Q3> the third qualifier reflection type.
 * @param <Q4> the fourth qualifier reflection type.
 */
public interface MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> extends
    Guide4<S, T, Q1, Q2, Q3, Q4>,
    MapperOfMoving<S, T>,
    MapperOfMoving5<S, T, Q1, Q2, Q3, Q4, Void>
{
  default T map(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4) throws TraverseException {
    return traverse(source, qualifier1, qualifier2, qualifier3, qualifier4);
  }

  @Override
  default T map(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4, Void qualifier5) throws TraverseException {
    return traverse(source, qualifier1, qualifier2, qualifier3, qualifier4);
  }

  @Override
  @SuppressWarnings("unchecked")
  default T traverse(S source, Object... qualifiers) throws TraverseException {
    return traverse(source, (Q1) qualifiers[0], (Q2) qualifiers[1], (Q3) qualifiers[2], (Q4) qualifiers[3]);
  }

  @Override
  default T traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4, Void qualifier5) throws TraverseException {
    return traverse(source, qualifier1, qualifier2, qualifier3, qualifier4);
  }
}
