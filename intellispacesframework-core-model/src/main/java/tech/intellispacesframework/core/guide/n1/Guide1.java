package tech.intellispacesframework.core.guide.n1;

import tech.intellispacesframework.core.exception.TraverseException;
import tech.intellispacesframework.core.guide.Guide;

public interface Guide1<S, T, Q> extends Guide<S, T> {

  T sync(S source, Q qualifier) throws TraverseException;
}
