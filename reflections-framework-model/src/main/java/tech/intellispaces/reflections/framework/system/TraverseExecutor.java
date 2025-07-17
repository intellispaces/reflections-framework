package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.task.plan.AscendAndExecutePlan;
import tech.intellispaces.reflections.framework.task.plan.CallGuidePlan;
import tech.intellispaces.reflections.framework.task.plan.MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan;
import tech.intellispaces.reflections.framework.task.plan.MapSourceSpecifiedClassThruIdentifiedChannelPlan;
import tech.intellispaces.reflections.framework.task.plan.MapSpecifiedSourceAndQualifierToSpecifiedTargetDomainAndClassPlan;
import tech.intellispaces.reflections.framework.task.plan.MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan;
import tech.intellispaces.reflections.framework.task.plan.MoveSourceSpecifiedClassThruIdentifiedChannelPlan;
import tech.intellispaces.reflections.framework.task.plan.MoveSpecifiedSourceAndQualifierThruChannel1Plan;

/**
 * The traverse plan executor.
 */
public interface TraverseExecutor {

  Object execute(
      CallGuidePlan plan,
      Object source
  ) throws TraverseException;

  int executeReturnInt(
      CallGuidePlan plan,
      Object source
  ) throws TraverseException;

  double executeReturnDouble(
      CallGuidePlan plan,
      Object source
  ) throws TraverseException;

  Object execute(
      CallGuidePlan plan,
      Object source,
      Object qualifier
  ) throws TraverseException;

  Object execute(
      CallGuidePlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2
  ) throws TraverseException;

  Object execute(
      CallGuidePlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3
  ) throws TraverseException;

  Object execute(
      CallGuidePlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3,
      Object qualifier4
  ) throws TraverseException;

  Object execute(
      AscendAndExecutePlan plan,
      Object source,
      Object qualifier
  ) throws TraverseException;

  Object execute(
      MapSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source
  ) throws TraverseException;

  int executeReturnInt(
      MapSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source
  ) throws TraverseException;

  double executeReturnDouble(
      MapSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source
  ) throws TraverseException;

  Object execute(
      MapSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      Object qualifier
  ) throws TraverseException;

  Object execute(
      MapSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2
  ) throws TraverseException;

  Object execute(
      MapSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3
  ) throws TraverseException;


  Object execute(
      MoveSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source
  ) throws TraverseException;

  Object execute(
      MoveSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      Object qualifier
  ) throws TraverseException;

  Object execute(
      MoveSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2
  ) throws TraverseException;

  Object execute(
      MoveSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3
  ) throws TraverseException;

  Object execute(
      MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source
  ) throws TraverseException;

  int executeReturnInt(
      MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source
  ) throws TraverseException;

  double executeReturnDouble(
      MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source
  ) throws TraverseException;

  Object execute(
      MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      Object qualifier
  ) throws TraverseException;

  Object execute(
      MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2
  ) throws TraverseException;

  Object execute(
      MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3
  ) throws TraverseException;

  Object execute(
      MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3,
      Object qualifier4
  ) throws TraverseException;

  Object execute(MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan plan) throws TraverseException;

  int executeReturnInt(MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan plan) throws TraverseException;

  double executeReturnDouble(MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan plan) throws TraverseException;

  Object execute(MapSpecifiedSourceAndQualifierToSpecifiedTargetDomainAndClassPlan plan) throws TraverseException;

  Object execute(MoveSpecifiedSourceAndQualifierThruChannel1Plan plan) throws TraverseException;
}
