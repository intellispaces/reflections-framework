package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.exception.TraverseExceptions;
import tech.intellispaces.reflections.framework.guide.n0.Guide0;
import tech.intellispaces.reflections.framework.guide.n1.Guide1;
import tech.intellispaces.reflections.framework.guide.n2.Guide2;
import tech.intellispaces.reflections.framework.guide.n3.Guide3;
import tech.intellispaces.reflections.framework.guide.n4.Guide4;
import tech.intellispaces.reflections.framework.reflection.Reflection;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.reflections.framework.traverse.plan.AscendAndExecutePlan1;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide0Plan;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide1Plan;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide2Plan;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide3Plan;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide4Plan;
import tech.intellispaces.reflections.framework.traverse.plan.ExecutionTraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel0Plan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel3TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel4TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapThruChannel0TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapThruChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapThruChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapThruChannel3TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveThruChannel0TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveThruChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveThruChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveThruChannel3TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.TraverseAnalyzer;
import tech.intellispaces.reflections.framework.traverse.plan.TraverseExecutor;

public class LocalTraverseExecutor implements TraverseExecutor {
  private final TraverseAnalyzer traverseAnalyzer;

  public LocalTraverseExecutor(TraverseAnalyzer traverseAnalyzer) {
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
    var sourceReflection = (Reflection<?>) source;
    var overlyingReflection = sourceReflection.overlyingReflection();
    return plan.executionPlan().execute(overlyingReflection, qualifier, this);
  }

  @Override
  public Object execute(MapThruChannel0TraversePlan plan, Object source) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
              "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, this);
  }

  @Override
  public int executeReturnInt(MapThruChannel0TraversePlan plan, Object source) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ReflectionForms.Primitive);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.executeReturnInt(source, this);
  }

  @Override
  public double executeReturnDouble(MapThruChannel0TraversePlan plan, Object source) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ReflectionForms.Primitive);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.executeReturnDouble(source, this);
  }

  @Override
  public Object execute(
      MapThruChannel1TraversePlan plan, Object source, Object qualifier
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MapThruChannel2TraversePlan plan, Object source, Object qualifier1, Object qualifier2
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, this);
  }

  @Override
  public Object execute(
      MapThruChannel3TraversePlan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
              "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, qualifier3, this);
  }

  @Override
  public Object execute(MoveThruChannel0TraversePlan plan, Object source) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to move object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, this);
  }

  @Override
  public Object execute(
      MoveThruChannel1TraversePlan plan, Object source, Object qualifier
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to move object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MoveThruChannel2TraversePlan plan, Object source, Object qualifier1, Object qualifier2
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to move object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, this);
  }

  @Override
  public Object execute(
      MoveThruChannel3TraversePlan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to move object of class {0} through " +
              "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, qualifier3, this);
  }

  @Override
  public Object execute(
      MapOfMovingThruChannel0Plan plan, Object source
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, this);
  }

  @Override
  public int executeReturnInt(MapOfMovingThruChannel0Plan plan, Object source) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.executeReturnInt(source, this);
  }

  @Override
  public double executeReturnDouble(MapOfMovingThruChannel0Plan plan, Object source) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.executeReturnDouble(source, this);
  }

  @Override
  public Object execute(
      MapOfMovingThruChannel1TraversePlan plan, Object source, Object qualifier
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MapOfMovingThruChannel2TraversePlan plan, Object source, Object qualifier1, Object qualifier2
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, this);
  }

  @Override
  public Object execute(
      MapOfMovingThruChannel3TraversePlan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, qualifier3, this);
  }

  @Override
  public Object execute(
      MapOfMovingThruChannel4TraversePlan plan, Object source, Object qualifier1, Object qualifier2, Object qualifier3, Object qualifier4
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = traverseAnalyzer.getExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map object of class {0} through " +
          "channel {1}. Suitable guide has not been found", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, qualifier3, qualifier4, this);
  }
}
