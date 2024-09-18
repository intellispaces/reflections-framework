package intellispaces.framework.core.guide.n1;

import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.Mover;
import intellispaces.framework.core.guide.n2.Mover2;
import intellispaces.framework.core.guide.n3.Mover3;
import intellispaces.framework.core.guide.n4.Mover4;
import intellispaces.framework.core.guide.n5.Mover5;

import java.util.function.BiConsumer;

/**
 * Mover guide with one qualifier.
 *
 * @param <S> source object type.
 * @param <R> result object handle type.
 * @param <Q> qualifier type.
 */
public interface Mover1<S, R, Q> extends
    Guide1<S, R, Q>,
    Mover<S, R>,
    Mover2<S, R, Q, Void>,
    Mover3<S, R, Q, Void, Void>,
    Mover4<S, R, Q, Void, Void, Void>,
    Mover5<S, R, Q, Void, Void, Void, Void>
{
  R move(S source, Q qualifier) throws TraverseException;

  BiConsumer<S, Q> asBiConsumer();

  @Override
  default R move(S source, Q qualifier1, Void qualifier2) throws TraverseException {
    return move(source, qualifier1);
  }

  @Override
  default R move(S source, Q qualifier1, Void qualifier2, Void qualifier3) throws TraverseException {
    return move(source, qualifier1);
  }

  @Override
  default R move(S source, Q qualifier1, Void qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return move(source, qualifier1);
  }

  @Override
  default R move(S source, Q qualifier1, Void qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return move(source, qualifier1);
  }

  @Override
  default R traverse(S source, Q qualifier) throws TraverseException {
    return move(source, qualifier);
  }

  @SuppressWarnings("unchecked")
  default R traverse(S source, Object... qualifiers) throws TraverseException {
    return move(source, (Q) qualifiers[0]);
  }
}
