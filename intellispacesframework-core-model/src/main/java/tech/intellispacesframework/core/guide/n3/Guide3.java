package tech.intellispacesframework.core.guide.n3;

import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.guide.Guide;

public interface Guide3<S, T, Q1, Q2, Q3> extends Guide<S, T> {

  T sync(S source, Q1 qualifier1, Q2 qualifier2, Q3 qualifier3) throws TraverseException;
}
