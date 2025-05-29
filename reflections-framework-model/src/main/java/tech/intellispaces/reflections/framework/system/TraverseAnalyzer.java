package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.core.Domain;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.traverse.plan.ExecutionTraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel0Plan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel3TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel4TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecifiedClassSourceThruIdentifiedChannel0TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecifiedClassSourceThruIdentifiedChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecifiedClassSourceThruIdentifiedChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecifiedClassSourceThruIdentifiedChannel3TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveSpecifiedClassSourceThruIdentifiedChannel0TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveSpecifiedClassSourceThruIdentifiedChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveSpecifiedClassSourceThruIdentifiedChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveSpecifiedClassSourceThruIdentifiedChannel3TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.TraverseSpecifiedClassSourceThruIdentifierChannelTraversePlan;

/**
 * The traverse analyzer.
 */
public interface TraverseAnalyzer {

  MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlan buildMapToTraversePlan(
      Reflection source, Domain targetDomain, Class<?> targetClass
  );

  MapSpecifiedClassSourceThruIdentifiedChannel0TraversePlan buildMapThruChannel0TraversePlan(
      Class<?> sourceClass, String cid, ReflectionForm targetForm
  );

  MapSpecifiedClassSourceThruIdentifiedChannel1TraversePlan buildMapThruChannel1TraversePlan(
      Class<?> sourceClass, String cid, ReflectionForm targetForm
  );

  MapSpecifiedClassSourceThruIdentifiedChannel2TraversePlan buildMapThruChannel2TraversePlan(
      Class<?> sourceClass, String cid, ReflectionForm targetForm
  );

  MapSpecifiedClassSourceThruIdentifiedChannel3TraversePlan buildMapThruChannel3TraversePlan(
      Class<?> sourceClass, String cid, ReflectionForm targetForm
  );

  MoveSpecifiedClassSourceThruIdentifiedChannel0TraversePlan buildMoveThruChannel0TraversePlan(
      Class<?> sourceClass, String cid, ReflectionForm targetForm
  );

  MoveSpecifiedClassSourceThruIdentifiedChannel1TraversePlan buildMoveThruChannel1TraversePlan(
      Class<?> sourceClass, String cid, ReflectionForm targetForm
  );

  MoveSpecifiedClassSourceThruIdentifiedChannel2TraversePlan buildMoveThruChannel2TraversePlan(
      Class<?> sourceClass, String cid, ReflectionForm targetForm
  );

  MoveSpecifiedClassSourceThruIdentifiedChannel3TraversePlan buildMoveThruChannel3TraversePlan(
      Class<?> sourceClass, String cid, ReflectionForm targetForm
  );

  MapOfMovingSpecifiedClassSourceThruIdentifiedChannel0Plan buildMapOfMovingThruChannel0TraversePlan(
      Class<?> sourceClass, String cid, ReflectionForm targetForm
  );

  MapOfMovingSpecifiedClassSourceThruIdentifiedChannel1TraversePlan buildMapOfMovingThruChannel1TraversePlan(
      Class<?> sourceClass, String cid, ReflectionForm targetForm
  );

  MapOfMovingSpecifiedClassSourceThruIdentifiedChannel2TraversePlan buildMapOfMovingThruChannel2TraversePlan(
      Class<?> sourceClass, String cid, ReflectionForm targetForm
  );

  MapOfMovingSpecifiedClassSourceThruIdentifiedChannel3TraversePlan buildMapOfMovingThruChannel3TraversePlan(
      Class<?> sourceClass, String cid, ReflectionForm targetForm
  );

  MapOfMovingSpecifiedClassSourceThruIdentifiedChannel4TraversePlan buildMapOfMovingThruChannel4TraversePlan(
      Class<?> sourceClass, String cid, ReflectionForm targetForm
  );

  ExecutionTraversePlan buildExecutionPlan(
      MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlan plan
  );

  ExecutionTraversePlan buildExecutionPlan(
      TraverseSpecifiedClassSourceThruIdentifierChannelTraversePlan declarativePlan,
      Object source,
      ReflectionForm targetForm
  );

  ExecutionTraversePlan buildExecutionPlan(
      TraverseSpecifiedClassSourceThruIdentifierChannelTraversePlan declarativePlan,
      Class<?> sourceClass,
      ReflectionForm targetForm
  );
}
