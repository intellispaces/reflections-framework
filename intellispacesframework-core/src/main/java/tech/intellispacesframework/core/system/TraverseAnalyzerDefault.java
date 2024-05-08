package tech.intellispacesframework.core.system;

import tech.intellispacesframework.core.annotation.Mover;
import tech.intellispacesframework.core.guide.EmbeddedMover1;
import tech.intellispacesframework.core.guide.n1.Guide1;
import tech.intellispacesframework.core.space.transition.TransitionFunctions;
import tech.intellispacesframework.core.traverseplan.CallGuide1TraversePlan;
import tech.intellispacesframework.core.traverseplan.CallGuide1TraversePlanDefault;
import tech.intellispacesframework.core.traverseplan.MoveObjectHandleThruTransition1TraversePlan;
import tech.intellispacesframework.core.traverseplan.MoveObjectHandleThruTransition1TraversePlanDefault;

import java.lang.reflect.Method;

class TraverseAnalyzerDefault implements TraverseAnalyzer {

  @Override
  public MoveObjectHandleThruTransition1TraversePlan buildTraversePlanMoveObjectHandleThruTransition1(Class<?> objectHandleClass, String tid) {
    Guide1<?, ?, ?> guide = findGuide(objectHandleClass, tid);
    CallGuide1TraversePlan delegate = null;
    if (guide != null) {
      delegate = new CallGuide1TraversePlanDefault(guide);
    }
    return new MoveObjectHandleThruTransition1TraversePlanDefault(objectHandleClass, tid, delegate);
  }

  private Guide1<?, ?, ?> findGuide(Class<?> objectHandleClass, String cid) {
    return findEmbeddedGuide(objectHandleClass, cid);
  }

  private Guide1<?, ?, ?> findEmbeddedGuide(Class<?> objectHandleClass, String cid) {
    for (Method method : objectHandleClass.getDeclaredMethods()) {
      Mover moverAnnotation = method.getAnnotation(Mover.class);
      if (moverAnnotation != null) {
        if (!moverAnnotation.value().isEmpty()) {
          if (cid.equals(moverAnnotation.value())) {
            return new EmbeddedMover1<>(objectHandleClass, method);
          }
        } else {
          String guideCid = TransitionFunctions.getTransitionIdOfEmbeddedGuide(objectHandleClass, method);
          if (cid.equals(guideCid)) {
            return new EmbeddedMover1<>(objectHandleClass, method);
          }
        }
      }
    }
    return null;
  }
}
