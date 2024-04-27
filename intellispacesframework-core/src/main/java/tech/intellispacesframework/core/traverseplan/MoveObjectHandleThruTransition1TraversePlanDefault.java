package tech.intellispacesframework.core.traverseplan;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.exception.TraverseException;

public class MoveObjectHandleThruTransition1TraversePlanDefault implements MoveObjectHandleThruTransition1TraversePlan {
  private final Class<?> objectHandleClass;
  private final String tid;
  private TraversePlan delegate;

  public MoveObjectHandleThruTransition1TraversePlanDefault(Class<?> objectHandleClass, String tid, TraversePlan delegate) {
    this.objectHandleClass = objectHandleClass;
    this.tid = tid;
    this.delegate = delegate;
  }

  @Override
  public TraversePlanType type() {
    return TraversePlanTypes.MoveObjectHandleThruTransition1;
  }

  @Override
  public Class<?> objectHandleClass() {
    return objectHandleClass;
  }

  @Override
  public String tid() {
    return tid;
  }

  @Override
  public TraversePlan delegate() {
    return delegate;
  }

  @Override
  public Object traverse(Object source) {
    throw UnexpectedViolationException.withMessage("Expected traverse with one qualifier");
  }

  @Override
  public Object traverse(Object source, Object qualifier) throws TraverseException {
    if (delegate == null) {
      //todo: try rebuild the traverse plan (if Traverse Analyzer state was changed only). Otherwise throws exception.
      throw TraverseException.withMessage("Cannot to build traverse plan");
    }
    return delegate.traverse(source, qualifier);
  }
}
