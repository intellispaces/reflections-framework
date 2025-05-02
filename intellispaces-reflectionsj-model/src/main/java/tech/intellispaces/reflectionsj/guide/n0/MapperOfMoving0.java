package tech.intellispaces.reflectionsj.guide.n0;

import java.util.function.BiFunction;
import java.util.function.Function;

import tech.intellispaces.reflectionsj.exception.TraverseException;
import tech.intellispaces.reflectionsj.guide.MapperOfMoving;
import tech.intellispaces.reflectionsj.guide.n1.MapperOfMoving1;
import tech.intellispaces.reflectionsj.guide.n2.MapperOfMoving2;
import tech.intellispaces.reflectionsj.guide.n3.MapperOfMoving3;
import tech.intellispaces.reflectionsj.guide.n4.MapperOfMoving4;
import tech.intellispaces.reflectionsj.guide.n5.MapperOfMoving5;

/**
 * Mapper related to moving guide without qualifiers.
 *
 * @param <S> source handle type.
 * @param <T> target handle type.
 */
public interface MapperOfMoving0<S, T> extends
    Guide0<S, T>,
    MapperOfMoving<S, T>,
    MapperOfMoving1<S, T, Void>,
    MapperOfMoving2<S, T, Void, Void>,
    MapperOfMoving3<S, T, Void, Void, Void>,
    MapperOfMoving4<S, T, Void, Void, Void, Void>,
    MapperOfMoving5<S, T, Void, Void, Void, Void, Void>
{
  Function<S, T> asFunction();

  @Override
  default BiFunction<S, Void, T> asBiFunction() {
    return (source, qualifier) -> map(source);
  }

  default T map(S source) throws TraverseException {
    return traverse(source);
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
  default T traverse(S source, Object... qualifiers) throws TraverseException {
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
