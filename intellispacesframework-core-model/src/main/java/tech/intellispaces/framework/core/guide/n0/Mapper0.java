package tech.intellispaces.framework.core.guide.n0;

import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.n1.Mapper1;
import tech.intellispaces.framework.core.guide.n2.Mapper2;
import tech.intellispaces.framework.core.guide.n3.Mapper3;
import tech.intellispaces.framework.core.guide.n4.Mapper4;
import tech.intellispaces.framework.core.guide.n5.Mapper5;
import tech.intellispaces.framework.core.guide.Mapper;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Mapper guide without qualifiers.
 *
 * @param <S> source object type.
 * @param <T> target object type.
 */
public interface Mapper0<S, T> extends
    Guide0<S, T>,
    Mapper<S, T>,
    Mapper1<S, T, Void>,
    Mapper2<S, T, Void, Void>,
    Mapper3<S, T, Void, Void, Void>,
    Mapper4<S, T, Void, Void, Void, Void>,
    Mapper5<S, T, Void, Void, Void, Void, Void>
{
  T map(S source) throws TraverseException;

  Function<S, T> asFunction();

  @Override
  default BiFunction<S, Void, T> asBiFunction() {
    return (source, qualifier) -> map(source);
  }

  @Override
  default T map(S source, Void qualifier) throws TraverseException {
    return map(source);
  }

  @Override
  default T map(S source, Void qualifier1, Void qualifier2) throws TraverseException {
    return map(source);
  }

  @Override
  default T map(S source, Void qualifier1, Void qualifier2, Void qualifier3) throws TraverseException {
    return map(source);
  }

  @Override
  default T map(S source, Void qualifier1, Void qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return map(source);
  }

  @Override
  default T map(S source, Void qualifier1, Void qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return map(source);
  }

  @Override
  default T traverse(S source) throws TraverseException {
    return map(source);
  }

  @Override
  default T traverse(S source, Object... qualifiers) throws TraverseException {
    return map(source);
  }
}
