package tech.intellispaces.reflections.framework.guide.n5;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.guide.Mapper;

/**
 * Mapper guide with five qualifiers.
 *
 * @param <S> the source reflection type.
 * @param <T> the target reflection type.
 * @param <Q1> the first qualifier reflection type.
 * @param <Q2> the second qualifier reflection type.
 * @param <Q3> the third qualifier reflection type.
 * @param <Q4> the fourth qualifier reflection type.
 * @param <Q5> the fifth qualifier reflection type.
 */
public interface Mapper5<S, T, Q1, Q2, Q3, Q4, Q5> extends
    SystemGuide5<S, T, Q1, Q2, Q3, Q4, Q5>,
    Mapper<S, T>
{
  default T map(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4, Q5 qualifier5) throws TraverseException {
    return traverse(source, qualifier1, qualifier2, qualifier3, qualifier4, qualifier5);
  }

  @Override
  @SuppressWarnings("unchecked")
  default T traverse(S source, Object[] qualifiers) throws TraverseException {
    return traverse(source, (Q1) qualifiers[0], (Q2) qualifiers[1], (Q3) qualifiers[2], (Q4) qualifiers[3], (Q5) qualifiers[4]);
  }
}
