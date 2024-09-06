package intellispaces.framework.core.guide.n3;

import intellispaces.common.base.function.QuadFunction;
import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.Mapper;
import intellispaces.framework.core.guide.n4.Mapper4;
import intellispaces.framework.core.guide.n5.Mapper5;

/**
 * Mapper guide with three qualifiers.
 *
 * @param <S> source object type.
 * @param <T> target object type.
 * @param <Q1> first qualifier type.
 * @param <Q2> second qualifier type.
 * @param <Q3> third qualifier type.
 */
public interface Mapper3<S, T, Q1, Q2, Q3> extends
    Guide3<S, T, Q1, Q2, Q3>,
    Mapper<S, T>,
    Mapper4<S, T, Q1, Q2, Q3, Void>,
    Mapper5<S, T, Q1, Q2, Q3, Void, Void>
{
  T map(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3) throws TraverseException;

  QuadFunction<S, Q1, Q2, Q3, T> asQuadFunction();

  @Override
  default T map(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Void qualifier4) throws TraverseException {
    return map(source, qualifier1, qualifier2, qualifier3);
  }

  @Override
  default T map(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return map(source, qualifier1, qualifier2, qualifier3);
  }

  @Override
  default T traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3) throws TraverseException {
    return map(source, qualifier1, qualifier2, qualifier3);
  }

  @Override
  @SuppressWarnings("unchecked")
  default T traverse(S source, Object... qualifiers) throws TraverseException {
    return map(source, (Q1) qualifiers[0], (Q2) qualifiers[1], (Q3) qualifiers[2]);
  }
}
