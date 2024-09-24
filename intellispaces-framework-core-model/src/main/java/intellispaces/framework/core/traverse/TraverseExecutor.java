package intellispaces.framework.core.traverse;

import intellispaces.framework.core.exception.TraverseException;

public interface TraverseExecutor {

  Object execute(CallGuide0Plan plan, Object source) throws TraverseException;

  Object execute(CallGuide1Plan plan, Object source, Object qualifier) throws TraverseException;

  Object execute(CallGuide2Plan plan, Object source, Object qualifier1, Object qualifier2) throws TraverseException;

  Object execute(CallGuide3Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3) throws TraverseException;

  Object execute(MapObjectHandleThruTransition0Plan plan, Object source) throws TraverseException;

  Object execute(MapObjectHandleThruTransition1Plan plan, Object source, Object qualifier) throws TraverseException;

  Object execute(MapObjectHandleThruTransition2Plan plan, Object source, Object qualifier1, Object qualifier2) throws TraverseException;

  Object execute(MapObjectHandleThruTransition3Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3) throws TraverseException;

  Object execute(MoveObjectHandleThruTransition0Plan plan, Object source) throws TraverseException;

  Object execute(MoveObjectHandleThruTransition1Plan plan, Object source, Object qualifier) throws TraverseException;

  Object execute(MoveObjectHandleThruTransition2Plan plan, Object source, Object qualifier1, Object qualifier2) throws TraverseException;

  Object execute(MoveObjectHandleThruTransition3Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3) throws TraverseException;

  Object execute(MapOfMovingObjectHandleThruTransition0Plan plan, Object source) throws TraverseException;

  Object execute(MapOfMovingObjectHandleThruTransition1Plan plan, Object source, Object qualifier) throws TraverseException;

  Object execute(MapOfMovingObjectHandleThruTransition2Plan plan, Object source, Object qualifier1, Object qualifier2) throws TraverseException;

  Object execute(MapOfMovingObjectHandleThruTransition3Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3) throws TraverseException;

}
