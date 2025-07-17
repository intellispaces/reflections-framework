package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.ReflectionPoint;
import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.task.plan.ExecutionTraversePlan;
import tech.intellispaces.reflections.framework.task.plan.MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan;
import tech.intellispaces.reflections.framework.task.plan.MapSourceSpecifiedClassThruIdentifiedChannelPlan;
import tech.intellispaces.reflections.framework.task.plan.MapSpecifiedSourceAndQualifierToSpecifiedTargetDomainAndClassPlan;
import tech.intellispaces.reflections.framework.task.plan.MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan;
import tech.intellispaces.reflections.framework.task.plan.MoveSourceSpecifiedClassThruIdentifiedChannelPlan;
import tech.intellispaces.reflections.framework.task.plan.MoveSpecifiedSourceAndQualifierThruChannel1Plan;
import tech.intellispaces.reflections.framework.task.plan.TraversePlan;
import tech.intellispaces.reflections.framework.task.plan.TraverseSourceSpecifiedClassThruIdentifierChannelTraversePlan;

/**
 * The traverse analyzer.
 */
public interface TraverseAnalyzer {

  MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan buildMapToDomainPlan(
      ReflectionPoint source, ReflectionDomain targetDomain, Class<?> targetClass
  );

  MapSpecifiedSourceAndQualifierToSpecifiedTargetDomainAndClassPlan buildMapToDomainPlan(
      ReflectionPoint source, ReflectionDomain targetDomain, Object qualifier, Class<?> targetClass
  );

  MapSourceSpecifiedClassThruIdentifiedChannelPlan buildMapThruChannelPlan(
      Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  );

  MoveSourceSpecifiedClassThruIdentifiedChannelPlan buildMoveThruChannelPlan(
      Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  );

  MoveSpecifiedSourceAndQualifierThruChannel1Plan buildMoveThruChannel1Plan(Object source, Rid cid, Object qualifier);

  MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan buildMapOfMovingThruChannelPlan(
      Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  );

  TraversePlan buildExecutionPlan(MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan plan);

  TraversePlan buildExecutionPlan(MapSpecifiedSourceAndQualifierToSpecifiedTargetDomainAndClassPlan plan);

  TraversePlan buildExecutionPlan(MoveSpecifiedSourceAndQualifierThruChannel1Plan plan);

  ExecutionTraversePlan buildExecutionPlan(
      TraverseSourceSpecifiedClassThruIdentifierChannelTraversePlan declarativePlan,
      Object source,
      ReflectionForm targetForm
  );

  ExecutionTraversePlan buildExecutionPlan(
      TraverseSourceSpecifiedClassThruIdentifierChannelTraversePlan declarativePlan,
      Class<?> sourceClass,
      ReflectionForm targetForm
  );
}
