package tech.intellispaces.reflections.framework.guide.n0;

import java.util.function.BiFunction;
import java.util.function.Function;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.guide.Mapper;
import tech.intellispaces.reflections.framework.guide.n1.Mapper1;
import tech.intellispaces.reflections.framework.guide.n2.Mapper2;
import tech.intellispaces.reflections.framework.guide.n3.Mapper3;
import tech.intellispaces.reflections.framework.guide.n4.Mapper4;
import tech.intellispaces.reflections.framework.guide.n5.Mapper5;

/**
 * Mapper guide without qualifiers.
 *
 * @param <S> the source reflection type.
 * @param <T> the target reflection type.
 */
public interface Mapper0<S, T> extends
    SystemGuide0<S, T>,
    Mapper<S, T>,
    Mapper1<S, T, Void>,
    Mapper2<S, T, Void, Void>,
    Mapper3<S, T, Void, Void, Void>,
    Mapper4<S, T, Void, Void, Void, Void>,
    Mapper5<S, T, Void, Void, Void, Void, Void>
{
  Function<S, T> asFunction();

  @Override
  default BiFunction<S, Void, T> asBiFunction() {
    return (source, qualifier) -> map(source);
  }

  default T map(S source) throws TraverseException {
    return traverse(source);
  }

  default int mapToInt(S source) throws TraverseException {
    return traverseToInt(source);
  }

  default double mapToDouble(S source) throws TraverseException {
    return traverseToDouble(source);
  }

  @Override
  default T map(S source, Void qualifier) throws TraverseException {
    return traverse(source);
  }

  @Override
  default T map(S source, Void qualifier1, Void qualifier2) throws TraverseException {
    return traverse(source);
  }

  @Override
  default T map(S source, Void qualifier1, Void qualifier2, Void qualifier3) throws TraverseException {
    return traverse(source);
  }

  @Override
  default T map(S source, Void qualifier1, Void qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return traverse(source);
  }

  @Override
  default T map(S source, Void qualifier1, Void qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return traverse(source);
  }

  @Override
  default T traverse(S source, Object[] qualifiers) throws TraverseException {
    return traverse(source);
  }

  @Override
  default T traverse(S source, Void qualifier) throws TraverseException {
    return traverse(source);
  }

  @Override
  default T traverse(S source, Void qualifier1, Void qualifier2) throws TraverseException {
    return traverse(source);
  }

  @Override
  default T traverse(S source, Void qualifier1, Void qualifier2, Void qualifier3) throws TraverseException {
    return traverse(source);
  }

  @Override
  default T traverse(S source, Void qualifier1, Void qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return traverse(source);
  }

  @Override
  default T traverse(S source, Void qualifier1, Void qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return traverse(source);
  }
}
