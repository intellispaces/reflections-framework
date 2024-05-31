package tech.intellispaces.framework.core.guide.n5;

import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.Guide;

public interface Guide5<S, T, Q1, Q2, Q3, Q4, Q5> extends Guide<S, T> {

  T traverse(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4, Q5 qualifier5) throws TraverseException;
}
