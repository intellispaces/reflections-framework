package tech.intellispacesframework.core.traverse;

import java.util.Optional;

public interface TraverseAnalyzer {

  MoveObjectHandleThruTransition0TraversePlan buildTraversePlanMoveObjectHandleThruTransition0(Class<?> objectHandleClass, String tid);

  MoveObjectHandleThruTransition1TraversePlan buildTraversePlanMoveObjectHandleThruTransition1(Class<?> objectHandleClass, String tid);

  Optional<EffectiveTraversePlan> buildEffectiveTaskPlanFor(MoveObjectHandleThruTransition0TraversePlan traversePlan, Class<?> objectHandleClass);

  Optional<EffectiveTraversePlan> buildEffectiveTaskPlanFor(MoveObjectHandleThruTransition1TraversePlan traversePlan, Class<?> objectHandleClass);
}
