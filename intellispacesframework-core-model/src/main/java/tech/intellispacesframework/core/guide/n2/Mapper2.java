package tech.intellispacesframework.core.guide.n2;

import tech.intellispacesframework.commons.function.TriFunction;
import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.guide.Mapper;
import tech.intellispacesframework.core.guide.n3.Mapper3;
import tech.intellispacesframework.core.guide.n4.Mapper4;
import tech.intellispacesframework.core.guide.n5.Mapper5;

/**
 * Mapper guide with two qualifiers.
 *
 * @param <S> source object type.
 * @param <T> target object type.
 * @param <Q1> first qualifier type.
 * @param <Q2> second qualifier type.
 */
public interface Mapper2<S, T, Q1, Q2> extends
    Guide2<S, T, Q1, Q2>,
    Mapper<S, T>,
    Mapper3<S, T, Q1, Q2, Void>,
    Mapper4<S, T, Q1, Q2, Void, Void>,
    Mapper5<S, T, Q1, Q2, Void, Void, Void>
{
  T map(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException;

  TriFunction<S, Q1, Q2, T> asTriFunction();

  @Override
  default T map(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3) throws TraverseException {
    return map(source, qualifier1, qualifier2);
  }

  @Override
  default T map(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return map(source, qualifier1, qualifier2);
  }

  @Override
  default T map(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return map(source, qualifier1, qualifier2);
  }

  @Override
  default T traverse(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException {
    return map(source, qualifier1, qualifier2);
  }

  @Override
  @SuppressWarnings("unchecked")
  default T traverse(S source, Object... qualifiers) throws TraverseException {
    return map(source, (Q1) qualifiers[0], (Q2) qualifiers[1]);
  }
}
