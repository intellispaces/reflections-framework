package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
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
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel0Plan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel3TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel4TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecifiedClassSourceThruIdentifiedChannel0TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecifiedClassSourceThruIdentifiedChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecifiedClassSourceThruIdentifiedChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecifiedClassSourceThruIdentifiedChannel3TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveSpecifiedClassSourceThruIdentifiedChannel0TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveSpecifiedClassSourceThruIdentifiedChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveSpecifiedClassSourceThruIdentifiedChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveSpecifiedClassSourceThruIdentifiedChannel3TraversePlan;

public class LocalTraverseExecutor implements TraverseExecutor {
  private final TraverseAnalyzer analyzer;

  public LocalTraverseExecutor(TraverseAnalyzer analyzer) {
    this.analyzer = analyzer;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object execute(
      CallGuide0Plan plan,
      Object source
  ) throws TraverseException {
    var guide = (Guide0<Object, Object>) plan.guide();
    return guide.traverse(source);
  }

  @Override
  @SuppressWarnings("unchecked")
  public int executeReturnInt(
      CallGuide0Plan plan,
      Object source
  ) throws TraverseException {
    var guide = (Guide0<Object, Object>) plan.guide();
    return guide.traverseToInt(source);
  }

  @Override
  @SuppressWarnings("unchecked")
  public double executeReturnDouble(
      CallGuide0Plan plan,
      Object source
  ) throws TraverseException {
    var guide = (Guide0<Object, Object>) plan.guide();
    return guide.traverseToDouble(source);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object execute(
      CallGuide1Plan plan,
      Object source,
      Object qualifier
  ) throws TraverseException {
    var guide = (Guide1<Object, Object, Object>) plan.guide();
    return guide.traverse(source, qualifier);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object execute(
      CallGuide2Plan plan,
      Object source,
      Object qualifier1,
      Object qualifier2
  ) throws TraverseException {
    var guide = (Guide2<Object, Object, Object, Object>) plan.guide();
    return guide.traverse(source, qualifier1, qualifier2);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object execute(
    CallGuide3Plan plan,
    Object source,
    Object qualifier1,
    Object qualifier2,
    Object qualifier3
  ) throws TraverseException {
    var guide = (Guide3<Object, Object, Object, Object, Object>) plan.guide();
    return guide.traverse(source, qualifier1, qualifier2, qualifier3);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object execute(
      CallGuide4Plan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3,
      Object qualifier4
  ) throws TraverseException {
    var guide = (Guide4<Object, Object, Object, Object, Object, Object>) plan.guide();
    return guide.traverse(source, qualifier1, qualifier2, qualifier3, qualifier4);
  }

  @Override
  public Object execute(
      AscendAndExecutePlan1 plan,
      Object source,
      Object qualifier
  ) throws TraverseException {
    var sourceReflection = (Reflection<?>) source;
    var overlyingReflection = sourceReflection.overlyingReflection();
    return plan.executionPlan().execute(overlyingReflection, qualifier, this);
  }

  @Override
  public Object execute(
      MapSpecifiedClassSourceThruIdentifiedChannel0TraversePlan plan,
      Object source
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map source of class {0} through " +
              "channel {1}", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, this);
  }

  @Override
  public int executeReturnInt(
      MapSpecifiedClassSourceThruIdentifiedChannel0TraversePlan plan,
      Object source
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan, source, ReflectionForms.Primitive);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map source of class {0} through " +
          "channel {1}", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.executeReturnInt(source, this);
  }

  @Override
  public double executeReturnDouble(
      MapSpecifiedClassSourceThruIdentifiedChannel0TraversePlan plan,
      Object source
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan, source, ReflectionForms.Primitive);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map source of class {0} through " +
          "channel {1}", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.executeReturnDouble(source, this);
  }

  @Override
  public Object execute(
      MapSpecifiedClassSourceThruIdentifiedChannel1TraversePlan plan,
      Object source,
      Object qualifier
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map source of class {0} through " +
          "channel {1}", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MapSpecifiedClassSourceThruIdentifiedChannel2TraversePlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map source of class {0} through " +
          "channel {1}", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, this);
  }

  @Override
  public Object execute(
      MapSpecifiedClassSourceThruIdentifiedChannel3TraversePlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map source of class {0} through " +
              "channel {1}", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, qualifier3, this);
  }

  @Override
  public Object execute(
      MoveSpecifiedClassSourceThruIdentifiedChannel0TraversePlan plan,
      Object source
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to move source of class {0} through " +
          "channel {1}", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, this);
  }

  @Override
  public Object execute(
      MoveSpecifiedClassSourceThruIdentifiedChannel1TraversePlan plan,
      Object source,
      Object qualifier
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to move source of class {0} through " +
          "channel {1}", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MoveSpecifiedClassSourceThruIdentifiedChannel2TraversePlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to move source of class {0} through " +
          "channel {1}", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, this);
  }

  @Override
  public Object execute(
      MoveSpecifiedClassSourceThruIdentifiedChannel3TraversePlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to move source of class {0} through " +
              "channel {1}", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, qualifier3, this);
  }

  @Override
  public Object execute(
      MapOfMovingSpecifiedClassSourceThruIdentifiedChannel0Plan plan,
      Object source
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map source of class {0} through " +
          "channel {1}", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, this);
  }

  @Override
  public int executeReturnInt(
      MapOfMovingSpecifiedClassSourceThruIdentifiedChannel0Plan plan,
      Object source
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map source of class {0} through " +
          "channel {1}", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.executeReturnInt(source, this);
  }

  @Override
  public double executeReturnDouble(
      MapOfMovingSpecifiedClassSourceThruIdentifiedChannel0Plan plan,
      Object source
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map source of class {0} through " +
          "channel {1}", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.executeReturnDouble(source, this);
  }

  @Override
  public Object execute(
      MapOfMovingSpecifiedClassSourceThruIdentifiedChannel1TraversePlan plan,
      Object source,
      Object qualifier
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map source of class {0} through " +
          "channel {1}", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MapOfMovingSpecifiedClassSourceThruIdentifiedChannel2TraversePlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map source of class {0} through " +
          "channel {1}", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, this);
  }

  @Override
  public Object execute(
      MapOfMovingSpecifiedClassSourceThruIdentifiedChannel3TraversePlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map source of class {0} through " +
          "channel {1}", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, qualifier3, this);
  }

  @Override
  public Object execute(
      MapOfMovingSpecifiedClassSourceThruIdentifiedChannel4TraversePlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3,
      Object qualifier4
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan, source, ReflectionForms.Reflection);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map source of class {0} through " +
          "channel {1}", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan.execute(source, qualifier1, qualifier2, qualifier3, qualifier4, this);
  }

  @Override
  public Object execute(
      MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlan plan
  ) throws TraverseException {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map specified source to domain {0}",
          plan.targetDomain());
    }
    return executionPlan.execute(plan.source(), this);
  }

  @Override
  public int executeReturnInt(
      MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlan plan
  ) throws TraverseException {
    throw NotImplementedExceptions.withCode("qkAfJQ");
  }

  @Override
  public double executeReturnDouble(
      MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlan plan
  ) throws TraverseException {
    throw NotImplementedExceptions.withCode("30u4tw");
  }
}
