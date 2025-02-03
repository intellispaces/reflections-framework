package tech.intellispaces.jaquarius.engine.impl;

import tech.intellispaces.commons.base.exception.NotImplementedExceptions;
import tech.intellispaces.commons.base.exception.UnexpectedExceptions;
import tech.intellispaces.jaquarius.guide.Guide;
import tech.intellispaces.jaquarius.guide.GuideKind;
import tech.intellispaces.jaquarius.guide.GuideKinds;
import tech.intellispaces.jaquarius.guide.n0.Guide0;
import tech.intellispaces.jaquarius.guide.n1.Guide1;
import tech.intellispaces.jaquarius.guide.n2.Guide2;
import tech.intellispaces.jaquarius.guide.n3.Guide3;
import tech.intellispaces.jaquarius.guide.n4.Guide4;
import tech.intellispaces.jaquarius.object.reference.ObjectHandleFunctions;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.traverse.plan.CallGuide0PlanImpl;
import tech.intellispaces.jaquarius.traverse.plan.CallGuide1PlanImpl;
import tech.intellispaces.jaquarius.traverse.plan.CallGuide2PlanImpl;
import tech.intellispaces.jaquarius.traverse.plan.CallGuide3PlanImpl;
import tech.intellispaces.jaquarius.traverse.plan.CallGuide4PlanImpl;
import tech.intellispaces.jaquarius.traverse.plan.ExecutionTraversePlan;
import tech.intellispaces.jaquarius.traverse.plan.MapObjectHandleThruChannel0Plan;
import tech.intellispaces.jaquarius.traverse.plan.MapObjectHandleThruChannel0PlanImpl;
import tech.intellispaces.jaquarius.traverse.plan.MapObjectHandleThruChannel1Plan;
import tech.intellispaces.jaquarius.traverse.plan.MapObjectHandleThruChannel1PlanImpl;
import tech.intellispaces.jaquarius.traverse.plan.MapObjectHandleThruChannel2Plan;
import tech.intellispaces.jaquarius.traverse.plan.MapObjectHandleThruChannel2PlanImpl;
import tech.intellispaces.jaquarius.traverse.plan.MapObjectHandleThruChannel3Plan;
import tech.intellispaces.jaquarius.traverse.plan.MapObjectHandleThruChannel3PlanImpl;
import tech.intellispaces.jaquarius.traverse.plan.MapOfMovingObjectHandleThruChannel0Plan;
import tech.intellispaces.jaquarius.traverse.plan.MapOfMovingObjectHandleThruChannel0PlanImpl;
import tech.intellispaces.jaquarius.traverse.plan.MapOfMovingObjectHandleThruChannel1Plan;
import tech.intellispaces.jaquarius.traverse.plan.MapOfMovingObjectHandleThruChannel1PlanImpl;
import tech.intellispaces.jaquarius.traverse.plan.MapOfMovingObjectHandleThruChannel2Plan;
import tech.intellispaces.jaquarius.traverse.plan.MapOfMovingObjectHandleThruChannel2PlanImpl;
import tech.intellispaces.jaquarius.traverse.plan.MapOfMovingObjectHandleThruChannel3Plan;
import tech.intellispaces.jaquarius.traverse.plan.MapOfMovingObjectHandleThruChannel3PlanImpl;
import tech.intellispaces.jaquarius.traverse.plan.MapOfMovingObjectHandleThruChannel4Plan;
import tech.intellispaces.jaquarius.traverse.plan.MapOfMovingObjectHandleThruChannel4PlanImpl;
import tech.intellispaces.jaquarius.traverse.plan.MoveObjectHandleThruChannel0Plan;
import tech.intellispaces.jaquarius.traverse.plan.MoveObjectHandleThruChannel0PlanImpl;
import tech.intellispaces.jaquarius.traverse.plan.MoveObjectHandleThruChannel1Plan;
import tech.intellispaces.jaquarius.traverse.plan.MoveObjectHandleThruChannel1PlanImpl;
import tech.intellispaces.jaquarius.traverse.plan.MoveObjectHandleThruChannel2Plan;
import tech.intellispaces.jaquarius.traverse.plan.MoveObjectHandleThruChannel2PlanImpl;
import tech.intellispaces.jaquarius.traverse.plan.MoveObjectHandleThruChannel3Plan;
import tech.intellispaces.jaquarius.traverse.plan.MoveObjectHandleThruChannel3PlanImpl;
import tech.intellispaces.jaquarius.traverse.plan.ObjectHandleTraversePlan;
import tech.intellispaces.jaquarius.traverse.plan.TraversePlanType;
import tech.intellispaces.jaquarius.traverse.plan.TraversePlanTypes;

