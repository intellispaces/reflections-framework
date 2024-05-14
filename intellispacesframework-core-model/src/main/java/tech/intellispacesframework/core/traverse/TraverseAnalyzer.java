package tech.intellispacesframework.core.traverse;

import java.util.Optional;

public interface TraverseAnalyzer {

  MoveObjectHandleThruTransition1GeneralTraversePlan buildTraversePlanMoveObjectHandleThruTransition1(Class<?> objectHandleClass, String tid);

  Optional<EffectiveTraversePlan> buildEffectiveTaskPlanFor(MoveObjectHandleThruTransition1GeneralTraversePlan traversePlan, Class<?> objectHandleClass);
}
