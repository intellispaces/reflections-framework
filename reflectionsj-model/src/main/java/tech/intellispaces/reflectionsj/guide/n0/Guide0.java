package tech.intellispaces.reflectionsj.guide.n0;

import tech.intellispaces.reflectionsj.exception.TraverseException;
import tech.intellispaces.reflectionsj.guide.Guide;

/**
 * Non-parameterized guide.
 *
 * @param <S> source handle type.
 * @param <R> result handle type.
 */
public interface Guide0<S, R> extends Guide<S, R> {

  R traverse(S source) throws TraverseException;

  int traverseToInt(S source) throws TraverseException;

  double traverseToDouble(S source) throws TraverseException;
}
