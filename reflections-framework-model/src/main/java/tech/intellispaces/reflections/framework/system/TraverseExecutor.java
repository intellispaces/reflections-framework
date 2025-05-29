package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.traverse.plan.AscendAndExecutePlan1;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide0Plan;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide1Plan;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide2Plan;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide3Plan;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide4Plan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel0Plan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel3TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel4TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecificReflectionToSpecificDomainAndClassTraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapThruChannel0TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapThruChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapThruChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapThruChannel3TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveThruChannel0TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveThruChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveThruChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveThruChannel3TraversePlan;

/**
 * The traverse plan executor.
 */
public interface TraverseExecutor {

  Object execute(CallGuide0Plan plan, Object source) throws TraverseException;

  int executeReturnInt(CallGuide0Plan plan, Object source) throws TraverseException;

  double executeReturnDouble(CallGuide0Plan plan, Object source) throws TraverseException;

  Object execute(CallGuide1Plan plan, Object source, Object qualifier) throws TraverseException;

  Object execute(CallGuide2Plan plan, Object source, Object qualifier1, Object qualifier2) throws TraverseException;

  Object execute(CallGuide3Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3) throws TraverseException;

  Object execute(CallGuide4Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3, Object qualifier4) throws TraverseException;

  Object execute(AscendAndExecutePlan1 plan, Object source, Object qualifier) throws TraverseException;

  Object execute(MapThruChannel0TraversePlan plan, Object source) throws TraverseException;

  int executeReturnInt(MapThruChannel0TraversePlan plan, Object source) throws TraverseException;

  double executeReturnDouble(MapThruChannel0TraversePlan plan, Object source) throws TraverseException;

  Object execute(MapThruChannel1TraversePlan plan, Object source, Object qualifier) throws TraverseException;

  Object execute(MapThruChannel2TraversePlan plan, Object source, Object qualifier1, Object qualifier2) throws TraverseException;

  Object execute(MapThruChannel3TraversePlan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3) throws TraverseException;

  Object execute(MoveThruChannel0TraversePlan plan, Object source) throws TraverseException;

  Object execute(MoveThruChannel1TraversePlan plan, Object source, Object qualifier) throws TraverseException;

  Object execute(MoveThruChannel2TraversePlan plan, Object source, Object qualifier1, Object qualifier2) throws TraverseException;

  Object execute(MoveThruChannel3TraversePlan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3) throws TraverseException;

  Object execute(MapOfMovingThruChannel0Plan plan, Object source) throws TraverseException;

  int executeReturnInt(MapOfMovingThruChannel0Plan plan, Object source) throws TraverseException;

  double executeReturnDouble(MapOfMovingThruChannel0Plan plan, Object source) throws TraverseException;

  Object execute(MapOfMovingThruChannel1TraversePlan plan, Object source, Object qualifier) throws TraverseException;

  Object execute(MapOfMovingThruChannel2TraversePlan plan, Object source, Object qualifier1, Object qualifier2) throws TraverseException;

  Object execute(MapOfMovingThruChannel3TraversePlan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3) throws TraverseException;

  Object execute(MapOfMovingThruChannel4TraversePlan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3, Object qualifier4) throws TraverseException;

  Object execute(MapSpecificReflectionToSpecificDomainAndClassTraversePlan plan) throws TraverseException;

  int executeReturnInt(MapSpecificReflectionToSpecificDomainAndClassTraversePlan plan) throws TraverseException;

  double executeReturnDouble(MapSpecificReflectionToSpecificDomainAndClassTraversePlan plan) throws TraverseException;
}
