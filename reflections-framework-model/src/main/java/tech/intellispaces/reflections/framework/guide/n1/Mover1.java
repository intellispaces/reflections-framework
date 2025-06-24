package tech.intellispaces.reflections.framework.guide.n1;

import java.util.function.BiConsumer;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.guide.Mover;
import tech.intellispaces.reflections.framework.guide.n2.Mover2;
import tech.intellispaces.reflections.framework.guide.n3.Mover3;
import tech.intellispaces.reflections.framework.guide.n4.Mover4;
import tech.intellispaces.reflections.framework.guide.n5.Mover5;

/**
 * Mover guide with one qualifier.
 *
 * @param <S> the source reflection type.
 * @param <Q> the qualifier reflection type.
 */
public interface Mover1<S, Q> extends
    SystemGuide1<S, S, Q>,
    Mover<S>,
    Mover2<S, Q, Void>,
    Mover3<S, Q, Void, Void>,
    Mover4<S, Q, Void, Void, Void>,
    Mover5<S, Q, Void, Void, Void, Void>
{
  BiConsumer<S, Q> asBiConsumer();

  default S move(S source, Q qualifier) throws TraverseException {
    return traverse(source, qualifier);
  }

  @Override
  default S move(S source, Q qualifier1, Void qualifier2) throws TraverseException {
    return traverse(source, qualifier1);
  }

  @Override
  default S move(S source, Q qualifier1, Void qualifier2, Void qualifier3) throws TraverseException {
    return traverse(source, qualifier1);
  }

  @Override
  default S move(S source, Q qualifier1, Void qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return traverse(source, qualifier1);
  }

  @Override
  default S move(S source, Q qualifier1, Void qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return traverse(source, qualifier1);
  }

  @SuppressWarnings("unchecked")
  default S traverse(S source, Object[] qualifiers) throws TraverseException {
    return traverse(source, (Q) qualifiers[0]);
  }

  @Override
  default S traverse(S source, Q qualifier1, Void qualifier2) throws TraverseException {
    return traverse(source, qualifier1);
  }

  @Override
  default S traverse(S source, Q qualifier1, Void qualifier2, Void qualifier3) throws TraverseException {
    return traverse(source, qualifier1);
  }

  @Override
  default S traverse(S source, Q qualifier1, Void qualifier2, Void qualifier3, Void qualifier4) throws TraverseException {
    return traverse(source, qualifier1);
  }

  @Override
  default S traverse(S source, Q qualifier1, Void qualifier2, Void qualifier3, Void qualifier4, Void qualifier5) throws TraverseException {
    return traverse(source, qualifier1);
  }
}
