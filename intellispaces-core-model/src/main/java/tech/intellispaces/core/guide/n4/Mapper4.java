package tech.intellispaces.core.guide.n4;

import tech.intellispaces.core.exception.TraverseException;
import tech.intellispaces.core.guide.Mapper;
import tech.intellispaces.core.guide.n5.Mapper5;

/**
 * Mapper guide with four qualifiers.
 *
 * @param <S> source object type.
 * @param <T> target object type.
 * @param <Q1> first qualifier type.
 * @param <Q2> second qualifier type.
 * @param <Q3> third qualifier type.
 * @param <Q4> fourth qualifier type.
 */
public interface Mapper4<S, T, Q1, Q2, Q3, Q4> extends
    Guide4<S, T, Q1, Q2, Q3, Q4>,
    Mapper<S, T>,
    Mapper5<S, T, Q1, Q2, Q3, Q4, Void>
{
  T map(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4) throws TraverseException;

  @Override
  default T map(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4, Void qualifier5) throws TraverseException {
    return map(source, qualifier1, qualifier2, qualifier3, qualifier4);
  }

  @Override
  default T traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4) throws TraverseException {
    return map(source, qualifier1, qualifier2, qualifier3, qualifier4);
  }

  @Override
  @SuppressWarnings("unchecked")
  default T traverse(S source, Object... qualifiers) throws TraverseException {
    return map(source, (Q1) qualifiers[0], (Q2) qualifiers[1], (Q3) qualifiers[2], (Q4) qualifiers[3]);
  }
}
