package tech.intellispaces.framework.core.guide.n1;

import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.Guide;

public interface Guide1<S, T, Q> extends Guide<S, T> {

  T traverse(S source, Q qualifier) throws TraverseException;
}
