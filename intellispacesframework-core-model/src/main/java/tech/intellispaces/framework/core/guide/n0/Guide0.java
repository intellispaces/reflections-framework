package tech.intellispaces.framework.core.guide.n0;

import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.Guide;

public interface Guide0<S, T> extends Guide<S, T> {

  T traverse(S source) throws TraverseException;
}
