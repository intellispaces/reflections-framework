package intellispaces.framework.core.traverse;

public interface TraverseAnalyzer {

  MapObjectHandleThruTransition0Plan buildMapObjectHandleThruTransition0Plan(Class<?> sourceClass, String tid);

  MapObjectHandleThruTransition1Plan buildMapObjectHandleThruTransition1Plan(Class<?> sourceClass, String tid);

  MapObjectHandleThruTransition2Plan buildMapObjectHandleThruTransition2Plan(Class<?> sourceClass, String tid);

  MapObjectHandleThruTransition3Plan buildMapObjectHandleThruTransition3Plan(Class<?> sourceClass, String tid);

  MoveObjectHandleThruTransition0Plan buildMoveObjectHandleThruTransition0Plan(Class<?> sourceClass, String tid);

  MoveObjectHandleThruTransition1Plan buildMoveObjectHandleThruTransition1Plan(Class<?> sourceClass, String tid);

  MoveObjectHandleThruTransition2Plan buildMoveObjectHandleThruTransition2Plan(Class<?> sourceClass, String tid);

  MoveObjectHandleThruTransition3Plan buildMoveObjectHandleThruTransition3Plan(Class<?> sourceClass, String tid);

  ExecutionPlan getExecutionPlan(ObjectHandleTraversePlan declarativePlan, Class<?> sourceClass);
}
