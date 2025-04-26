package tech.intellispaces.jaquarius.engine.impl;

import tech.intellispaces.jaquarius.exception.TraverseException;
import tech.intellispaces.jaquarius.exception.TraverseExceptions;
import tech.intellispaces.jaquarius.guide.n0.Guide0;
import tech.intellispaces.jaquarius.guide.n1.Guide1;
import tech.intellispaces.jaquarius.guide.n2.Guide2;
import tech.intellispaces.jaquarius.guide.n3.Guide3;
import tech.intellispaces.jaquarius.guide.n4.Guide4;
import tech.intellispaces.jaquarius.object.reference.ObjectHandle;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.traverse.plan.AscendAndExecutePlan1;
import tech.intellispaces.jaquarius.traverse.plan.CallGuide0Plan;
import tech.intellispaces.jaquarius.traverse.plan.CallGuide1Plan;
import tech.intellispaces.jaquarius.traverse.plan.CallGuide2Plan;
import tech.intellispaces.jaquarius.traverse.plan.CallGuide3Plan;
import tech.intellispaces.jaquarius.traverse.plan.CallGuide4Plan;
import tech.intellispaces.jaquarius.traverse.plan.ExecutionTraversePlan;
import tech.intellispaces.jaquarius.traverse.plan.MapObjectHandleThruChannel0Plan;
import tech.intellispaces.jaquarius.traverse.plan.MapObjectHandleThruChannel1Plan;
import tech.intellispaces.jaquarius.traverse.plan.MapObjectHandleThruChannel2Plan;
import tech.intellispaces.jaquarius.traverse.plan.MapObjectHandleThruChannel3Plan;
import tech.intellispaces.jaquarius.traverse.plan.MapOfMovingObjectHandleThruChannel0Plan;
import tech.intellispaces.jaquarius.traverse.plan.MapOfMovingObjectHandleThruChannel1Plan;
import tech.intellispaces.jaquarius.traverse.plan.MapOfMovingObjectHandleThruChannel2Plan;
import tech.intellispaces.jaquarius.traverse.plan.MapOfMovingObjectHandleThruChannel3Plan;
import tech.intellispaces.jaquarius.traverse.plan.MapOfMovingObjectHandleThruChannel4Plan;
import tech.intellispaces.jaquarius.traverse.plan.MoveObjectHandleThruChannel0Plan;
import tech.intellispaces.jaquarius.traverse.plan.MoveObjectHandleThruChannel1Plan;
import tech.intellispaces.jaquarius.traverse.plan.MoveObjectHandleThruChannel2Plan;
import tech.intellispaces.jaquarius.traverse.plan.MoveObjectHandleThruChannel3Plan;
import tech.intellispaces.jaquarius.traverse.plan.TraverseAnalyzer;

class TraverseExecutor implements tech.intellispaces.jaquarius.traverse.plan.TraverseExecutor {
  private final TraverseAnalyzer traverseAnalyzer;

  public TraverseExecutor(TraverseAnalyzer traverseAnalyzer) {
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
  @SuppressWarnings("unchecked")
  public Object execute(
      CallGuide4Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3, Object qualifier4
  ) throws TraverseException {
    var guide = (Guide4<Object, Object, Object, Object, Object, Object>) plan.guide();
    return guide.traverse(source, qualifier1, qualifier2, qualifier3, qualifier4);
  }

  @Override
  public Object execute(AscendAndExecutePlan1 plan, Object source, Object qualifier) throws TraverseException {
    var sourceHandle = (ObjectHandle<?>) source;
    var overlyingHandle = sourceHandle.overlyingHandle();
    return plan.executionPlan().execute(overlyingHandle, qualifier, this);
  }

  @Override
  public Object execute(MapObjectHandleThruChannel0Plan plan, Object source) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ObjectReferenceForms.ObjectHandle);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
              "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, this);
  }

  @Override
  public int executeReturnInt(MapObjectHandleThruChannel0Plan plan, Object source) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ObjectReferenceForms.Primitive);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.executeReturnInt(source, this);
  }

  @Override
  public double executeReturnDouble(MapObjectHandleThruChannel0Plan plan, Object source) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ObjectReferenceForms.Primitive);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.executeReturnDouble(source, this);
  }

  @Override
  public Object execute(
      MapObjectHandleThruChannel1Plan plan, Object source, Object qualifier
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ObjectReferenceForms.ObjectHandle);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MapObjectHandleThruChannel2Plan plan, Object source, Object qualifier1, Object qualifier2
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ObjectReferenceForms.ObjectHandle);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, this);
  }

  @Override
  public Object execute(
      MapObjectHandleThruChannel3Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ObjectReferenceForms.ObjectHandle);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
              "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, qualifier3, this);
  }

  @Override
  public Object execute(MoveObjectHandleThruChannel0Plan plan, Object source) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ObjectReferenceForms.ObjectHandle);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to move object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, this);
  }

  @Override
  public Object execute(
      MoveObjectHandleThruChannel1Plan plan, Object source, Object qualifier
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ObjectReferenceForms.ObjectHandle);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to move object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MoveObjectHandleThruChannel2Plan plan, Object source, Object qualifier1, Object qualifier2
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ObjectReferenceForms.ObjectHandle);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to move object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, this);
  }

  @Override
  public Object execute(
      MoveObjectHandleThruChannel3Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ObjectReferenceForms.ObjectHandle);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to move object of class {0} through " +
              "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, qualifier3, this);
  }

  @Override
  public Object execute(
      MapOfMovingObjectHandleThruChannel0Plan plan, Object source
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ObjectReferenceForms.ObjectHandle);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, this);
  }

  @Override
  public int executeReturnInt(MapOfMovingObjectHandleThruChannel0Plan plan, Object source) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ObjectReferenceForms.ObjectHandle);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.executeReturnInt(source, this);
  }

  @Override
  public double executeReturnDouble(MapOfMovingObjectHandleThruChannel0Plan plan, Object source) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ObjectReferenceForms.ObjectHandle);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.executeReturnDouble(source, this);
  }

  @Override
  public Object execute(
      MapOfMovingObjectHandleThruChannel1Plan plan, Object source, Object qualifier
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ObjectReferenceForms.ObjectHandle);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MapOfMovingObjectHandleThruChannel2Plan plan, Object source, Object qualifier1, Object qualifier2
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ObjectReferenceForms.ObjectHandle);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, this);
  }

  @Override
  public Object execute(
      MapOfMovingObjectHandleThruChannel3Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ObjectReferenceForms.ObjectHandle);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, qualifier3, this);
  }

  @Override
  public Object execute(
      MapOfMovingObjectHandleThruChannel4Plan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3, Object qualifier4
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ObjectReferenceForms.ObjectHandle);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, qualifier3, qualifier4, this);
  }
}
