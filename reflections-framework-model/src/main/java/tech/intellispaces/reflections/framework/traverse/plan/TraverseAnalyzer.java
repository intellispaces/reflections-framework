package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.reflections.framework.reflection.ReflectionForm;

public interface TraverseAnalyzer {

  MapObjectHandleThruChannel0Plan buildMapObjectHandleThruChannel0Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MapObjectHandleThruChannel1Plan buildMapObjectHandleThruChannel1Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MapObjectHandleThruChannel2Plan buildMapObjectHandleThruChannel2Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MapObjectHandleThruChannel3Plan buildMapObjectHandleThruChannel3Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MoveObjectHandleThruChannel0Plan buildMoveObjectHandleThruChannel0Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MoveObjectHandleThruChannel1Plan buildMoveObjectHandleThruChannel1Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MoveObjectHandleThruChannel2Plan buildMoveObjectHandleThruChannel2Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MoveObjectHandleThruChannel3Plan buildMoveObjectHandleThruChannel3Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MapOfMovingObjectHandleThruChannel0Plan buildMapOfMovingObjectHandleThruChannel0Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MapOfMovingObjectHandleThruChannel1Plan buildMapOfMovingObjectHandleThruChannel1Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MapOfMovingObjectHandleThruChannel2Plan buildMapOfMovingObjectHandleThruChannel2Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MapOfMovingObjectHandleThruChannel3Plan buildMapOfMovingObjectHandleThruChannel3Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MapOfMovingObjectHandleThruChannel4Plan buildMapOfMovingObjectHandleThruChannel4Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  ExecutionTraversePlan getExecutionPlan(ObjectHandleTraversePlan declarativePlan, Object source, ReflectionForm targetForm);

  ExecutionTraversePlan getExecutionPlan(ObjectHandleTraversePlan declarativePlan, Class<?> sourceClass, ReflectionForm targetForm);
}
