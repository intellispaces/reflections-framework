package intellispaces.framework.core.traverse.plan;

public interface TraverseAnalyzer {

  MapObjectHandleThruChannel0Plan buildMapObjectHandleThruChannel0Plan(Class<?> sourceClass, String cid);

  MapObjectHandleThruChannel1Plan buildMapObjectHandleThruChannel1Plan(Class<?> sourceClass, String cid);

  MapObjectHandleThruChannel2Plan buildMapObjectHandleThruChannel2Plan(Class<?> sourceClass, String cid);

  MapObjectHandleThruChannel3Plan buildMapObjectHandleThruChannel3Plan(Class<?> sourceClass, String cid);

  MoveObjectHandleThruChannel0Plan buildMoveObjectHandleThruChannel0Plan(Class<?> sourceClass, String cid);

  MoveObjectHandleThruChannel1Plan buildMoveObjectHandleThruChannel1Plan(Class<?> sourceClass, String cid);

  MoveObjectHandleThruChannel2Plan buildMoveObjectHandleThruChannel2Plan(Class<?> sourceClass, String cid);

  MoveObjectHandleThruChannel3Plan buildMoveObjectHandleThruChannel3Plan(Class<?> sourceClass, String cid);

  MapOfMovingObjectHandleThruChannel0Plan buildMapOfMovingObjectHandleThruChannel0Plan(Class<?> sourceClass, String cid);

  MapOfMovingObjectHandleThruChannel1Plan buildMapOfMovingObjectHandleThruChannel1Plan(Class<?> sourceClass, String cid);

  MapOfMovingObjectHandleThruChannel2Plan buildMapOfMovingObjectHandleThruChannel2Plan(Class<?> sourceClass, String cid);

  MapOfMovingObjectHandleThruChannel3Plan buildMapOfMovingObjectHandleThruChannel3Plan(Class<?> sourceClass, String cid);

  ExecutionPlan getExecutionPlan(ObjectHandleTraversePlan declarativePlan, Class<?> sourceClass);
}
