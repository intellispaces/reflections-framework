package tech.intellispaces.framework.core.system;

import tech.intellispaces.framework.core.traverse.MapObjectHandleThruTransition0Plan;
import tech.intellispaces.framework.core.traverse.MapObjectHandleThruTransition1Plan;
import tech.intellispaces.framework.core.traverse.MoveObjectHandleThruTransition0Plan;
import tech.intellispaces.framework.core.traverse.TraverseExecutor;
import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.guide.n0.Guide0;
import tech.intellispaces.framework.core.guide.n1.Guide1;
import tech.intellispaces.framework.core.traverse.ActualPlan;
import tech.intellispaces.framework.core.traverse.CallGuide0Plan;
import tech.intellispaces.framework.core.traverse.CallGuide1Plan;
import tech.intellispaces.framework.core.traverse.MoveObjectHandleThruTransition1Plan;
import tech.intellispaces.framework.core.traverse.TraverseAnalyzer;

public class TraverseExecutorImpl implements TraverseExecutor {
  private final TraverseAnalyzer traverseAnalyzer;

  public TraverseExecutorImpl(TraverseAnalyzer traverseAnalyzer) {
    this.traverseAnalyzer = traverseAnalyzer;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object execute(CallGuide0Plan plan, Object source) throws TraverseException {
    var guide = (Guide0<Object, Object>) plan.guide();
    return guide.traverse(source);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object execute(CallGuide1Plan plan, Object source, Object qualifier) throws TraverseException {
    var guide = (Guide1<Object, Object, Object>) plan.guide();
    return guide.traverse(source, qualifier);
  }

  @Override
  public Object execute(MapObjectHandleThruTransition0Plan plan, Object source) throws TraverseException {
    ActualPlan actualPlan = traverseAnalyzer.getActualTraversePlan(plan, source.getClass());
    if (actualPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object handle of class {} through " +
              "transition {}", source.getClass().getCanonicalName(), plan.tid());
    }
    return actualPlan.execute(source, this);
  }

  @Override
  public Object execute(
      MapObjectHandleThruTransition1Plan plan, Object source, Object qualifier
  ) throws TraverseException {
    ActualPlan actualPlan = traverseAnalyzer.getActualTraversePlan(plan, source.getClass());
    if (actualPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object handle of class {} through " +
          "transition {}", source.getClass().getCanonicalName(), plan.tid());
    }
    return actualPlan.execute(source, qualifier, this);
  }

  @Override
  public Object execute(MoveObjectHandleThruTransition0Plan plan, Object source) throws TraverseException {
    ActualPlan actualPlan = traverseAnalyzer.getActualTraversePlan(plan, source.getClass());
    if (actualPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to move object handle of class {} through " +
          "transition {}", source.getClass().getCanonicalName(), plan.tid());
    }
    return actualPlan.execute(source, this);
  }

  @Override
  public Object execute(
      MoveObjectHandleThruTransition1Plan plan, Object source, Object qualifier
  ) throws TraverseException {
    ActualPlan actualPlan = traverseAnalyzer.getActualTraversePlan(plan, source.getClass());
    if (actualPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to move object handle of class {} through " +
          "transition {}", source.getClass().getCanonicalName(), plan.tid());
    }
    return actualPlan.execute(source, qualifier, this);
  }
}
