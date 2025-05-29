package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.core.Domain;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.traverse.plan.ExecutionTraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel0Plan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel3TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel4TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapThruChannel0TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapThruChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapThruChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapThruChannel3TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecificReflectionToSpecificDomainAndClassTraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveThruChannel0TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveThruChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveThruChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveThruChannel3TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.TraverseThruChannelPlan;

public interface TraverseAnalyzer {

  MapSpecificReflectionToSpecificDomainAndClassTraversePlan buildMapT0Plan(Reflection source, Domain targetDomain, Class<?> targetClass);

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
