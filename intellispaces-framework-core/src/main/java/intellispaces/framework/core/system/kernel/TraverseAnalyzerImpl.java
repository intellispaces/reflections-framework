package intellispaces.framework.core.system.kernel;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.guide.Guide;
import intellispaces.framework.core.guide.GuideKind;
import intellispaces.framework.core.guide.GuideKinds;
import intellispaces.framework.core.guide.n0.Guide0;
import intellispaces.framework.core.guide.n1.Guide1;
import intellispaces.framework.core.guide.n2.Guide2;
import intellispaces.framework.core.guide.n3.Guide3;
import intellispaces.framework.core.object.ObjectFunctions;
import intellispaces.framework.core.traverse.CallGuide0PlanImpl;
import intellispaces.framework.core.traverse.CallGuide1PlanImpl;
import intellispaces.framework.core.traverse.CallGuide2PlanImpl;
import intellispaces.framework.core.traverse.CallGuide3PlanImpl;
import intellispaces.framework.core.traverse.ExecutionPlan;
import intellispaces.framework.core.traverse.MapObjectHandleThruTransition0Plan;
import intellispaces.framework.core.traverse.MapObjectHandleThruTransition0PlanImpl;
import intellispaces.framework.core.traverse.MapObjectHandleThruTransition1Plan;
import intellispaces.framework.core.traverse.MapObjectHandleThruTransition1PlanImpl;
import intellispaces.framework.core.traverse.MapObjectHandleThruTransition2Plan;
import intellispaces.framework.core.traverse.MapObjectHandleThruTransition2PlanImpl;
import intellispaces.framework.core.traverse.MapObjectHandleThruTransition3Plan;
import intellispaces.framework.core.traverse.MapObjectHandleThruTransition3PlanImpl;
import intellispaces.framework.core.traverse.MapOfMovingObjectHandleThruTransition0Plan;
import intellispaces.framework.core.traverse.MapOfMovingObjectHandleThruTransition0PlanImpl;
import intellispaces.framework.core.traverse.MapOfMovingObjectHandleThruTransition1Plan;
import intellispaces.framework.core.traverse.MapOfMovingObjectHandleThruTransition1PlanImpl;
import intellispaces.framework.core.traverse.MapOfMovingObjectHandleThruTransition2Plan;
import intellispaces.framework.core.traverse.MapOfMovingObjectHandleThruTransition2PlanImpl;
import intellispaces.framework.core.traverse.MapOfMovingObjectHandleThruTransition3Plan;
import intellispaces.framework.core.traverse.MapOfMovingObjectHandleThruTransition3PlanImpl;
import intellispaces.framework.core.traverse.MoveObjectHandleThruTransition0Plan;
import intellispaces.framework.core.traverse.MoveObjectHandleThruTransition0PlanImpl;
import intellispaces.framework.core.traverse.MoveObjectHandleThruTransition1Plan;
import intellispaces.framework.core.traverse.MoveObjectHandleThruTransition1PlanImpl;
import intellispaces.framework.core.traverse.MoveObjectHandleThruTransition2Plan;
import intellispaces.framework.core.traverse.MoveObjectHandleThruTransition2PlanImpl;
import intellispaces.framework.core.traverse.MoveObjectHandleThruTransition3Plan;
import intellispaces.framework.core.traverse.MoveObjectHandleThruTransition3PlanImpl;
import intellispaces.framework.core.traverse.ObjectHandleTraversePlan;
import intellispaces.framework.core.traverse.TraverseAnalyzer;
import intellispaces.framework.core.traverse.TraversePlanTypes;

import java.util.List;

class TraverseAnalyzerImpl implements TraverseAnalyzer {
  private final GuideRegistry guideRegistry;

  public TraverseAnalyzerImpl(GuideRegistry guideRegistry) {
    this.guideRegistry = guideRegistry;
  }

  @Override
  public MapObjectHandleThruTransition0Plan buildMapObjectHandleThruTransition0Plan(
    Class<?> sourceClass, String tid
  ) {
    MapObjectHandleThruTransition0Plan declarativePlan = new MapObjectHandleThruTransition0PlanImpl(
      sourceClass, tid);
    setExecutionPlan(declarativePlan, sourceClass);
    return declarativePlan;
  }

  @Override
  public MapObjectHandleThruTransition1Plan buildMapObjectHandleThruTransition1Plan(
    Class<?> sourceClass, String tid
  ) {
    MapObjectHandleThruTransition1Plan declarativePlan = new MapObjectHandleThruTransition1PlanImpl(
      sourceClass, tid);
    setExecutionPlan(declarativePlan, sourceClass);
    return declarativePlan;
  }

