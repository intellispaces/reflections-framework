package intellispaces.core.system.shadow;

import intellispaces.core.exception.TraverseException;
import intellispaces.core.guide.n0.Guide0;
import intellispaces.core.guide.n1.Guide1;
import intellispaces.core.object.ObjectFunctions;
import intellispaces.core.traverse.ActualPlan;
import intellispaces.core.traverse.CallGuide0Plan;
import intellispaces.core.traverse.CallGuide1Plan;
import intellispaces.core.traverse.MapObjectHandleThruTransition0Plan;
import intellispaces.core.traverse.MapObjectHandleThruTransition1Plan;
import intellispaces.core.traverse.MapObjectHandleThruTransition2Plan;
import intellispaces.core.traverse.MoveObjectHandleThruTransition0Plan;
import intellispaces.core.traverse.MoveObjectHandleThruTransition1Plan;
import intellispaces.core.traverse.MoveObjectHandleThruTransition2Plan;
import intellispaces.core.traverse.TraverseAnalyzer;
import intellispaces.core.traverse.TraverseExecutor;

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
    Class<?> objectHandleClass = ObjectFunctions.defineObjectHandleClass(source.getClass());
    ActualPlan actualPlan = traverseAnalyzer.getActualTraversePlan(plan, objectHandleClass);
    if (actualPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object handle of class {} through " +
              "transition {}. Suitable guide has not been found", objectHandleClass.getCanonicalName(), plan.tid());
    }
    return actualPlan.execute(source, this);
  }

  @Override
  public Object execute(
      MapObjectHandleThruTransition1Plan plan, Object source, Object qualifier
  ) throws TraverseException {
    Class<?> objectHandleClass = ObjectFunctions.defineObjectHandleClass(source.getClass());
    ActualPlan actualPlan = traverseAnalyzer.getActualTraversePlan(plan, objectHandleClass);
    if (actualPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object handle of class {} through " +
          "transition {}. Suitable guide has not been found", objectHandleClass.getCanonicalName(), plan.tid());
    }
    return actualPlan.execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MapObjectHandleThruTransition2Plan plan, Object source, Object qualifier1, Object qualifier2
  ) throws TraverseException {
    throw new RuntimeException("Not implemented");
  }

  @Override
  public Object execute(MoveObjectHandleThruTransition0Plan plan, Object source) throws TraverseException {
    Class<?> objectHandleClass = ObjectFunctions.defineObjectHandleClass(source.getClass());
    ActualPlan actualPlan = traverseAnalyzer.getActualTraversePlan(plan, objectHandleClass);
    if (actualPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to move object handle of class {} through " +
          "transition {}. Suitable guide has not been found", objectHandleClass.getCanonicalName(), plan.tid());
    }
    return actualPlan.execute(source, this);
  }

  @Override
  public Object execute(
      MoveObjectHandleThruTransition1Plan plan, Object source, Object qualifier
  ) throws TraverseException {
    Class<?> objectHandleClass = ObjectFunctions.defineObjectHandleClass(source.getClass());
    ActualPlan actualPlan = traverseAnalyzer.getActualTraversePlan(plan, objectHandleClass);
    if (actualPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to move object handle of class {} through " +
          "transition {}. Suitable guide has not been found", objectHandleClass.getCanonicalName(), plan.tid());
    }
    return actualPlan.execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MoveObjectHandleThruTransition2Plan plan, Object source, Object qualifier1, Object qualifier2
  ) throws TraverseException {
    throw new RuntimeException("Not implemented");
  }
}
