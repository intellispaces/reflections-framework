package tech.intellispaces.reflectionsj.guide.n1;

import tech.intellispaces.reflectionsj.exception.TraverseException;
import tech.intellispaces.reflectionsj.guide.Guide;

/**
 * Guide with one qualifier.
 *
 * @param <S> source handle type.
 * @param <R> result handle type.
 * @param <Q> qualifier handle type.
 */
public interface Guide1<S, R, Q> extends Guide<S, R> {

  R traverse(S source, Q qualifier) throws TraverseException;
}
