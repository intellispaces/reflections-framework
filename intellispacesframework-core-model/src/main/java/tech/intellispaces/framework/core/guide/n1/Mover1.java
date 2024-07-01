package tech.intellispaces.framework.core.guide.n1;

import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.Mover;
import tech.intellispaces.framework.core.guide.n2.Mover2;
import tech.intellispaces.framework.core.guide.n3.Mover3;
import tech.intellispaces.framework.core.guide.n4.Mover4;
import tech.intellispaces.framework.core.guide.n5.Mover5;

import java.util.function.BiConsumer;

/**
 * Mover guide with one qualifier.
 *
 * @param <S> source object type.
 * @param <B> backward object handle type.
 * @param <Q> qualifier type.
 */
public interface Mover1<S, B, Q> extends
    Guide1<S, B, Q>,
    Mover<S, B>,
    Mover2<S, B, Q, Void>,
    Mover3<S, B, Q, Void, Void>,
    Mover4<S, B, Q, Void, Void, Void>,
    Mover5<S, B, Q, Void, Void, Void, Void>
{
  B move(S source, Q qualifier) throws TraverseException;

  BiConsumer<S, Q> asBiConsumer();

  @Override
  default B move(S source, Q qualifier1, Void qualifier2) throws TraverseException {
    return move(source, qualifier1);
  }

  @Override
  default B move(S source, Q qualifier1, Void qualifier2, Void qualifier3) throws TraverseException {
    return move(source, qualifier1);
  }

  @Override
  default B move(S source, Q qualifier1, Void qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return move(source, qualifier1);
  }

  @Override
  default B move(S source, Q qualifier1, Void qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return move(source, qualifier1);
  }

  @Override
  default B traverse(S source, Q qualifier) throws TraverseException {
    return move(source, qualifier);
  }

  @SuppressWarnings("unchecked")
  default B traverse(S source, Object... qualifiers) throws TraverseException {
    return move(source, (Q) qualifiers[0]);
  }
}
