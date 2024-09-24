package intellispaces.framework.core.system.kernel;

import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.n0.Guide0;
import intellispaces.framework.core.guide.n1.Guide1;
import intellispaces.framework.core.guide.n2.Guide2;
import intellispaces.framework.core.guide.n3.Guide3;
import intellispaces.framework.core.traverse.CallGuide0Plan;
import intellispaces.framework.core.traverse.CallGuide1Plan;
import intellispaces.framework.core.traverse.CallGuide2Plan;
import intellispaces.framework.core.traverse.CallGuide3Plan;
import intellispaces.framework.core.traverse.ExecutionPlan;
import intellispaces.framework.core.traverse.MapObjectHandleThruTransition0Plan;
import intellispaces.framework.core.traverse.MapObjectHandleThruTransition1Plan;
import intellispaces.framework.core.traverse.MapObjectHandleThruTransition2Plan;
import intellispaces.framework.core.traverse.MapObjectHandleThruTransition3Plan;
import intellispaces.framework.core.traverse.MapOfMovingObjectHandleThruTransition0Plan;
import intellispaces.framework.core.traverse.MapOfMovingObjectHandleThruTransition1Plan;
import intellispaces.framework.core.traverse.MapOfMovingObjectHandleThruTransition2Plan;
import intellispaces.framework.core.traverse.MapOfMovingObjectHandleThruTransition3Plan;
import intellispaces.framework.core.traverse.MoveObjectHandleThruTransition0Plan;
import intellispaces.framework.core.traverse.MoveObjectHandleThruTransition1Plan;
import intellispaces.framework.core.traverse.MoveObjectHandleThruTransition2Plan;
import intellispaces.framework.core.traverse.MoveObjectHandleThruTransition3Plan;
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
  @SuppressWarnings("unchecked")
  public Object execute(
    CallGuide3Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3
  ) throws TraverseException {
    var guide = (Guide3<Object, Object, Object, Object, Object>) plan.guide();
    return guide.traverse(source, qualifier1, qualifier2, qualifier3);

  }

  @Override
  public Object execute(MapObjectHandleThruTransition0Plan plan, Object source) throws TraverseException {
    ExecutionPlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass());
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
              "transition {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.tid());
    }
    return executionPlan.execute(source, this);
  }

  @Override
  public Object execute(
      MapObjectHandleThruTransition1Plan plan, Object source, Object qualifier
  ) throws TraverseException {
    ExecutionPlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass());
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "transition {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.tid());
    }
    return executionPlan.execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MapObjectHandleThruTransition2Plan plan, Object source, Object qualifier1, Object qualifier2
  ) throws TraverseException {
    ExecutionPlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass());
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "transition {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.tid());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, this);
  }

  @Override
  public Object execute(
    MapObjectHandleThruTransition3Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3
  ) throws TraverseException {
    ExecutionPlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass());
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
              "transition {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.tid());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, qualifier3, this);
  }

  @Override
  public Object execute(MoveObjectHandleThruTransition0Plan plan, Object source) throws TraverseException {
    ExecutionPlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass());
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to move object of class {0} through " +
          "transition {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.tid());
    }
    return executionPlan.execute(source, this);
  }

  @Override
  public Object execute(
      MoveObjectHandleThruTransition1Plan plan, Object source, Object qualifier
  ) throws TraverseException {
    ExecutionPlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass());
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to move object of class {0} through " +
          "transition {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.tid());
    }
    return executionPlan.execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MoveObjectHandleThruTransition2Plan plan, Object source, Object qualifier1, Object qualifier2
  ) throws TraverseException {
    ExecutionPlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass());
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to move object of class {0} through " +
          "transition {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.tid());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, this);
  }

  @Override
  public Object execute(
    MoveObjectHandleThruTransition3Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3
  ) throws TraverseException {
    ExecutionPlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass());
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to move object of class {0} through " +
              "transition {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.tid());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, qualifier3, this);
  }

  @Override
  public Object execute(
      MapOfMovingObjectHandleThruTransition0Plan plan, Object source
  ) throws TraverseException {
    ExecutionPlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass());
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "transition {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.tid());
    }
    return executionPlan.execute(source, this);
  }

  @Override
  public Object execute(
      MapOfMovingObjectHandleThruTransition1Plan plan, Object source, Object qualifier
  ) throws TraverseException {
    ExecutionPlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass());
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "transition {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.tid());
    }
    return executionPlan.execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MapOfMovingObjectHandleThruTransition2Plan plan, Object source, Object qualifier1, Object qualifier2
  ) throws TraverseException {
    ExecutionPlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass());
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "transition {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.tid());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, this);
  }

  @Override
  public Object execute(
      MapOfMovingObjectHandleThruTransition3Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3
  ) throws TraverseException {
    ExecutionPlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass());
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "transition {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.tid());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, qualifier3, this);
  }
}
