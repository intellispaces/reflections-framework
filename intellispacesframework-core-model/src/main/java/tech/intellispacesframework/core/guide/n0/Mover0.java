package tech.intellispacesframework.core.guide.n0;

import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.guide.Mover;
import tech.intellispacesframework.core.guide.n1.Mover1;
import tech.intellispacesframework.core.guide.n2.Mover2;
import tech.intellispacesframework.core.guide.n3.Mover3;
import tech.intellispacesframework.core.guide.n4.Mover4;
import tech.intellispacesframework.core.guide.n5.Mover5;

import java.util.function.Consumer;

/**
 * Mover guide without qualifiers.
 *
 * @param <S> source object type.
 */
public interface Mover0<S> extends
    Guide0<S, S>,
    Mover<S>,
    Mover1<S, Void>,
    Mover2<S, Void, Void>,
    Mover3<S, Void, Void, Void>,
    Mover4<S, Void, Void, Void, Void>,
    Mover5<S, Void, Void, Void, Void, Void>
{
  S move(S source) throws TraverseException;

  Consumer<S> asConsumer();

  @Override
  default S move(S source, Void qualifier) throws TraverseException {
    return move(source);
  }

  @Override
  default S move(S source, Void qualifier1, Void qualifier2) throws TraverseException {
    return move(source);
  }

  @Override
  default S move(S source, Void qualifier1, Void qualifier2, Void qualifier3) throws TraverseException {
    return move(source);
  }

  @Override
  default S move(S source, Void qualifier1, Void qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return move(source);
  }

  @Override
  default S move(S source, Void qualifier1, Void qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return move(source);
  }

  @Override
  default S sync(S source) throws TraverseException {
    return move(source);
  }

  @Override
  default S sync(S source, Object... qualifiers) throws TraverseException {
    return move(source);
  }
}
