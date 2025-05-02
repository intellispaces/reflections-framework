package tech.intellispaces.reflectionsj.traverse.plan;

import tech.intellispaces.reflectionsj.object.reference.ObjectReferenceForm;

public interface TraverseAnalyzer {

  MapObjectHandleThruChannel0Plan buildMapObjectHandleThruChannel0Plan(Class<?> sourceClass, String cid, ObjectReferenceForm targetForm);

  MapObjectHandleThruChannel1Plan buildMapObjectHandleThruChannel1Plan(Class<?> sourceClass, String cid, ObjectReferenceForm targetForm);

  MapObjectHandleThruChannel2Plan buildMapObjectHandleThruChannel2Plan(Class<?> sourceClass, String cid, ObjectReferenceForm targetForm);

  MapObjectHandleThruChannel3Plan buildMapObjectHandleThruChannel3Plan(Class<?> sourceClass, String cid, ObjectReferenceForm targetForm);

  MoveObjectHandleThruChannel0Plan buildMoveObjectHandleThruChannel0Plan(Class<?> sourceClass, String cid, ObjectReferenceForm targetForm);

  MoveObjectHandleThruChannel1Plan buildMoveObjectHandleThruChannel1Plan(Class<?> sourceClass, String cid, ObjectReferenceForm targetForm);

  MoveObjectHandleThruChannel2Plan buildMoveObjectHandleThruChannel2Plan(Class<?> sourceClass, String cid, ObjectReferenceForm targetForm);

  MoveObjectHandleThruChannel3Plan buildMoveObjectHandleThruChannel3Plan(Class<?> sourceClass, String cid, ObjectReferenceForm targetForm);

  MapOfMovingObjectHandleThruChannel0Plan buildMapOfMovingObjectHandleThruChannel0Plan(Class<?> sourceClass, String cid, ObjectReferenceForm targetForm);

  MapOfMovingObjectHandleThruChannel1Plan buildMapOfMovingObjectHandleThruChannel1Plan(Class<?> sourceClass, String cid, ObjectReferenceForm targetForm);

  MapOfMovingObjectHandleThruChannel2Plan buildMapOfMovingObjectHandleThruChannel2Plan(Class<?> sourceClass, String cid, ObjectReferenceForm targetForm);

  MapOfMovingObjectHandleThruChannel3Plan buildMapOfMovingObjectHandleThruChannel3Plan(Class<?> sourceClass, String cid, ObjectReferenceForm targetForm);

  MapOfMovingObjectHandleThruChannel4Plan buildMapOfMovingObjectHandleThruChannel4Plan(Class<?> sourceClass, String cid, ObjectReferenceForm targetForm);

  ExecutionTraversePlan getExecutionPlan(ObjectHandleTraversePlan declarativePlan, Object source, ObjectReferenceForm targetForm);

  ExecutionTraversePlan getExecutionPlan(ObjectHandleTraversePlan declarativePlan, Class<?> sourceClass, ObjectReferenceForm targetForm);
}
