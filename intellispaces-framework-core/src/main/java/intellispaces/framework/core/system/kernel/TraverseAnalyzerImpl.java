package intellispaces.framework.core.system.kernel;

import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.guide.Guide;
import intellispaces.framework.core.guide.GuideForm;
import intellispaces.framework.core.guide.GuideKind;
import intellispaces.framework.core.guide.GuideKinds;
import intellispaces.framework.core.guide.n0.Guide0;
import intellispaces.framework.core.guide.n1.Guide1;
import intellispaces.framework.core.guide.n2.Guide2;
import intellispaces.framework.core.guide.n3.Guide3;
import intellispaces.framework.core.guide.n4.Guide4;
import intellispaces.framework.core.object.ObjectFunctions;
import intellispaces.framework.core.traverse.plan.CallGuide0PlanImpl;
import intellispaces.framework.core.traverse.plan.CallGuide1PlanImpl;
import intellispaces.framework.core.traverse.plan.CallGuide2PlanImpl;
import intellispaces.framework.core.traverse.plan.CallGuide3PlanImpl;
import intellispaces.framework.core.traverse.plan.CallGuide4PlanImpl;
import intellispaces.framework.core.traverse.plan.ExecutionTraversePlan;
import intellispaces.framework.core.traverse.plan.MapObjectHandleThruChannel0Plan;
import intellispaces.framework.core.traverse.plan.MapObjectHandleThruChannel0PlanImpl;
import intellispaces.framework.core.traverse.plan.MapObjectHandleThruChannel1Plan;
import intellispaces.framework.core.traverse.plan.MapObjectHandleThruChannel1PlanImpl;
import intellispaces.framework.core.traverse.plan.MapObjectHandleThruChannel2Plan;
import intellispaces.framework.core.traverse.plan.MapObjectHandleThruChannel2PlanImpl;
import intellispaces.framework.core.traverse.plan.MapObjectHandleThruChannel3Plan;
import intellispaces.framework.core.traverse.plan.MapObjectHandleThruChannel3PlanImpl;
import intellispaces.framework.core.traverse.plan.MapOfMovingObjectHandleThruChannel0Plan;
import intellispaces.framework.core.traverse.plan.MapOfMovingObjectHandleThruChannel0PlanImpl;
import intellispaces.framework.core.traverse.plan.MapOfMovingObjectHandleThruChannel1Plan;
import intellispaces.framework.core.traverse.plan.MapOfMovingObjectHandleThruChannel1PlanImpl;
import intellispaces.framework.core.traverse.plan.MapOfMovingObjectHandleThruChannel2Plan;
import intellispaces.framework.core.traverse.plan.MapOfMovingObjectHandleThruChannel2PlanImpl;
import intellispaces.framework.core.traverse.plan.MapOfMovingObjectHandleThruChannel3Plan;
import intellispaces.framework.core.traverse.plan.MapOfMovingObjectHandleThruChannel3PlanImpl;
import intellispaces.framework.core.traverse.plan.MapOfMovingObjectHandleThruChannel4Plan;
import intellispaces.framework.core.traverse.plan.MapOfMovingObjectHandleThruChannel4PlanImpl;
import intellispaces.framework.core.traverse.plan.MoveObjectHandleThruChannel0Plan;
import intellispaces.framework.core.traverse.plan.MoveObjectHandleThruChannel0PlanImpl;
import intellispaces.framework.core.traverse.plan.MoveObjectHandleThruChannel1Plan;
import intellispaces.framework.core.traverse.plan.MoveObjectHandleThruChannel1PlanImpl;
import intellispaces.framework.core.traverse.plan.MoveObjectHandleThruChannel2Plan;
import intellispaces.framework.core.traverse.plan.MoveObjectHandleThruChannel2PlanImpl;
import intellispaces.framework.core.traverse.plan.MoveObjectHandleThruChannel3Plan;
import intellispaces.framework.core.traverse.plan.MoveObjectHandleThruChannel3PlanImpl;
import intellispaces.framework.core.traverse.plan.ObjectHandleTraversePlan;
import intellispaces.framework.core.traverse.plan.TraverseAnalyzer;
import intellispaces.framework.core.traverse.plan.TraversePlanTypes;

