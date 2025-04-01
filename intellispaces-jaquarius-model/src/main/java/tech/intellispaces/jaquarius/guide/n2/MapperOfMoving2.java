package tech.intellispaces.jaquarius.guide.n2;

import tech.intellispaces.commons.function.Function3;
import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.guide.MapperOfMoving;
import tech.intellispaces.jaquarius.guide.n3.MapperOfMoving3;
import tech.intellispaces.jaquarius.guide.n4.MapperOfMoving4;
import tech.intellispaces.jaquarius.guide.n5.MapperOfMoving5;

/**
 * Mapper related to moving guide with two qualifiers.
 *
 * @param <S> source handle type.
 * @param <T> target handle type.
 * @param <Q1> first qualifier handle type.
 * @param <Q2> second qualifier handle type.
 */
public interface MapperOfMoving2<S, T, Q1, Q2> extends
    Guide2<S, T, Q1, Q2>,
    MapperOfMoving<S, T>,
    MapperOfMoving3<S, T, Q1, Q2, Void>,
    MapperOfMoving4<S, T, Q1, Q2, Void, Void>,
    MapperOfMoving5<S, T, Q1, Q2, Void, Void, Void>
{
  Function3<S, Q1, Q2, T> asFunction3();

  default T map(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException {
    return traverse(source, qualifier1, qualifier2);
  }

  @Override
  default T map(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3) throws TraverseException {
    return traverse(source, qualifier1, qualifier2);
  }

  @Override
  default T map(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return traverse(source, qualifier1, qualifier2);
  }

  @Override
  default T map(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return traverse(source, qualifier1, qualifier2);
  }

  @Override
  @SuppressWarnings("unchecked")
  default T traverse(S source, Object... qualifiers) throws TraverseException {
    return traverse(source, (Q1) qualifiers[0], (Q2) qualifiers[1]);
  }

  @Override
  default T traverse(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3) throws TraverseException {
    return traverse(source, qualifier1, qualifier2);
  }

  @Override
  default T traverse(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return traverse(source, qualifier1, qualifier2);
  }

  @Override
  default T traverse(S source, Q1 qualifier1, Q2 qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return traverse(source, qualifier1, qualifier2);
  }
}
