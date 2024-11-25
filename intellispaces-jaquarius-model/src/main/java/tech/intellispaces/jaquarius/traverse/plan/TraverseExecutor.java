package tech.intellispaces.jaquarius.traverse.plan;

import tech.intellispaces.jaquarius.exception.TraverseException;

public interface TraverseExecutor {

  Object execute(CallGuide0Plan plan, Object source) throws TraverseException;

  int executeReturnInt(CallGuide0Plan plan, Object source) throws TraverseException;

  double executeReturnDouble(CallGuide0Plan plan, Object source) throws TraverseException;

  Object execute(CallGuide1Plan plan, Object source, Object qualifier) throws TraverseException;

  Object execute(CallGuide2Plan plan, Object source, Object qualifier1, Object qualifier2) throws TraverseException;

  Object execute(CallGuide3Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3) throws TraverseException;

  Object execute(CallGuide4Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3, Object qualifier4) throws TraverseException;

  Object execute(MapObjectHandleThruChannel0Plan plan, Object source) throws TraverseException;

  int executeReturnInt(MapObjectHandleThruChannel0Plan plan, Object source) throws TraverseException;

  double executeReturnDouble(MapObjectHandleThruChannel0Plan plan, Object source) throws TraverseException;

  Object execute(MapObjectHandleThruChannel1Plan plan, Object source, Object qualifier) throws TraverseException;

  Object execute(MapObjectHandleThruChannel2Plan plan, Object source, Object qualifier1, Object qualifier2) throws TraverseException;

  Object execute(MapObjectHandleThruChannel3Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3) throws TraverseException;

  Object execute(MoveObjectHandleThruChannel0Plan plan, Object source) throws TraverseException;

  Object execute(MoveObjectHandleThruChannel1Plan plan, Object source, Object qualifier) throws TraverseException;

  Object execute(MoveObjectHandleThruChannel2Plan plan, Object source, Object qualifier1, Object qualifier2) throws TraverseException;

  Object execute(MoveObjectHandleThruChannel3Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3) throws TraverseException;

  Object execute(MapOfMovingObjectHandleThruChannel0Plan plan, Object source) throws TraverseException;

  int executeReturnInt(MapOfMovingObjectHandleThruChannel0Plan plan, Object source) throws TraverseException;

  double executeReturnDouble(MapOfMovingObjectHandleThruChannel0Plan plan, Object source) throws TraverseException;

  Object execute(MapOfMovingObjectHandleThruChannel1Plan plan, Object source, Object qualifier) throws TraverseException;

  Object execute(MapOfMovingObjectHandleThruChannel2Plan plan, Object source, Object qualifier1, Object qualifier2) throws TraverseException;

  Object execute(MapOfMovingObjectHandleThruChannel3Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3) throws TraverseException;

  Object execute(MapOfMovingObjectHandleThruChannel4Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3, Object qualifier4) throws TraverseException;
}
