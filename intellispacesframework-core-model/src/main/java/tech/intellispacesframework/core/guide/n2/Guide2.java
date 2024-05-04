package tech.intellispacesframework.core.guide.n2;

import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.guide.Guide;

public interface Guide2<S, T, Q1, Q2> extends Guide<S, T> {

  T traverse(S source, Q1 qualifier1, Q2 qualifier2) throws TraverseException;
}
