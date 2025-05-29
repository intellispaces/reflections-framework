package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.traverse.plan.AscendAndExecutePlan1;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide0Plan;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide1Plan;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide2Plan;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide3Plan;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide4Plan;
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

/**
 * The traverse plan executor.
 */
public interface TraverseExecutor {

  Object execute(
      CallGuide0Plan plan,
      Object source
  ) throws TraverseException;

  int executeReturnInt(
      CallGuide0Plan plan,
      Object source
  ) throws TraverseException;

  double executeReturnDouble(
      CallGuide0Plan plan,
      Object source
  ) throws TraverseException;

  Object execute(
      CallGuide1Plan plan,
      Object source,
      Object qualifier
    ) throws TraverseException;

  Object execute(
      CallGuide2Plan plan,
      Object source,
      Object qualifier1,
      Object qualifier2
  ) throws TraverseException;

  Object execute(
      CallGuide3Plan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3
  ) throws TraverseException;

  Object execute(
      CallGuide4Plan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3,
      Object qualifier4
  ) throws TraverseException;

  Object execute(
      AscendAndExecutePlan1 plan,
      Object source,
      Object qualifier
  ) throws TraverseException;

  Object execute(
      MapSpecifiedClassSourceThruIdentifiedChannel0TraversePlan plan,
      Object source
  ) throws TraverseException;

  int executeReturnInt(
      MapSpecifiedClassSourceThruIdentifiedChannel0TraversePlan plan,
      Object source
  ) throws TraverseException;

  double executeReturnDouble(
      MapSpecifiedClassSourceThruIdentifiedChannel0TraversePlan plan,
      Object source
  ) throws TraverseException;

  Object execute(
      MapSpecifiedClassSourceThruIdentifiedChannel1TraversePlan plan,
      Object source,
      Object qualifier
  ) throws TraverseException;

  Object execute(
      MapSpecifiedClassSourceThruIdentifiedChannel2TraversePlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2
  ) throws TraverseException;

  Object execute(
      MapSpecifiedClassSourceThruIdentifiedChannel3TraversePlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3
  ) throws TraverseException;

  Object execute(
      MoveSpecifiedClassSourceThruIdentifiedChannel0TraversePlan plan,
      Object source
  ) throws TraverseException;

  Object execute(
      MoveSpecifiedClassSourceThruIdentifiedChannel1TraversePlan plan,
      Object source,
      Object qualifier
  ) throws TraverseException;

  Object execute(
      MoveSpecifiedClassSourceThruIdentifiedChannel2TraversePlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2
  ) throws TraverseException;

  Object execute(
      MoveSpecifiedClassSourceThruIdentifiedChannel3TraversePlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3
  ) throws TraverseException;

  Object execute(
      MapOfMovingSpecifiedClassSourceThruIdentifiedChannel0Plan plan,
      Object source
  ) throws TraverseException;

  int executeReturnInt(
      MapOfMovingSpecifiedClassSourceThruIdentifiedChannel0Plan plan,
      Object source
  ) throws TraverseException;

  double executeReturnDouble(
      MapOfMovingSpecifiedClassSourceThruIdentifiedChannel0Plan plan,
      Object source
  ) throws TraverseException;

  Object execute(
      MapOfMovingSpecifiedClassSourceThruIdentifiedChannel1TraversePlan plan,
      Object source,
      Object qualifier
  ) throws TraverseException;

  Object execute(
      MapOfMovingSpecifiedClassSourceThruIdentifiedChannel2TraversePlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2
  ) throws TraverseException;

  Object execute(
      MapOfMovingSpecifiedClassSourceThruIdentifiedChannel3TraversePlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3
  ) throws TraverseException;

  Object execute(
      MapOfMovingSpecifiedClassSourceThruIdentifiedChannel4TraversePlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3,
      Object qualifier4
  ) throws TraverseException;

  Object execute(
      MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlan plan
  ) throws TraverseException;

  int executeReturnInt(
      MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlan plan
  ) throws TraverseException;

  double executeReturnDouble(
      MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlan plan
  ) throws TraverseException;
}
