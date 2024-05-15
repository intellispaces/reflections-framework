package tech.intellispacesframework.core.system;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.annotation.Mover;
import tech.intellispacesframework.core.guide.EmbeddedMover0;
import tech.intellispacesframework.core.guide.EmbeddedMover1;
import tech.intellispacesframework.core.guide.Guide;
import tech.intellispacesframework.core.guide.n0.Guide0;
import tech.intellispacesframework.core.guide.n1.Guide1;
import tech.intellispacesframework.core.object.ObjectFunctions;
import tech.intellispacesframework.core.space.transition.TransitionFunctions;
import tech.intellispacesframework.core.traverse.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class TraverseAnalyzerDefault implements TraverseAnalyzer {

  @Override
  public MoveObjectHandleThruTransition0TraversePlan buildTraversePlanMoveObjectHandleThruTransition0(
      Class<?> objectHandleClass, String tid
  ) {
    var traversePlan = new MoveObjectHandleThruTransition0TraversePlanDefault(objectHandleClass, tid);
    Optional<EffectiveTraversePlan> effectiveTaskPlan = buildEffectiveTaskPlanFor(traversePlan, objectHandleClass);
    effectiveTaskPlan.ifPresent(etp -> traversePlan.addEffectiveTaskPlan(objectHandleClass, etp));
    return traversePlan;
  }

  @Override
  public MoveObjectHandleThruTransition1TraversePlan buildTraversePlanMoveObjectHandleThruTransition1(
      Class<?> objectHandleClass, String tid
  ) {
    var traversePlan = new MoveObjectHandleThruTransition1TraversePlanDefault(objectHandleClass, tid);
    Optional<EffectiveTraversePlan> effectiveTaskPlan = buildEffectiveTaskPlanFor(traversePlan, objectHandleClass);
    effectiveTaskPlan.ifPresent(etp -> traversePlan.addEffectiveTaskPlan(objectHandleClass, etp));
    return traversePlan;
  }

  @Override
  public Optional<EffectiveTraversePlan> buildEffectiveTaskPlanFor(
      MoveObjectHandleThruTransition0TraversePlan traversePlan, Class<?> objectHandleClass
  ) {
    var guide = (Guide0<?, ?>) findGuideByTid(objectHandleClass, traversePlan.tid());
    CallGuide0TraversePlan delegatedTaskPlan = null;
    if (guide != null) {
      delegatedTaskPlan = new CallGuide0TraversePlanDefault(guide);
    }
    return Optional.ofNullable(delegatedTaskPlan);
  }

  @Override
  public Optional<EffectiveTraversePlan> buildEffectiveTaskPlanFor(
      MoveObjectHandleThruTransition1TraversePlan traversePlan, Class<?> objectHandleClass
  ) {
    var guide = (Guide1<?, ?, ?>) findGuideByTid(objectHandleClass, traversePlan.tid());
    CallGuide1TraversePlan delegatedTaskPlan = null;
    if (guide != null) {
      delegatedTaskPlan = new CallGuide1TraversePlanDefault(guide);
    }
    return Optional.ofNullable(delegatedTaskPlan);
  }

  private Guide<?, ?> findGuideByTid(Class<?> objectHandleClass, String tid) {
    return findEmbeddedGuideByTid(objectHandleClass, tid);
  }

  private Guide<?, ?> findEmbeddedGuideByTid(Class<?> objectHandleClass, String tid) {
    ObjectHandleDescription description = objectHandleDescriptions.computeIfAbsent(
        objectHandleClass, this::getObjectHandleDescription);
    return description.findEmbeddedGuideByTid(tid);
  }

  private ObjectHandleDescription getObjectHandleDescription(Class<?> objectHandleClass) {
    ObjectHandleDescription description = new ObjectHandleDescription();
    Class<?> actualObjectHandleClass = ObjectFunctions.getObjectHandleClass(objectHandleClass);
    if (actualObjectHandleClass == null) {
      return description;
    }
    for (Method method : actualObjectHandleClass.getDeclaredMethods()) {
      Mover mover = method.getAnnotation(Mover.class);
      if (mover != null) {
        String tid = getMoverTid(method);
        Guide<?, ?> guide = makeEmbeddedMover(actualObjectHandleClass, method);
        description.addEmbeddedGuide(guide, tid);
      }
    }
    return description;
  }

  private String getMoverTid(Method moverMethod) {
    Mover mover = moverMethod.getAnnotation(Mover.class);
    if (!mover.value().isEmpty()) {
      return mover.value();
    }
    return TransitionFunctions.getTransitionIdOfEmbeddedGuide(moverMethod);
  }

  private Guide<?, ?> makeEmbeddedMover(Class<?> objectHandleClass, Method guideMethod) {
    int qualifiersCount = guideMethod.getParameterCount();
    return switch (qualifiersCount) {
      case 0 -> new EmbeddedMover0<>(objectHandleClass, guideMethod);
      case 1 -> new EmbeddedMover1<>(objectHandleClass, guideMethod);
      default -> throw UnexpectedViolationException.withMessage("Unsupported number of guide qualifiers - {}", qualifiersCount);
    };
  }

  private final Map<Class<?>, ObjectHandleDescription> objectHandleDescriptions = new HashMap<>();

  private final class ObjectHandleDescription {
    private final Map<String, Guide<?, ?>> embeddedGuides = new HashMap<>();

    void addEmbeddedGuide(Guide<?, ?> guide, String tid) {
      embeddedGuides.put(tid, guide);
    }

    Guide<?, ?> findEmbeddedGuideByTid(String tid) {
      return embeddedGuides.get(tid);
    }
  }
}
