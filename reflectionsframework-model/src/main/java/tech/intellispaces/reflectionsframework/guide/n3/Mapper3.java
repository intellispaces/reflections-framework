package tech.intellispaces.reflectionsframework.guide.n3;

import tech.intellispaces.commons.function.Function4;
import tech.intellispaces.reflectionsframework.exception.TraverseException;
import tech.intellispaces.reflectionsframework.guide.Mapper;
import tech.intellispaces.reflectionsframework.guide.n4.Mapper4;
import tech.intellispaces.reflectionsframework.guide.n5.Mapper5;

/**
 * Mapper guide with three qualifiers.
 *
 * @param <S> source handle type.
 * @param <T> target handle type.
 * @param <Q1> first qualifier handle type.
 * @param <Q2> second qualifier handle type.
 * @param <Q3> third qualifier handle type.
 */
public interface Mapper3<S, T, Q1, Q2, Q3> extends
    Guide3<S, T, Q1, Q2, Q3>,
    Mapper<S, T>,
    Mapper4<S, T, Q1, Q2, Q3, Void>,
    Mapper5<S, T, Q1, Q2, Q3, Void, Void>
{
  Function4<S, Q1, Q2, Q3, T> asFunction4();

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
  default T traverse(S source, Object... qualifiers) throws TraverseException {
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
