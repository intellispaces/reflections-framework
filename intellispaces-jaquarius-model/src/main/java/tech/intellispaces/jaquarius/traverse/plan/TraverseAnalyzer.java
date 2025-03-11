package tech.intellispaces.jaquarius.traverse.plan;

import tech.intellispaces.jaquarius.object.reference.ObjectForm;

public interface TraverseAnalyzer {

  MapObjectHandleThruChannel0Plan buildMapObjectHandleThruChannel0Plan(Class<?> sourceClass, String cid, ObjectForm targetForm);

  MapObjectHandleThruChannel1Plan buildMapObjectHandleThruChannel1Plan(Class<?> sourceClass, String cid, ObjectForm targetForm);

  MapObjectHandleThruChannel2Plan buildMapObjectHandleThruChannel2Plan(Class<?> sourceClass, String cid, ObjectForm targetForm);

  MapObjectHandleThruChannel3Plan buildMapObjectHandleThruChannel3Plan(Class<?> sourceClass, String cid, ObjectForm targetForm);

  MoveObjectHandleThruChannel0Plan buildMoveObjectHandleThruChannel0Plan(Class<?> sourceClass, String cid, ObjectForm targetForm);

  MoveObjectHandleThruChannel1Plan buildMoveObjectHandleThruChannel1Plan(Class<?> sourceClass, String cid, ObjectForm targetForm);

  MoveObjectHandleThruChannel2Plan buildMoveObjectHandleThruChannel2Plan(Class<?> sourceClass, String cid, ObjectForm targetForm);

  MoveObjectHandleThruChannel3Plan buildMoveObjectHandleThruChannel3Plan(Class<?> sourceClass, String cid, ObjectForm targetForm);

  MapOfMovingObjectHandleThruChannel0Plan buildMapOfMovingObjectHandleThruChannel0Plan(Class<?> sourceClass, String cid, ObjectForm targetForm);

  MapOfMovingObjectHandleThruChannel1Plan buildMapOfMovingObjectHandleThruChannel1Plan(Class<?> sourceClass, String cid, ObjectForm targetForm);

  MapOfMovingObjectHandleThruChannel2Plan buildMapOfMovingObjectHandleThruChannel2Plan(Class<?> sourceClass, String cid, ObjectForm targetForm);

  MapOfMovingObjectHandleThruChannel3Plan buildMapOfMovingObjectHandleThruChannel3Plan(Class<?> sourceClass, String cid, ObjectForm targetForm);

  MapOfMovingObjectHandleThruChannel4Plan buildMapOfMovingObjectHandleThruChannel4Plan(Class<?> sourceClass, String cid, ObjectForm targetForm);

  ExecutionTraversePlan getExecutionPlan(ObjectHandleTraversePlan declarativePlan, Class<?> sourceClass, ObjectForm targetForm);
}