  @Override
  public MapObjectHandleThruTransition2Plan buildMapObjectHandleThruTransition2Plan(
    Class<?> sourceClass, String tid
  ) {
    MapObjectHandleThruTransition2Plan declarativePlan = new MapObjectHandleThruTransition2PlanImpl(
      sourceClass, tid);
    setExecutionPlan(declarativePlan, sourceClass);
    return declarativePlan;
  }

  @Override
  public MapObjectHandleThruTransition3Plan buildMapObjectHandleThruTransition3Plan(
    Class<?> sourceClass, String tid
  ) {
    MapObjectHandleThruTransition3Plan declarativePlan = new MapObjectHandleThruTransition3PlanImpl(
      sourceClass, tid);
    setExecutionPlan(declarativePlan, sourceClass);
    return declarativePlan;
  }

  @Override
  public MoveObjectHandleThruTransition0Plan buildMoveObjectHandleThruTransition0Plan(
    Class<?> sourceClass, String tid
  ) {
    MoveObjectHandleThruTransition0Plan declarativePlan = new MoveObjectHandleThruTransition0PlanImpl(
      sourceClass, tid);
    setExecutionPlan(declarativePlan, sourceClass);
    return declarativePlan;
  }

  @Override
  public MoveObjectHandleThruTransition1Plan buildMoveObjectHandleThruTransition1Plan(
    Class<?> sourceClass, String tid
  ) {
    MoveObjectHandleThruTransition1Plan declarativePlan = new MoveObjectHandleThruTransition1PlanImpl(
      sourceClass, tid);
    setExecutionPlan(declarativePlan, sourceClass);
    return declarativePlan;
  }

  @Override
  public MoveObjectHandleThruTransition2Plan buildMoveObjectHandleThruTransition2Plan(
    Class<?> sourceClass, String tid
  ) {
    MoveObjectHandleThruTransition2Plan declarativePlan = new MoveObjectHandleThruTransition2PlanImpl(
      sourceClass, tid);
    setExecutionPlan(declarativePlan, sourceClass);
    return declarativePlan;
  }

  @Override
  public MoveObjectHandleThruTransition3Plan buildMoveObjectHandleThruTransition3Plan(
    Class<?> sourceClass, String tid
  ) {
    MoveObjectHandleThruTransition3Plan declarativePlan = new MoveObjectHandleThruTransition3PlanImpl(
      sourceClass, tid);
    setExecutionPlan(declarativePlan, sourceClass);
    return declarativePlan;
  }

  @Override
  public MapOfMovingObjectHandleThruTransition0Plan buildMapOfMovingObjectHandleThruTransition0Plan(
      Class<?> sourceClass, String tid
  ) {
    MapOfMovingObjectHandleThruTransition0Plan declarativePlan = new MapOfMovingObjectHandleThruTransition0PlanImpl(
        sourceClass, tid);
    setExecutionPlan(declarativePlan, sourceClass);
    return declarativePlan;
  }

  @Override
  public MapOfMovingObjectHandleThruTransition1Plan buildMapOfMovingObjectHandleThruTransition1Plan(
      Class<?> sourceClass, String tid
  ) {
    MapOfMovingObjectHandleThruTransition1Plan declarativePlan = new MapOfMovingObjectHandleThruTransition1PlanImpl(
        sourceClass, tid);
    setExecutionPlan(declarativePlan, sourceClass);
    return declarativePlan;
  }

  @Override
  public MapOfMovingObjectHandleThruTransition2Plan buildMapOfMovingObjectHandleThruTransition2Plan(
      Class<?> sourceClass, String tid
  ) {
    MapOfMovingObjectHandleThruTransition2Plan declarativePlan = new MapOfMovingObjectHandleThruTransition2PlanImpl(
        sourceClass, tid);
    setExecutionPlan(declarativePlan, sourceClass);
    return declarativePlan;
  }

  @Override
  public MapOfMovingObjectHandleThruTransition3Plan buildMapOfMovingObjectHandleThruTransition3Plan(
      Class<?> sourceClass, String tid
  ) {
    MapOfMovingObjectHandleThruTransition3Plan declarativePlan = new MapOfMovingObjectHandleThruTransition3PlanImpl(
        sourceClass, tid);
    setExecutionPlan(declarativePlan, sourceClass);
    return declarativePlan;
  }

