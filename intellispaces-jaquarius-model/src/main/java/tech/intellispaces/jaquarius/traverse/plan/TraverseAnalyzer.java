package tech.intellispaces.jaquarius.traverse.plan;

import tech.intellispaces.jaquarius.guide.GuideForm;

public interface TraverseAnalyzer {

  MapObjectHandleThruChannel0Plan buildMapObjectHandleThruChannel0Plan(Class<?> sourceClass, String cid, GuideForm guideForm);

  MapObjectHandleThruChannel1Plan buildMapObjectHandleThruChannel1Plan(Class<?> sourceClass, String cid, GuideForm guideForm);

  MapObjectHandleThruChannel2Plan buildMapObjectHandleThruChannel2Plan(Class<?> sourceClass, String cid, GuideForm guideForm);

  MapObjectHandleThruChannel3Plan buildMapObjectHandleThruChannel3Plan(Class<?> sourceClass, String cid, GuideForm guideForm);

  MoveObjectHandleThruChannel0Plan buildMoveObjectHandleThruChannel0Plan(Class<?> sourceClass, String cid, GuideForm guideForm);

  MoveObjectHandleThruChannel1Plan buildMoveObjectHandleThruChannel1Plan(Class<?> sourceClass, String cid, GuideForm guideForm);

  MoveObjectHandleThruChannel2Plan buildMoveObjectHandleThruChannel2Plan(Class<?> sourceClass, String cid, GuideForm guideForm);

  MoveObjectHandleThruChannel3Plan buildMoveObjectHandleThruChannel3Plan(Class<?> sourceClass, String cid, GuideForm guideForm);

  MapOfMovingObjectHandleThruChannel0Plan buildMapOfMovingObjectHandleThruChannel0Plan(Class<?> sourceClass, String cid, GuideForm guideForm);

  MapOfMovingObjectHandleThruChannel1Plan buildMapOfMovingObjectHandleThruChannel1Plan(Class<?> sourceClass, String cid, GuideForm guideForm);

  MapOfMovingObjectHandleThruChannel2Plan buildMapOfMovingObjectHandleThruChannel2Plan(Class<?> sourceClass, String cid, GuideForm guideForm);

  MapOfMovingObjectHandleThruChannel3Plan buildMapOfMovingObjectHandleThruChannel3Plan(Class<?> sourceClass, String cid, GuideForm guideForm);

  MapOfMovingObjectHandleThruChannel4Plan buildMapOfMovingObjectHandleThruChannel4Plan(Class<?> sourceClass, String cid, GuideForm guideForm);

  ExecutionTraversePlan getExecutionPlan(ObjectHandleTraversePlan declarativePlan, Class<?> sourceClass, GuideForm guideForm);
}