import java.util.List;
import java.util.stream.Collectors;

class TraverseAnalyzer implements tech.intellispaces.jaquarius.traverse.plan.TraverseAnalyzer {
  private final GuideRegistry guideRegistry;

  public TraverseAnalyzer(GuideRegistry guideRegistry) {
    this.guideRegistry = guideRegistry;
  }

  @Override
  public MapObjectHandleThruChannel0Plan buildMapObjectHandleThruChannel0Plan(
    Class<?> sourceClass, String cid, ObjectReferenceForm targetForm
  ) {
    MapObjectHandleThruChannel0Plan declarativePlan = new MapObjectHandleThruChannel0PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapObjectHandleThruChannel1Plan buildMapObjectHandleThruChannel1Plan(
    Class<?> sourceClass, String cid, ObjectReferenceForm targetForm
  ) {
    MapObjectHandleThruChannel1Plan declarativePlan = new MapObjectHandleThruChannel1PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapObjectHandleThruChannel2Plan buildMapObjectHandleThruChannel2Plan(
    Class<?> sourceClass, String cid, ObjectReferenceForm targetForm
  ) {
    MapObjectHandleThruChannel2Plan declarativePlan = new MapObjectHandleThruChannel2PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapObjectHandleThruChannel3Plan buildMapObjectHandleThruChannel3Plan(
    Class<?> sourceClass, String cid, ObjectReferenceForm targetForm
  ) {
    MapObjectHandleThruChannel3Plan declarativePlan = new MapObjectHandleThruChannel3PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MoveObjectHandleThruChannel0Plan buildMoveObjectHandleThruChannel0Plan(
    Class<?> sourceClass, String cid, ObjectReferenceForm targetForm
  ) {
    MoveObjectHandleThruChannel0Plan declarativePlan = new MoveObjectHandleThruChannel0PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MoveObjectHandleThruChannel1Plan buildMoveObjectHandleThruChannel1Plan(
    Class<?> sourceClass, String cid, ObjectReferenceForm targetForm
  ) {
    MoveObjectHandleThruChannel1Plan declarativePlan = new MoveObjectHandleThruChannel1PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MoveObjectHandleThruChannel2Plan buildMoveObjectHandleThruChannel2Plan(
    Class<?> sourceClass, String cid, ObjectReferenceForm targetForm
  ) {
    MoveObjectHandleThruChannel2Plan declarativePlan = new MoveObjectHandleThruChannel2PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MoveObjectHandleThruChannel3Plan buildMoveObjectHandleThruChannel3Plan(
    Class<?> sourceClass, String cid, ObjectReferenceForm targetForm
  ) {
    MoveObjectHandleThruChannel3Plan declarativePlan = new MoveObjectHandleThruChannel3PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingObjectHandleThruChannel0Plan buildMapOfMovingObjectHandleThruChannel0Plan(
      Class<?> sourceClass, String cid, ObjectReferenceForm targetForm
  ) {
    MapOfMovingObjectHandleThruChannel0Plan declarativePlan = new MapOfMovingObjectHandleThruChannel0PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingObjectHandleThruChannel1Plan buildMapOfMovingObjectHandleThruChannel1Plan(
      Class<?> sourceClass, String cid, ObjectReferenceForm targetForm
  ) {
    MapOfMovingObjectHandleThruChannel1Plan declarativePlan = new MapOfMovingObjectHandleThruChannel1PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingObjectHandleThruChannel2Plan buildMapOfMovingObjectHandleThruChannel2Plan(
      Class<?> sourceClass, String cid, ObjectReferenceForm targetForm
  ) {
    MapOfMovingObjectHandleThruChannel2Plan declarativePlan = new MapOfMovingObjectHandleThruChannel2PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingObjectHandleThruChannel3Plan buildMapOfMovingObjectHandleThruChannel3Plan(
      Class<?> sourceClass, String cid, ObjectReferenceForm targetForm
  ) {
    MapOfMovingObjectHandleThruChannel3Plan declarativePlan = new MapOfMovingObjectHandleThruChannel3PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingObjectHandleThruChannel4Plan buildMapOfMovingObjectHandleThruChannel4Plan(
      Class<?> sourceClass, String cid, ObjectReferenceForm targetForm
  ) {
    MapOfMovingObjectHandleThruChannel4Plan declarativePlan = new MapOfMovingObjectHandleThruChannel4PlanImpl(sourceClass, cid);
    defineExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  private void defineExecutionPlan(
      ObjectHandleTraversePlan plan, Class<?> objectHandleClass, ObjectReferenceForm targetForm
  ) {
    ExecutionTraversePlan executionPlan = getExecutionPlan(plan, objectHandleClass, targetForm);
    if (executionPlan != null) {
      plan.addExecutionPlan(objectHandleClass, executionPlan);
    }
  }

  @Override
  public ExecutionTraversePlan getExecutionPlan(
      ObjectHandleTraversePlan plan, Class<?> sourceClass, ObjectReferenceForm targetForm
  ) {
    ExecutionTraversePlan executionPlan = plan.getExecutionPlan(sourceClass);
    if (executionPlan != null) {
      return executionPlan;
    }

    if (!ObjectHandleFunctions.isObjectHandleClass(sourceClass)) {
      throw UnexpectedExceptions.withMessage("Traverse plan of type {0} expected object handle to input",
          plan.type());
    }
    Class<?> objectHandleClass = ObjectHandleFunctions.getObjectHandleClass(sourceClass);
    executionPlan = plan.getExecutionPlan(objectHandleClass);
    if (executionPlan != null) {
      return executionPlan;
    }

    if (sourceClass != plan.objectHandleClass() && !plan.objectHandleClass().isAssignableFrom(sourceClass)) {
      throw UnexpectedExceptions.withMessage("Traverse plan of type {0} expected object handle of class {1} " +
          "or it subclass", plan.objectHandleClass());
    }
    executionPlan = plan.getExecutionPlan(plan.objectHandleClass());
    if (executionPlan != null) {
      return executionPlan;
    }

    executionPlan = buildExecutionTraversePlan(plan, sourceClass, targetForm);
    if (executionPlan != null) {
      plan.addExecutionPlan(sourceClass, executionPlan);
    } else {
      executionPlan = buildExecutionTraversePlan(plan, objectHandleClass, targetForm);
      if (executionPlan != null) {
        plan.addExecutionPlan(sourceClass, executionPlan);
        plan.addExecutionPlan(objectHandleClass, executionPlan);
      }
    }
    return executionPlan;
  }

  private ExecutionTraversePlan buildExecutionTraversePlan(
      ObjectHandleTraversePlan plan, Class<?> objectHandleClass, ObjectReferenceForm targetForm
  ) {
    GuideKinds guideKind = getGuideKind(plan.type());
    List<Guide<?, ?>> guides = findGuides(guideKind, objectHandleClass, plan.cid(), targetForm);
    if (guides.isEmpty()) {
      return null;
    }
    if (guides.size() > 1) {
      throw NotImplementedExceptions.withCodeAndMessage("e9iXkw", "Multiple guides are found:\n{0}",
          guides.stream().map(Object::toString).collect(Collectors.joining("\n"))
      );
    }
    return switch (guideKind) {
      case Mapper0, Mover0, MapperOfMoving0 -> new CallGuide0PlanImpl((Guide0<?, ?>) guides.get(0));
      case Mapper1, Mover1, MapperOfMoving1 -> new CallGuide1PlanImpl((Guide1<?, ?, ?>) guides.get(0));
      case Mapper2, Mover2, MapperOfMoving2 -> new CallGuide2PlanImpl((Guide2<?, ?, ?, ?>) guides.get(0));
      case Mapper3, Mover3, MapperOfMoving3 -> new CallGuide3PlanImpl((Guide3<?, ?, ?, ?, ?>) guides.get(0));
      case Mapper4, Mover4, MapperOfMoving4 -> new CallGuide4PlanImpl((Guide4<?, ?, ?, ?, ?, ?>) guides.get(0));
      default -> throw NotImplementedExceptions.withCode("MlgXfQ");
    };
  }

  private List<Guide<?, ?>> findGuides(GuideKind kind, Class<?> objectHandleClass, String cid, ObjectReferenceForm form) {
    return guideRegistry.findGuides(kind, objectHandleClass, cid, form);
  }

  private GuideKinds getGuideKind(TraversePlanType planType) {
    return switch (TraversePlanTypes.from(planType)) {
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
      default -> throw NotImplementedExceptions.withCode("5cYSWA");
    };
  }
}
