package tech.intellispaces.framework.core.traverse;

public interface TraverseAnalyzer {

  MapObjectHandleThruTransition0Plan buildTraversePlanMapObjectHandleThruTransition0(Class<?> objectHandleClass, String tid);

  MapObjectHandleThruTransition1Plan buildTraversePlanMapObjectHandleThruTransition1(Class<?> objectHandleClass, String tid);

  MapObjectHandleThruTransition2Plan buildTraversePlanMapObjectHandleThruTransition2(Class<?> objectHandleClass, String tid);

  MoveObjectHandleThruTransition0Plan buildTraversePlanMoveObjectHandleThruTransition0(Class<?> objectHandleClass, String tid);

  MoveObjectHandleThruTransition1Plan buildTraversePlanMoveObjectHandleThruTransition1(Class<?> objectHandleClass, String tid);

  MoveObjectHandleThruTransition2Plan buildTraversePlanMoveObjectHandleThruTransition2(Class<?> objectHandleClass, String tid);

  ActualPlan getActualTraversePlan(ObjectHandleTraversePlan declarativePlan, Class<?> objectHandleClass);
}
