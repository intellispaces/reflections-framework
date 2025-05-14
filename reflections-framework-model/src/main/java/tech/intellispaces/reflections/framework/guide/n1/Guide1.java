package tech.intellispaces.reflections.framework.guide.n1;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.guide.Guide;

/**
 * Guide with one qualifier.
 *
 * @param <S> the source reflection type.
 * @param <R> the result reflection type.
 * @param <Q> the qualifier reflection type.
 */
public interface Guide1<S, R, Q> extends Guide<S, R> {

  R traverse(S source, Q qualifier) throws TraverseException;
}
