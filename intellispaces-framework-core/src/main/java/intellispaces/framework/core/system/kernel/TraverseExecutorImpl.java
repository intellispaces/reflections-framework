package intellispaces.framework.core.system.kernel;

import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.n0.Guide0;
import intellispaces.framework.core.guide.n1.Guide1;
import intellispaces.framework.core.guide.n2.Guide2;
import intellispaces.framework.core.object.ObjectFunctions;
import intellispaces.framework.core.traverse.CallGuide0Plan;
import intellispaces.framework.core.traverse.CallGuide1Plan;
import intellispaces.framework.core.traverse.CallGuide2Plan;
import intellispaces.framework.core.traverse.ExecutionPlan;
import intellispaces.framework.core.traverse.MapObjectHandleThruTransition0Plan;
import intellispaces.framework.core.traverse.MapObjectHandleThruTransition1Plan;
import intellispaces.framework.core.traverse.MapObjectHandleThruTransition2Plan;
import intellispaces.framework.core.traverse.MoveObjectHandleThruTransition0Plan;
import intellispaces.framework.core.traverse.MoveObjectHandleThruTransition1Plan;
import intellispaces.framework.core.traverse.MoveObjectHandleThruTransition2Plan;
import intellispaces.framework.core.traverse.TraverseAnalyzer;
import intellispaces.framework.core.traverse.TraverseExecutor;

class TraverseExecutorImpl implements TraverseExecutor {
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
  public Object execute(
      CallGuide1Plan plan, Object source, Object qualifier
  ) throws TraverseException {
    var guide = (Guide1<Object, Object, Object>) plan.guide();
    return guide.traverse(source, qualifier);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object execute(
      CallGuide2Plan plan, Object source, Object qualifier1, Object qualifier2
  ) throws TraverseException {
    var guide = (Guide2<Object, Object, Object, Object>) plan.guide();
    return guide.traverse(source, qualifier1, qualifier2);
  }

  @Override
  public Object execute(MapObjectHandleThruTransition0Plan plan, Object source) throws TraverseException {
    Class<?> objectHandleClass = ObjectFunctions.defineObjectHandleClass(source.getClass());
    ExecutionPlan executionPlan = traverseAnalyzer.getExecutionTraversePlan(plan, objectHandleClass);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object handle of class {0} through " +
              "transition {1}. Suitable guide has not been found", objectHandleClass.getCanonicalName(), plan.tid());
    }
    return executionPlan.execute(source, this);
  }

  @Override
  public Object execute(
      MapObjectHandleThruTransition1Plan plan, Object source, Object qualifier
  ) throws TraverseException {
    Class<?> objectHandleClass = ObjectFunctions.defineObjectHandleClass(source.getClass());
    ExecutionPlan executionPlan = traverseAnalyzer.getExecutionTraversePlan(plan, objectHandleClass);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object handle of class {0} through " +
          "transition {1}. Suitable guide has not been found", objectHandleClass.getCanonicalName(), plan.tid());
    }
    return executionPlan.execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MapObjectHandleThruTransition2Plan plan, Object source, Object qualifier1, Object qualifier2
  ) throws TraverseException {
    Class<?> objectHandleClass = ObjectFunctions.defineObjectHandleClass(source.getClass());
    ExecutionPlan executionPlan = traverseAnalyzer.getExecutionTraversePlan(plan, objectHandleClass);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object handle of class {0} through " +
          "transition {1}. Suitable guide has not been found", objectHandleClass.getCanonicalName(), plan.tid());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, this);
  }

  @Override
  public Object execute(MoveObjectHandleThruTransition0Plan plan, Object source) throws TraverseException {
    Class<?> objectHandleClass = ObjectFunctions.defineObjectHandleClass(source.getClass());
    ExecutionPlan executionPlan = traverseAnalyzer.getExecutionTraversePlan(plan, objectHandleClass);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to move object handle of class {0} through " +
          "transition {1}. Suitable guide has not been found", objectHandleClass.getCanonicalName(), plan.tid());
    }
    return executionPlan.execute(source, this);
  }

  @Override
  public Object execute(
      MoveObjectHandleThruTransition1Plan plan, Object source, Object qualifier
  ) throws TraverseException {
    Class<?> objectHandleClass = ObjectFunctions.defineObjectHandleClass(source.getClass());
    ExecutionPlan executionPlan = traverseAnalyzer.getExecutionTraversePlan(plan, objectHandleClass);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to move object handle of class {0} through " +
          "transition {1}. Suitable guide has not been found", objectHandleClass.getCanonicalName(), plan.tid());
    }
    return executionPlan.execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MoveObjectHandleThruTransition2Plan plan, Object source, Object qualifier1, Object qualifier2
  ) throws TraverseException {
    Class<?> objectHandleClass = ObjectFunctions.defineObjectHandleClass(source.getClass());
    ExecutionPlan executionPlan = traverseAnalyzer.getExecutionTraversePlan(plan, objectHandleClass);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to move object handle of class {0} through " +
          "transition {1}. Suitable guide has not been found", objectHandleClass.getCanonicalName(), plan.tid());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, this);

  }
}