import java.util.List;

class TraverseAnalyzerImpl implements TraverseAnalyzer {
  private final GuideRegistry guideRegistry;

  public TraverseAnalyzerImpl(GuideRegistry guideRegistry) {
    this.guideRegistry = guideRegistry;
  }

  @Override
  public MapObjectHandleThruChannel0Plan buildMapObjectHandleThruChannel0Plan(
    Class<?> sourceClass, String cid, GuideForm guideForm
  ) {
    MapObjectHandleThruChannel0Plan declarativePlan = new MapObjectHandleThruChannel0PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, guideForm);
    return declarativePlan;
  }

  @Override
  public MapObjectHandleThruChannel1Plan buildMapObjectHandleThruChannel1Plan(
    Class<?> sourceClass, String cid, GuideForm guideForm
  ) {
    MapObjectHandleThruChannel1Plan declarativePlan = new MapObjectHandleThruChannel1PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, guideForm);
    return declarativePlan;
  }

  @Override
  public MapObjectHandleThruChannel2Plan buildMapObjectHandleThruChannel2Plan(
    Class<?> sourceClass, String cid, GuideForm guideForm
  ) {
    MapObjectHandleThruChannel2Plan declarativePlan = new MapObjectHandleThruChannel2PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, guideForm);
    return declarativePlan;
  }

  @Override
  public MapObjectHandleThruChannel3Plan buildMapObjectHandleThruChannel3Plan(
    Class<?> sourceClass, String cid, GuideForm guideForm
  ) {
    MapObjectHandleThruChannel3Plan declarativePlan = new MapObjectHandleThruChannel3PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, guideForm);
    return declarativePlan;
  }

  @Override
  public MoveObjectHandleThruChannel0Plan buildMoveObjectHandleThruChannel0Plan(
    Class<?> sourceClass, String cid, GuideForm guideForm
  ) {
    MoveObjectHandleThruChannel0Plan declarativePlan = new MoveObjectHandleThruChannel0PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, guideForm);
    return declarativePlan;
  }

  @Override
  public MoveObjectHandleThruChannel1Plan buildMoveObjectHandleThruChannel1Plan(
    Class<?> sourceClass, String cid, GuideForm guideForm
  ) {
    MoveObjectHandleThruChannel1Plan declarativePlan = new MoveObjectHandleThruChannel1PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, guideForm);
    return declarativePlan;
  }

  @Override
  public MoveObjectHandleThruChannel2Plan buildMoveObjectHandleThruChannel2Plan(
    Class<?> sourceClass, String cid, GuideForm guideForm
  ) {
    MoveObjectHandleThruChannel2Plan declarativePlan = new MoveObjectHandleThruChannel2PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, guideForm);
    return declarativePlan;
  }

  @Override
  public MoveObjectHandleThruChannel3Plan buildMoveObjectHandleThruChannel3Plan(
    Class<?> sourceClass, String cid, GuideForm guideForm
  ) {
    MoveObjectHandleThruChannel3Plan declarativePlan = new MoveObjectHandleThruChannel3PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, guideForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingObjectHandleThruChannel0Plan buildMapOfMovingObjectHandleThruChannel0Plan(
      Class<?> sourceClass, String cid, GuideForm guideForm
  ) {
    MapOfMovingObjectHandleThruChannel0Plan declarativePlan = new MapOfMovingObjectHandleThruChannel0PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, guideForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingObjectHandleThruChannel1Plan buildMapOfMovingObjectHandleThruChannel1Plan(
      Class<?> sourceClass, String cid, GuideForm guideForm
  ) {
    MapOfMovingObjectHandleThruChannel1Plan declarativePlan = new MapOfMovingObjectHandleThruChannel1PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, guideForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingObjectHandleThruChannel2Plan buildMapOfMovingObjectHandleThruChannel2Plan(
      Class<?> sourceClass, String cid, GuideForm guideForm
  ) {
    MapOfMovingObjectHandleThruChannel2Plan declarativePlan = new MapOfMovingObjectHandleThruChannel2PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, guideForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingObjectHandleThruChannel3Plan buildMapOfMovingObjectHandleThruChannel3Plan(
      Class<?> sourceClass, String cid, GuideForm guideForm
  ) {
    MapOfMovingObjectHandleThruChannel3Plan declarativePlan = new MapOfMovingObjectHandleThruChannel3PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, guideForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingObjectHandleThruChannel4Plan buildMapOfMovingObjectHandleThruChannel4Plan(
      Class<?> sourceClass, String cid, GuideForm guideForm
  ) {
    MapOfMovingObjectHandleThruChannel4Plan declarativePlan = new MapOfMovingObjectHandleThruChannel4PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, guideForm);
    return declarativePlan;
  }

  private void defineExecutionPlan(
      ObjectHandleTraversePlan plan, Class<?> objectHandleClass, GuideForm guideForm
  ) {
    ExecutionTraversePlan executionPlan = getExecutionPlan(plan, objectHandleClass, guideForm);
    if (executionPlan != null) {
      plan.addExecutionPlan(objectHandleClass, executionPlan);
    }
  }

  @Override
  public ExecutionTraversePlan getExecutionPlan(
      ObjectHandleTraversePlan plan, Class<?> sourceClass, GuideForm guideForm
  ) {
    ExecutionTraversePlan executionPlan = plan.getExecutionPlan(sourceClass);
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

    executionPlan = buildExecutionTraversePlan(plan, sourceClass, guideForm);
    if (executionPlan != null) {
      plan.addExecutionPlan(sourceClass, executionPlan);
    } else {
      executionPlan = buildExecutionTraversePlan(plan, objectHandleClass, guideForm);
      if (executionPlan != null) {
        plan.addExecutionPlan(sourceClass, executionPlan);
        plan.addExecutionPlan(objectHandleClass, executionPlan);
      }
    }
    return executionPlan;
  }

  private ExecutionTraversePlan buildExecutionTraversePlan(
      ObjectHandleTraversePlan plan, Class<?> objectHandleClass, GuideForm guideForm
  ) {
    GuideKinds guideKind = getGuideKind((TraversePlanTypes) plan.type());
    List<Guide<?, ?>> guides = findGuides(guideKind, objectHandleClass, plan.cid(), guideForm);
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
      case Mapper4, Mover4, MapperOfMoving4 -> new CallGuide4PlanImpl((Guide4<?, ?, ?, ?, ?, ?>) guides.get(0));
      default -> throw new UnsupportedOperationException("Not implemented");
    };
  }

  private List<Guide<?, ?>> findGuides(GuideKind kind, Class<?> objectHandleClass, String cid, GuideForm guideForm) {
    return guideRegistry.findGuides(kind, objectHandleClass, cid, guideForm);
  }

  private GuideKinds getGuideKind(TraversePlanTypes planType) {
    return switch (planType) {
      case MapObjectHandleThruChannel0 -> GuideKinds.Mapper0;
      case MapObjectHandleThruChannel1 -> GuideKinds.Mapper1;
      case MapObjectHandleThruChannel2 -> GuideKinds.Mapper2;
      case MapObjectHandleThruChannel3 -> GuideKinds.Mapper3;
      case MoveObjectHandleThruChannel0 -> GuideKinds.Mover0;
      case MoveObjectHandleThruChannel1 -> GuideKinds.Mover1;
      case MoveObjectHandleThruChannel2 -> GuideKinds.Mover2;
      case MoveObjectHandleThruChannel3 -> GuideKinds.Mover3;
      case MapOfMovingObjectHandleThruChannel0 -> GuideKinds.MapperOfMoving0;
      case MapOfMovingObjectHandleThruChannel1 -> GuideKinds.MapperOfMoving1;
      case MapOfMovingObjectHandleThruChannel2 -> GuideKinds.MapperOfMoving2;
      case MapOfMovingObjectHandleThruChannel3 -> GuideKinds.MapperOfMoving3;
      case MapOfMovingObjectHandleThruChannel4 -> GuideKinds.MapperOfMoving4;
      default -> throw new UnsupportedOperationException("Not implemented");
    };
  }
}
