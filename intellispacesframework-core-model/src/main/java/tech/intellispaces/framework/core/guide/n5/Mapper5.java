package tech.intellispaces.framework.core.guide.n5;

import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.Mapper;

/**
 * Mapper guide with five qualifiers.
 *
 * @param <S> source object type.
 * @param <T> target object type.
 * @param <Q1> first qualifier type.
 * @param <Q2> second qualifier type.
 * @param <Q3> third qualifier type.
 * @param <Q4> fourth qualifier type.
 * @param <Q5> fifth qualifier type.
 */
public interface Mapper5<S, T, Q1, Q2, Q3, Q4, Q5> extends
    Guide5<S, T, Q1, Q2, Q3, Q4, Q5>,
    Mapper<S, T>
{
  T map(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4, Q5 qualifier5) throws TraverseException;

  @Override
  default T traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4, Q5 qualifier5) throws TraverseException {
    return map(source, qualifier1, qualifier2, qualifier3, qualifier4, qualifier5);
  }

  @Override
  @SuppressWarnings("unchecked")
  default T traverse(S source, Object... qualifiers) throws TraverseException {
    return map(source, (Q1) qualifiers[0], (Q2) qualifiers[1], (Q3) qualifiers[2], (Q4) qualifiers[3], (Q5) qualifiers[4]);
  }
}
