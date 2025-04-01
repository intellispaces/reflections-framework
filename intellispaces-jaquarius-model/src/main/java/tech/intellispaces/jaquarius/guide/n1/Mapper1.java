package tech.intellispaces.jaquarius.guide.n1;

import tech.intellispaces.commons.function.Function3;
import tech.intellispaces.commons.function.Function4;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.guide.Mapper;
import tech.intellispaces.jaquarius.guide.n2.Mapper2;
import tech.intellispaces.jaquarius.guide.n3.Mapper3;
import tech.intellispaces.jaquarius.guide.n4.Mapper4;
import tech.intellispaces.jaquarius.guide.n5.Mapper5;

import java.util.function.BiFunction;

/**
 * Mapper guide with one qualifier.
 *
 * @param <S> source handle type.
 * @param <T> target handle type.
 * @param <Q> qualifier handle type.
 */
public interface Mapper1<S, T, Q> extends
    Guide1<S, T, Q>,
    Mapper<S, T>,
    Mapper2<S, T, Q, Void>,
    Mapper3<S, T, Q, Void, Void>,
    Mapper4<S, T, Q, Void, Void, Void>,
    Mapper5<S, T, Q, Void, Void, Void, Void>
{
  BiFunction<S, Q, T> asBiFunction();

  @Override
  default Function3<S, Q, Void, T> asFunction3() {
    return (source, qualifier1, qualifier2) -> map(source, qualifier1);
  }

  @Override
  default Function4<S, Q, Void, Void, T> asFunction4() {
    return (source, qualifier1, qualifier2, qualifier3) -> map(source, qualifier1);
  }

  default T map(S source, Q qualifier) throws TraverseException {
    return traverse(source, qualifier);
  }

  @Override
  default T map(S source, Q qualifier1, Void qualifier2) throws TraverseException {
    return traverse(source, qualifier1);
  }

  @Override
  default T map(S source, Q qualifier1, Void qualifier2, Void qualifier3) throws TraverseException {
    return traverse(source, qualifier1);
  }

  @Override
  default T map(S source, Q qualifier1, Void qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return traverse(source, qualifier1);
  }

  @Override
  default T map(S source, Q qualifier1, Void qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return traverse(source, qualifier1);
  }

  @Override
  @SuppressWarnings("unchecked")
  default T traverse(S source, Object... qualifiers) throws TraverseException {
    return traverse(source, (Q) qualifiers[0]);
  }

  @Override
  default T traverse(S source, Q qualifier1, Void qualifier2) throws TraverseException {
    return traverse(source, qualifier1);
  }

  @Override
  default T traverse(S source, Q qualifier1, Void qualifier2, Void qualifier3) throws TraverseException {
    return traverse(source, qualifier1);
  }

  @Override
  default T traverse(S source, Q qualifier1, Void qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return traverse(source, qualifier1);
  }

  @Override
  default T traverse(S source, Q qualifier1, Void qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return traverse(source, qualifier1);
  }
}
