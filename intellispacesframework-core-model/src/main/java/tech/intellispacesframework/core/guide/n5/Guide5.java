package tech.intellispacesframework.core.guide.n5;

import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.guide.Guide;

public interface Guide5<S, T, Q1, Q2, Q3, Q4, Q5> extends Guide<S, T> {

  T sync(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3, Q4 qualifier4, Q5 qualifier5) throws TraverseException;
}
