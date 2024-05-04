package tech.intellispacesframework.core.guide.n0;

import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.guide.Guide;

public interface Guide0<S, T> extends Guide<S, T> {

  T traverse(S source) throws TraverseException;
}
