package tech.intellispacesframework.core.system;

import tech.intellispacesframework.core.annotation.Mover;
import tech.intellispacesframework.core.guide.EmbeddedMover1;
import tech.intellispacesframework.core.guide.n1.Guide1;
import tech.intellispacesframework.core.object.ObjectFunctions;
import tech.intellispacesframework.core.space.transition.TransitionFunctions;
import tech.intellispacesframework.core.traverse.*;

import java.lang.reflect.Method;
import java.util.Optional;

class TraverseAnalyzerDefault implements TraverseAnalyzer {

  @Override
  public MoveObjectHandleThruTransition1GeneralTraversePlan buildTraversePlanMoveObjectHandleThruTransition1(
      Class<?> objectHandleClass, String tid
  ) {
    var traversePlan = new MoveObjectHandleThruTransition1GeneralTraversePlanDefault(objectHandleClass, tid);
    Optional<EffectiveTraversePlan> effectiveTaskPlan = buildEffectiveTaskPlanFor(traversePlan, objectHandleClass);
    effectiveTaskPlan.ifPresent(etp -> traversePlan.setEffectiveTaskPlan(objectHandleClass, etp));
    return traversePlan;
  }

  @Override
  public Optional<EffectiveTraversePlan> buildEffectiveTaskPlanFor(
      MoveObjectHandleThruTransition1GeneralTraversePlan traversePlan, Class<?> objectHandleClass
  ) {
    Guide1<?, ?, ?> guide = findGuide(objectHandleClass, traversePlan.tid());
    CallGuide1TraversePlan delegatedTaskPlan = null;
    if (guide != null) {
      delegatedTaskPlan = new CallGuide1TraversePlanDefault(guide);
    }
    return Optional.ofNullable(delegatedTaskPlan);
  }

  private Guide1<?, ?, ?> findGuide(Class<?> objectHandleClass, String tid) {
    return findEmbeddedGuide(objectHandleClass, tid);
  }

  private Guide1<?, ?, ?> findEmbeddedGuide(Class<?> objectHandleClass, String tid) {
    Class<?> actualObjectHandleClass = ObjectFunctions.getObjectHandleClass(objectHandleClass);
    if (actualObjectHandleClass == null) {
      return null;
    }
    for (Method method : actualObjectHandleClass.getDeclaredMethods()) {
      Mover moverAnnotation = method.getAnnotation(Mover.class);
      if (moverAnnotation != null) {
        if (!moverAnnotation.value().isEmpty()) {
          if (tid.equals(moverAnnotation.value())) {
            return new EmbeddedMover1<>(actualObjectHandleClass, method);
          }
        } else {
          String guideTid = TransitionFunctions.getTransitionIdOfEmbeddedGuide(actualObjectHandleClass, method);
          if (tid.equals(guideTid)) {
            return new EmbeddedMover1<>(actualObjectHandleClass, method);
          }
        }
      }
    }
    return null;
  }
}
