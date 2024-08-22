package intellispaces.core.guide.n1;

import intellispaces.commons.function.QuadFunction;
import intellispaces.commons.function.TriFunction;
import intellispaces.core.exception.TraverseException;
import intellispaces.core.guide.Mapper;
import intellispaces.core.guide.n2.Mapper2;
import intellispaces.core.guide.n3.Mapper3;
import intellispaces.core.guide.n4.Mapper4;
import intellispaces.core.guide.n5.Mapper5;

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
  default TriFunction<S, Q, Void, T> asTriFunction() {
    return (source, qualifier1, qualifier2) -> map(source, qualifier1);
  }

  @Override
  default QuadFunction<S, Q, Void, Void, T> asQuadFunction() {
    return (source, qualifier1, qualifier2, qualifier3) -> map(source, qualifier1);
  }

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
