package tech.intellispaces.framework.core.guide.n3;

import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.Guide;

public interface Guide3<S, T, Q1, Q2, Q3> extends Guide<S, T> {

  T traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3) throws TraverseException;
}
