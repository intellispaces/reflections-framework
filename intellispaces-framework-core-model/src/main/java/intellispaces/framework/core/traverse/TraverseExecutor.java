package intellispaces.framework.core.traverse;

import intellispaces.framework.core.exception.TraverseException;

public interface TraverseExecutor {

  Object execute(CallGuide0Plan plan, Object source) throws TraverseException;

  Object execute(CallGuide1Plan plan, Object source, Object qualifier) throws TraverseException;

  Object execute(CallGuide2Plan plan, Object source, Object qualifier1, Object qualifier2) throws TraverseException;

  Object execute(MapObjectHandleThruTransition0Plan plan, Object source) throws TraverseException;

  Object execute(MapObjectHandleThruTransition1Plan plan, Object source, Object qualifier) throws TraverseException;

  Object execute(MapObjectHandleThruTransition2Plan plan, Object source, Object qualifier1, Object qualifier2) throws TraverseException;

  Object execute(MoveObjectHandleThruTransition0Plan plan, Object source) throws TraverseException;

  Object execute(MoveObjectHandleThruTransition1Plan plan, Object source, Object qualifier) throws TraverseException;

  Object execute(MoveObjectHandleThruTransition2Plan plan, Object source, Object qualifier1, Object qualifier2) throws TraverseException;
}
