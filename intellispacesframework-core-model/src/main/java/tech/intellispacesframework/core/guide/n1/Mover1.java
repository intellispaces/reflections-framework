package tech.intellispacesframework.core.guide.n1;

import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.guide.Mover;
import tech.intellispacesframework.core.guide.n2.Mover2;
import tech.intellispacesframework.core.guide.n3.Mover3;
import tech.intellispacesframework.core.guide.n4.Mover4;
import tech.intellispacesframework.core.guide.n5.Mover5;

import java.util.function.BiConsumer;

/**
 * Mover guide with one qualifier.
 *
 * @param <S> source object type.
 * @param <Q> qualifier type.
 */
public interface Mover1<S, Q> extends
    Guide1<S, S, Q>,
    Mover<S>,
    Mover2<S, Q, Void>,
    Mover3<S, Q, Void, Void>,
    Mover4<S, Q, Void, Void, Void>,
    Mover5<S, Q, Void, Void, Void, Void>
{
  S move(S source, Q qualifier) throws TraverseException;

  BiConsumer<S, Q> asBiConsumer();

  @Override
  default S move(S source, Q qualifier1, Void qualifier2) throws TraverseException {
    return move(source, qualifier1);
  }

  @Override
  default S move(S source, Q qualifier1, Void qualifier2, Void qualifier3) throws TraverseException {
    return move(source, qualifier1);
  }

  @Override
  default S move(S source, Q qualifier1, Void qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return move(source, qualifier1);
  }

  @Override
  default S move(S source, Q qualifier1, Void qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return move(source, qualifier1);
  }

  @Override
  default S traverse(S source, Q qualifier) throws TraverseException {
    return move(source, qualifier);
  }

  @SuppressWarnings("unchecked")
  default S traverse(S source, Object... qualifiers) throws TraverseException {
    return move(source, (Q) qualifiers[0]);
  }
}
