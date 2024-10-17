package intellispaces.framework.core.system.kernel;

import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.guide.GuideForms;
import intellispaces.framework.core.guide.n0.Guide0;
import intellispaces.framework.core.guide.n1.Guide1;
import intellispaces.framework.core.guide.n2.Guide2;
import intellispaces.framework.core.guide.n3.Guide3;
import intellispaces.framework.core.traverse.plan.CallGuide0Plan;
import intellispaces.framework.core.traverse.plan.CallGuide1Plan;
import intellispaces.framework.core.traverse.plan.CallGuide2Plan;
import intellispaces.framework.core.traverse.plan.CallGuide3Plan;
import intellispaces.framework.core.traverse.plan.ExecutionTraversePlan;
import intellispaces.framework.core.traverse.plan.MapObjectHandleThruChannel0Plan;
import intellispaces.framework.core.traverse.plan.MapObjectHandleThruChannel1Plan;
import intellispaces.framework.core.traverse.plan.MapObjectHandleThruChannel2Plan;
import intellispaces.framework.core.traverse.plan.MapObjectHandleThruChannel3Plan;
import intellispaces.framework.core.traverse.plan.MapOfMovingObjectHandleThruChannel0Plan;
import intellispaces.framework.core.traverse.plan.MapOfMovingObjectHandleThruChannel1Plan;
import intellispaces.framework.core.traverse.plan.MapOfMovingObjectHandleThruChannel2Plan;
import intellispaces.framework.core.traverse.plan.MapOfMovingObjectHandleThruChannel3Plan;
import intellispaces.framework.core.traverse.plan.MapOfMovingObjectHandleThruChannel4Plan;
import intellispaces.framework.core.traverse.plan.MoveObjectHandleThruChannel0Plan;
import intellispaces.framework.core.traverse.plan.MoveObjectHandleThruChannel1Plan;
import intellispaces.framework.core.traverse.plan.MoveObjectHandleThruChannel2Plan;
import intellispaces.framework.core.traverse.plan.MoveObjectHandleThruChannel3Plan;
import intellispaces.framework.core.traverse.plan.TraverseAnalyzer;
import intellispaces.framework.core.traverse.plan.TraverseExecutor;

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
  public int executeReturnInt(CallGuide0Plan plan, Object source) throws TraverseException {
    var guide = (Guide0<Object, Object>) plan.guide();
    return guide.traverseToInt(source);
  }

  @Override
  @SuppressWarnings("unchecked")
  public double executeReturnDouble(CallGuide0Plan plan, Object source) throws TraverseException {
    var guide = (Guide0<Object, Object>) plan.guide();
    return guide.traverseToDouble(source);
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
  public Object execute(MapObjectHandleThruChannel0Plan plan, Object source) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass(), GuideForms.Main);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
              "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.cid());
    }
    return executionPlan.execute(source, this);
  }

  @Override
  public int executeReturnInt(MapObjectHandleThruChannel0Plan plan, Object source) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass(), GuideForms.Primitive);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.cid());
    }
    return executionPlan.executeReturnInt(source, this);
  }

  @Override
  public double executeReturnDouble(MapObjectHandleThruChannel0Plan plan, Object source) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass(), GuideForms.Primitive);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.cid());
    }
    return executionPlan.executeReturnDouble(source, this);
  }

  @Override
  public Object execute(
      MapObjectHandleThruChannel1Plan plan, Object source, Object qualifier
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass(), GuideForms.Main);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.cid());
    }
    return executionPlan.execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MapObjectHandleThruChannel2Plan plan, Object source, Object qualifier1, Object qualifier2
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass(), GuideForms.Main);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.cid());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, this);
  }

  @Override
  public Object execute(
      MapObjectHandleThruChannel3Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass(), GuideForms.Main);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
              "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.cid());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, qualifier3, this);
  }

  @Override
  public Object execute(MoveObjectHandleThruChannel0Plan plan, Object source) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass(), GuideForms.Main);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to move object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.cid());
    }
    return executionPlan.execute(source, this);
  }

  @Override
  public Object execute(
      MoveObjectHandleThruChannel1Plan plan, Object source, Object qualifier
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass(), GuideForms.Main);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to move object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.cid());
    }
    return executionPlan.execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MoveObjectHandleThruChannel2Plan plan, Object source, Object qualifier1, Object qualifier2
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass(), GuideForms.Main);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to move object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.cid());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, this);
  }

  @Override
  public Object execute(
      MoveObjectHandleThruChannel3Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass(), GuideForms.Main);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to move object of class {0} through " +
              "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.cid());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, qualifier3, this);
  }

  @Override
  public Object execute(
      MapOfMovingObjectHandleThruChannel0Plan plan, Object source
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass(), GuideForms.Main);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.cid());
    }
    return executionPlan.execute(source, this);
  }

  @Override
  public int executeReturnInt(MapOfMovingObjectHandleThruChannel0Plan plan, Object source) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass(), GuideForms.Main);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.cid());
    }
    return executionPlan.executeReturnInt(source, this);
  }

  @Override
  public double executeReturnDouble(MapOfMovingObjectHandleThruChannel0Plan plan, Object source) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass(), GuideForms.Main);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.cid());
    }
    return executionPlan.executeReturnDouble(source, this);
  }

  @Override
  public Object execute(
      MapOfMovingObjectHandleThruChannel1Plan plan, Object source, Object qualifier
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass(), GuideForms.Main);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.cid());
    }
    return executionPlan.execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MapOfMovingObjectHandleThruChannel2Plan plan, Object source, Object qualifier1, Object qualifier2
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass(), GuideForms.Main);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.cid());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, this);
  }

  @Override
  public Object execute(
      MapOfMovingObjectHandleThruChannel3Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass(), GuideForms.Main);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.cid());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, qualifier3, this);
  }

  @Override
  public Object execute(
      MapOfMovingObjectHandleThruChannel4Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3, Object qualifier4
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source.getClass(), GuideForms.Main);
    if (executionPlan == null) {
      throw TraverseException.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.cid());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, qualifier3, qualifier4, this);
  }
}
