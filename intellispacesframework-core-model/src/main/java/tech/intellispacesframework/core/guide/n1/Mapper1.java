package tech.intellispacesframework.core.guide.n1;

import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.guide.Mapper;
import tech.intellispacesframework.core.guide.n2.Mapper2;
import tech.intellispacesframework.core.guide.n3.Mapper3;
import tech.intellispacesframework.core.guide.n4.Mapper4;
import tech.intellispacesframework.core.guide.n5.Mapper5;

import java.util.function.BiFunction;

/**
 * Mapper guide with one qualifier.
 *
 * @param <S> source object type.
 * @param <T> target object type.
 * @param <Q> qualifier type.
 */
public interface Mapper1<S, T, Q> extends
    Guide1<S, T, Q>,
    Mapper<S, T>,
    Mapper2<S, T, Q, Void>,
    Mapper3<S, T, Q, Void, Void>,
    Mapper4<S, T, Q, Void, Void, Void>,
    Mapper5<S, T, Q, Void, Void, Void, Void>
{
  T map(S source, Q qualifier) throws TraverseException;

  BiFunction<S, Q, T> asBiFunction();

  @Override
  default T map(S source, Q qualifier1, Void qualifier2) throws TraverseException {
    return map(source, qualifier1);
  }

  @Override
  default T map(S source, Q qualifier1, Void qualifier2, Void qualifier3) throws TraverseException {
    return map(source, qualifier1);
  }

  @Override
  default T map(S source, Q qualifier1, Void qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return map(source, qualifier1);
  }

  @Override
  default T map(S source, Q qualifier1, Void qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return map(source, qualifier1);
  }

  @Override
  default T traverse(S source, Q qualifier) throws TraverseException {
    return map(source, qualifier);
  }

  @Override
  @SuppressWarnings("unchecked")
  default T traverse(S source, Object... qualifiers) throws TraverseException {
    return map(source, (Q) qualifiers[0]);
  }
}
