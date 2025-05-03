package tech.intellispaces.reflectionsj.guide.n5;

import tech.intellispaces.reflectionsj.exception.TraverseException;
import tech.intellispaces.reflectionsj.guide.Mapper;

/**
 * Mapper guide with five qualifiers.
 *
 * @param <S> source handle type.
 * @param <T> target handle type.
 * @param <Q1> first qualifier handle type.
 * @param <Q2> second qualifier handle type.
 * @param <Q3> third qualifier handle type.
 * @param <Q4> fourth qualifier handle type.
 * @param <Q5> fifth qualifier handle type.
 */
public interface Mapper5<S, T, Q1, Q2, Q3, Q4, Q5> extends
    Guide5<S, T, Q1, Q2, Q3, Q4, Q5>,
    Mapper<S, T>
{
  default T map(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4, Q5 qualifier5) throws TraverseException {
    return traverse(source, qualifier1, qualifier2, qualifier3, qualifier4, qualifier5);
  }

  @Override
  @SuppressWarnings("unchecked")
  default T traverse(S source, Object... qualifiers) throws TraverseException {
    return traverse(source, (Q1) qualifiers[0], (Q2) qualifiers[1], (Q3) qualifiers[2], (Q4) qualifiers[3], (Q5) qualifiers[4]);
  }
}
