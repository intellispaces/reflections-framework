package tech.intellispacesframework.core.system;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.guide.Guide;
import tech.intellispacesframework.core.guide.GuideKind;
import tech.intellispacesframework.core.guide.GuideKinds;
import tech.intellispacesframework.core.guide.n0.Guide0;
import tech.intellispacesframework.core.guide.n1.Guide1;
import tech.intellispacesframework.core.object.ObjectFunctions;
import tech.intellispacesframework.core.traverse.ActualPlan;
import tech.intellispacesframework.core.traverse.CallGuide0PlanImpl;
import tech.intellispacesframework.core.traverse.CallGuide1PlanImpl;
import tech.intellispacesframework.core.traverse.MapObjectHandleThruTransition0Plan;
import tech.intellispacesframework.core.traverse.MapObjectHandleThruTransition0PlanImpl;
import tech.intellispacesframework.core.traverse.MapObjectHandleThruTransition1Plan;
import tech.intellispacesframework.core.traverse.MapObjectHandleThruTransition1PlanImpl;
import tech.intellispacesframework.core.traverse.MoveObjectHandleThruTransition0Plan;
import tech.intellispacesframework.core.traverse.MoveObjectHandleThruTransition0PlanImpl;
import tech.intellispacesframework.core.traverse.MoveObjectHandleThruTransition1Plan;
import tech.intellispacesframework.core.traverse.MoveObjectHandleThruTransition1PlanImpl;
import tech.intellispacesframework.core.traverse.ObjectHandleTraversePlan;
import tech.intellispacesframework.core.traverse.TraverseAnalyzer;
import tech.intellispacesframework.core.traverse.TraversePlanTypes;

import java.util.ArrayList;
import java.util.List;

public class TraverseAnalyzerImpl implements TraverseAnalyzer {
  private final ModuleGuideRegistry moduleGuideRegistry;
  private final EmbeddedGuideRegistry embeddedGuideRegistry;

  public TraverseAnalyzerImpl(ModuleGuideRegistry moduleGuideRegistry, EmbeddedGuideRegistry embeddedGuideRegistry) {
    this.moduleGuideRegistry = moduleGuideRegistry;
    this.embeddedGuideRegistry = embeddedGuideRegistry;
  }

  @Override
  public MapObjectHandleThruTransition0Plan buildTraversePlanMapObjectHandleThruTransition0(
      Class<?> objectHandleClass, String tid
  ) {
    MapObjectHandleThruTransition0Plan declarativePlan = new MapObjectHandleThruTransition0PlanImpl(
        objectHandleClass, tid);
    setActualPlan(declarativePlan, objectHandleClass);
    return declarativePlan;
  }

  @Override
  public MapObjectHandleThruTransition1Plan buildTraversePlanMapObjectHandleThruTransition1(
      Class<?> objectHandleClass, String tid
  ) {
    MapObjectHandleThruTransition1Plan declarativePlan = new MapObjectHandleThruTransition1PlanImpl(
        objectHandleClass, tid);
    setActualPlan(declarativePlan, objectHandleClass);
    return declarativePlan;
  }

  @Override
  public MoveObjectHandleThruTransition0Plan buildTraversePlanMoveObjectHandleThruTransition0(
      Class<?> objectHandleClass, String tid
  ) {
    MoveObjectHandleThruTransition0Plan declarativePlan = new MoveObjectHandleThruTransition0PlanImpl(
        objectHandleClass, tid);
    setActualPlan(declarativePlan, objectHandleClass);
    return declarativePlan;
  }

  @Override
  public MoveObjectHandleThruTransition1Plan buildTraversePlanMoveObjectHandleThruTransition1(
      Class<?> objectHandleClass, String tid
  ) {
    MoveObjectHandleThruTransition1Plan declarativePlan = new MoveObjectHandleThruTransition1PlanImpl(
        objectHandleClass, tid);
    setActualPlan(declarativePlan, objectHandleClass);
    return declarativePlan;
  }

  private void setActualPlan(ObjectHandleTraversePlan plan, Class<?> objectHandleClass) {
    ActualPlan actualPlan = getActualTraversePlan(plan, objectHandleClass);
    if (actualPlan != null) {
      plan.addActualPlan(objectHandleClass, actualPlan);
    }
  }

  @Override
  public ActualPlan getActualTraversePlan(ObjectHandleTraversePlan plan, Class<?> objectHandleClass) {
    if (!ObjectFunctions.isObjectHandleClass(objectHandleClass)) {
      throw UnexpectedViolationException.withMessage("Traverse plan of type {} expected object handle to input",
          plan.type());
    }
    if (objectHandleClass != plan.objectHandleClass() && !plan.objectHandleClass().isAssignableFrom(objectHandleClass)) {
      throw UnexpectedViolationException.withMessage("Traverse plan of type {} expected object handle of class {} " +
          "or it subclass", plan.objectHandleClass());
    }

    ActualPlan actualPlan = plan.getActualPlan(plan.objectHandleClass());
    if (actualPlan == null) {
      actualPlan = plan.getActualPlan(objectHandleClass);
    }
    if (actualPlan == null) {
      actualPlan = buildActualTraversePlan(plan, plan.objectHandleClass());
      if (actualPlan != null) {
        plan.addActualPlan(plan.objectHandleClass(), actualPlan);
      } else {
        actualPlan = buildActualTraversePlan(plan, objectHandleClass);
        if (actualPlan != null) {
          plan.addActualPlan(objectHandleClass, actualPlan);
        }
      }
    }
    return actualPlan;
  }

  private ActualPlan buildActualTraversePlan(ObjectHandleTraversePlan plan, Class<?> objectHandleClass) {
    GuideKinds guideKind = getGuideKind((TraversePlanTypes) plan.type());
    List<Guide<?, ?>> guides = findGuides(objectHandleClass, guideKind, plan.tid());
    if (guides.isEmpty()) {
      return null;
    }
    if (guides.size() > 1) {
      throw new UnsupportedOperationException("Not implemented");
    }
    return switch (guideKind) {
      case Mapper0, Mover0 -> new CallGuide0PlanImpl((Guide0<?, ?>) guides.get(0));
      case Mapper1, Mover1 -> new CallGuide1PlanImpl((Guide1<?, ?, ?>) guides.get(0));
      default -> throw new UnsupportedOperationException("Not implemented");
    };
  }

  private List<Guide<?, ?>> findGuides(Class<?> objectHandleClass, GuideKind kind, String tid) {
    var guides = new ArrayList<Guide<?, ?>>();
    Guide<?, ?> embeddedGuide = embeddedGuideRegistry.getGuide(objectHandleClass, kind, tid);
    if (embeddedGuide != null) {
      guides.add(embeddedGuide);
    }
    guides.addAll(moduleGuideRegistry.findGuides(kind, tid));
    return guides;
  }

  private GuideKinds getGuideKind(TraversePlanTypes planType) {
    return switch (planType) {
      case MapObjectHandleThruTransition0 -> GuideKinds.Mapper0;
      case MapObjectHandleThruTransition1 -> GuideKinds.Mapper1;
      case MoveObjectHandleThruTransition0 -> GuideKinds.Mover0;
      case MoveObjectHandleThruTransition1 -> GuideKinds.Mover1;
      default -> throw new UnsupportedOperationException("Not implemented");
    };
  }
}
