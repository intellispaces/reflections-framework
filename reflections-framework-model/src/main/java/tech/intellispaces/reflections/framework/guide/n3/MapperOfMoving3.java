package tech.intellispaces.reflections.framework.guide.n3;

import tech.intellispaces.commons.function.Function4;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.guide.MapperOfMoving;
import tech.intellispaces.reflections.framework.guide.n4.MapperOfMoving4;
import tech.intellispaces.reflections.framework.guide.n5.MapperOfMoving5;

/**
 * Mapper related to moving guide with three qualifiers.
 *
 * @param <S> the source reflection type.
 * @param <T> the target reflection type.
 * @param <Q1> the first qualifier reflection type.
 * @param <Q2> the second qualifier reflection type.
 * @param <Q3> the third qualifier reflection type.
 */
public interface MapperOfMoving3<S, T, Q1, Q2, Q3> extends
    SystemGuide3<S, T, Q1, Q2, Q3>,
    MapperOfMoving<S, T>,
    MapperOfMoving4<S, T, Q1, Q2, Q3, Void>,
    MapperOfMoving5<S, T, Q1, Q2, Q3, Void, Void>
{
  Function4<S, Q1, Q2, Q3, T> asQuadFunction();

  default T map(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3) throws TraverseException {
    return traverse(source, qualifier1, qualifier2, qualifier3);
  }

  @Override
  default T map(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Void qualifier4) throws TraverseException {
    return traverse(source, qualifier1, qualifier2, qualifier3);
  }

  @Override
  default T map(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return traverse(source, qualifier1, qualifier2, qualifier3);
  }

  @Override
  @SuppressWarnings("unchecked")
  default T traverse(S source, Object[] qualifiers) throws TraverseException {
    return traverse(source, (Q1) qualifiers[0], (Q2) qualifiers[1], (Q3) qualifiers[2]);
  }

  @Override
  default T traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Void qualifier4) throws TraverseException {
    return traverse(source, qualifier1, qualifier2, qualifier3);
  }

  @Override
  default T traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return traverse(source, qualifier1, qualifier2, qualifier3);
  }
}