  private void setExecutionPlan(ObjectHandleTraversePlan plan, Class<?> objectHandleClass) {
    ExecutionPlan executionPlan = getExecutionPlan(plan, objectHandleClass);
    if (executionPlan != null) {
      plan.addExecutionPlan(objectHandleClass, executionPlan);
    }
  }

  @Override
  public ExecutionPlan getExecutionPlan(ObjectHandleTraversePlan plan, Class<?> sourceClass) {
    ExecutionPlan executionPlan = plan.getExecutionPlan(sourceClass);
    if (executionPlan != null) {
      return executionPlan;
    }

    if (!ObjectFunctions.isObjectHandleClass(sourceClass)) {
      throw UnexpectedViolationException.withMessage("Traverse plan of type {0} expected object handle to input",
          plan.type());
    }
    Class<?> objectHandleClass = ObjectFunctions.defineObjectHandleClass(sourceClass);
    executionPlan = plan.getExecutionPlan(objectHandleClass);
    if (executionPlan != null) {
      return executionPlan;
    }

    if (sourceClass != plan.objectHandleClass() && !plan.objectHandleClass().isAssignableFrom(sourceClass)) {
      throw UnexpectedViolationException.withMessage("Traverse plan of type {0} expected object handle of class {1} " +
          "or it subclass", plan.objectHandleClass());
    }
    executionPlan = plan.getExecutionPlan(plan.objectHandleClass());
    if (executionPlan != null) {
      return executionPlan;
    }

    executionPlan = buildExecutionTraversePlan(plan, sourceClass);
    if (executionPlan != null) {
      plan.addExecutionPlan(sourceClass, executionPlan);
    } else {
      executionPlan = buildExecutionTraversePlan(plan, objectHandleClass);
      if (executionPlan != null) {
        plan.addExecutionPlan(sourceClass, executionPlan);
        plan.addExecutionPlan(objectHandleClass, executionPlan);
      }
    }
    return executionPlan;
  }

  private ExecutionPlan buildExecutionTraversePlan(
      ObjectHandleTraversePlan plan, Class<?> objectHandleClass
  ) {
    GuideKinds guideKind = getGuideKind((TraversePlanTypes) plan.type());
    List<Guide<?, ?>> guides = findGuides(guideKind, objectHandleClass, plan.tid());
    if (guides.isEmpty()) {
      return null;
    }
    if (guides.size() > 1) {
      throw new UnsupportedOperationException("Not implemented");
    }
    return switch (guideKind) {
      case Mapper0, Mover0, MapperOfMoving0 -> new CallGuide0PlanImpl((Guide0<?, ?>) guides.get(0));
      case Mapper1, Mover1, MapperOfMoving1 -> new CallGuide1PlanImpl((Guide1<?, ?, ?>) guides.get(0));
      case Mapper2, Mover2, MapperOfMoving2 -> new CallGuide2PlanImpl((Guide2<?, ?, ?, ?>) guides.get(0));
      case Mapper3, Mover3, MapperOfMoving3 -> new CallGuide3PlanImpl((Guide3<?, ?, ?, ?, ?>) guides.get(0));
      default -> throw new UnsupportedOperationException("Not implemented");
    };
  }

  private List<Guide<?, ?>> findGuides(GuideKind kind, Class<?> objectHandleClass, String tid) {
    return guideRegistry.findGuides(kind, objectHandleClass, tid);
  }

  private GuideKinds getGuideKind(TraversePlanTypes planType) {
    return switch (planType) {
      case MapObjectHandleThruTransition0 -> GuideKinds.Mapper0;
      case MapObjectHandleThruTransition1 -> GuideKinds.Mapper1;
      case MapObjectHandleThruTransition2 -> GuideKinds.Mapper2;
      case MapObjectHandleThruTransition3 -> GuideKinds.Mapper3;
      case MoveObjectHandleThruTransition0 -> GuideKinds.Mover0;
      case MoveObjectHandleThruTransition1 -> GuideKinds.Mover1;
      case MoveObjectHandleThruTransition2 -> GuideKinds.Mover2;
      case MoveObjectHandleThruTransition3 -> GuideKinds.Mover3;
      case MapOfMovingObjectHandleThruTransition0 -> GuideKinds.MapperOfMoving0;
      case MapOfMovingObjectHandleThruTransition1 -> GuideKinds.MapperOfMoving1;
      case MapOfMovingObjectHandleThruTransition2 -> GuideKinds.MapperOfMoving2;
      case MapOfMovingObjectHandleThruTransition3 -> GuideKinds.MapperOfMoving3;
      default -> throw new UnsupportedOperationException("Not implemented");
    };
  }
}
