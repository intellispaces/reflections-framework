package tech.intellispaces.reflections.framework.guide.n0;

import java.util.function.Consumer;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.guide.Mover;
import tech.intellispaces.reflections.framework.guide.n1.Mover1;
import tech.intellispaces.reflections.framework.guide.n2.Mover2;
import tech.intellispaces.reflections.framework.guide.n3.Mover3;
import tech.intellispaces.reflections.framework.guide.n4.Mover4;
import tech.intellispaces.reflections.framework.guide.n5.Mover5;

/**
 * Mover guide without qualifiers.
 *
 * @param <S> the source reflection type.
 */
public interface Mover0<S> extends
    SystemGuide0<S, S>,
    Mover<S>,
    Mover1<S, Void>,
    Mover2<S, Void, Void>,
    Mover3<S, Void, Void, Void>,
    Mover4<S, Void, Void, Void, Void>,
    Mover5<S, Void, Void, Void, Void, Void>
{
  Consumer<S> asConsumer();

  default S move(S source) throws TraverseException {
    return traverse(source);
  }

  @Override
  default S move(S source, Void qualifier) throws TraverseException {
    return traverse(source);
  }

  @Override
  default S move(S source, Void qualifier1, Void qualifier2) throws TraverseException {
    return traverse(source);
  }

  @Override
  default S move(S source, Void qualifier1, Void qualifier2, Void qualifier3) throws TraverseException {
    return traverse(source);
  }

  @Override
  default S move(S source, Void qualifier1, Void qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return traverse(source);
  }

  @Override
  default S move(S source, Void qualifier1, Void qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return traverse(source);
  }

  @Override
  default S traverse(S source, Object[] qualifiers) throws TraverseException {
    return traverse(source);
  }

  @Override
  default S traverse(S source, Void qualifier) throws TraverseException {
    return traverse(source);
  }

  @Override
  default S traverse(S source, Void qualifier1, Void qualifier2) throws TraverseException {
    return traverse(source);
  }

  @Override
  default S traverse(S source, Void qualifier1, Void qualifier2, Void qualifier3) throws TraverseException {
    return traverse(source);
  }

  @Override
  default S traverse(S source, Void qualifier1, Void qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return traverse(source);
  }

  @Override
  default S traverse(S source, Void qualifier1, Void qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return traverse(source);
  }
}
