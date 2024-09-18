package intellispaces.framework.core.guide.n0;

import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.Mover;
import intellispaces.framework.core.guide.n1.Mover1;
import intellispaces.framework.core.guide.n2.Mover2;
import intellispaces.framework.core.guide.n3.Mover3;
import intellispaces.framework.core.guide.n4.Mover4;
import intellispaces.framework.core.guide.n5.Mover5;

import java.util.function.Consumer;

/**
 * Mover guide without qualifiers.
 *
 * @param <S> source object type.
 * @param <R> result object handle type.
 */
public interface Mover0<S, R> extends
    Guide0<S, R>,
    Mover<S, R>,
    Mover1<S, R, Void>,
    Mover2<S, R, Void, Void>,
    Mover3<S, R, Void, Void, Void>,
    Mover4<S, R, Void, Void, Void, Void>,
    Mover5<S, R, Void, Void, Void, Void, Void>
{
  R move(S source) throws TraverseException;

  Consumer<S> asConsumer();

  @Override
  default R move(S source, Void qualifier) throws TraverseException {
    return move(source);
  }

  @Override
  default R move(S source, Void qualifier1, Void qualifier2) throws TraverseException {
    return move(source);
  }

  @Override
  default R move(S source, Void qualifier1, Void qualifier2, Void qualifier3) throws TraverseException {
    return move(source);
  }

  @Override
  default R move(S source, Void qualifier1, Void qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return move(source);
  }

  @Override
  default R move(S source, Void qualifier1, Void qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return move(source);
  }

  @Override
  default R traverse(S source) throws TraverseException {
    return move(source);
  }

  @Override
  default R traverse(S source, Object... qualifiers) throws TraverseException {
    return move(source);
  }
}
