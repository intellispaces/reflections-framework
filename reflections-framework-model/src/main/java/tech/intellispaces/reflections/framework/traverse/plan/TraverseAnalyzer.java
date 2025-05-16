package tech.intellispaces.reflections.framework.traverse.plan;

import tech.intellispaces.reflections.framework.reflection.ReflectionForm;

public interface TraverseAnalyzer {

  MapThruChannel0TraversePlan buildMapThruChannel0Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MapThruChannel1TraversePlan buildMapThruChannel1Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MapThruChannel2TraversePlan buildMapThruChannel2Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MapThruChannel3TraversePlan buildMapThruChannel3Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MoveThruChannel0TraversePlan buildMoveThruChannel0Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MoveThruChannel1TraversePlan buildMoveThruChannel1Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MoveThruChannel2TraversePlan buildMoveThruChannel2Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MoveThruChannel3TraversePlan buildMoveThruChannel3Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MapOfMovingThruChannel0Plan buildMapOfMovingThruChannel0Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MapOfMovingThruChannel1TraversePlan buildMapOfMovingThruChannel1Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MapOfMovingThruChannel2TraversePlan buildMapOfMovingThruChannel2Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MapOfMovingThruChannel3TraversePlan buildMapOfMovingThruChannel3Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  MapOfMovingThruChannel4TraversePlan buildMapOfMovingThruChannel4Plan(Class<?> sourceClass, String cid, ReflectionForm targetForm);

  ExecutionTraversePlan getExecutionPlan(TraverseThruChannelPlan declarativePlan, Object source, ReflectionForm targetForm);

  ExecutionTraversePlan getExecutionPlan(TraverseThruChannelPlan declarativePlan, Class<?> sourceClass, ReflectionForm targetForm);
}
