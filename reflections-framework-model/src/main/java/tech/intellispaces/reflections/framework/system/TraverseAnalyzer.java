package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.core.Domain;
import tech.intellispaces.core.Reflection;
import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.task.plan.ExecutionTraversePlan;
import tech.intellispaces.reflections.framework.task.plan.MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan;
import tech.intellispaces.reflections.framework.task.plan.MapSourceSpecifiedClassThruIdentifiedChannelPlan;
import tech.intellispaces.reflections.framework.task.plan.MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan;
import tech.intellispaces.reflections.framework.task.plan.MoveSourceSpecifiedClassThruIdentifiedChannelPlan;
import tech.intellispaces.reflections.framework.task.plan.TraversePlan;
import tech.intellispaces.reflections.framework.task.plan.TraverseSourceSpecifiedClassThruIdentifierChannelTraversePlan;

/**
 * The traverse analyzer.
 */
public interface TraverseAnalyzer {

  MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan buildMapToDomainPlan(
      Reflection source, Domain targetDomain, Class<?> targetClass
  );

  MapSourceSpecifiedClassThruIdentifiedChannelPlan buildMapThruChannelPlan(
      Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  );

  MoveSourceSpecifiedClassThruIdentifiedChannelPlan buildMoveThruChannelPlan(
      Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  );

  MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan buildMapOfMovingThruChannelPlan(
      Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  );

  TraversePlan buildExecutionPlan(
      MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan plan
  );

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
