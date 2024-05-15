package tech.intellispacesframework.core.traverse;

import tech.intellispacesframework.core.exception.TraverseException;

public interface TraverseExecutor {

  Object execute(CallGuide0TraversePlan traversePlan, Object source) throws TraverseException;

  Object execute(CallGuide1TraversePlan traversePlan, Object source, Object qualifier) throws TraverseException;

  Object execute(MoveObjectHandleThruTransition0TraversePlan traversePlan, Object source) throws TraverseException;

  Object execute(MoveObjectHandleThruTransition1TraversePlan traversePlan, Object source, Object qualifier) throws TraverseException;
}
