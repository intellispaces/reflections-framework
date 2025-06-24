package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.exception.TraverseExceptions;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.reflections.framework.reflection.SystemReflection;
import tech.intellispaces.reflections.framework.task.plan.AscendAndExecutePlan;
import tech.intellispaces.reflections.framework.task.plan.CallGuidePlan;
import tech.intellispaces.reflections.framework.task.plan.ExecutionTraversePlan;
import tech.intellispaces.reflections.framework.task.plan.MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan;
import tech.intellispaces.reflections.framework.task.plan.MapSourceSpecifiedClassThruIdentifiedChannelPlan;
import tech.intellispaces.reflections.framework.task.plan.MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan;
import tech.intellispaces.reflections.framework.task.plan.MoveSourceSpecifiedClassThruIdentifiedChannelPlan;
import tech.intellispaces.reflections.framework.task.plan.TraversePlan;

public class LocalTraverseExecutor implements TraverseExecutor {
  private final TraverseAnalyzer analyzer;

  public LocalTraverseExecutor(TraverseAnalyzer analyzer) {
    this.analyzer = analyzer;
  }

  @Override
  public Object execute(
      CallGuidePlan plan,
      Object source
  ) throws TraverseException {
    var guide = plan.guide();
    return guide.asGuide0().traverse(source);
  }

  @Override
  public int executeReturnInt(
      CallGuidePlan plan,
      Object source
  ) throws TraverseException {
    var guide = plan.guide();
    return guide.asGuide0().traverseToInt(source);
  }

  @Override
  public double executeReturnDouble(
      CallGuidePlan plan,
      Object source
  ) throws TraverseException {
    var guide = plan.guide();
    return guide.asGuide0().traverseToDouble(source);
  }

  @Override
  public Object execute(
      CallGuidePlan plan,
      Object source,
      Object qualifier
  ) throws TraverseException {
    var guide = plan.guide();
    return guide.asGuide1().traverse(source, qualifier);
  }

  @Override
  public Object execute(
      CallGuidePlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2
  ) throws TraverseException {
    var guide = plan.guide();
    return guide.asGuide2().traverse(source, qualifier1, qualifier2);
  }

  @Override
  public Object execute(
      CallGuidePlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3
  ) throws TraverseException {
    var guide = plan.guide();
    return guide.asGuide3().traverse(source, qualifier1, qualifier2, qualifier3);
  }

  @Override
  public Object execute(
      CallGuidePlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3,
      Object qualifier4
  ) throws TraverseException {
    var guide = plan.guide();
    return guide.asGuide4().traverse(source, qualifier1, qualifier2, qualifier3, qualifier4);
  }

  @Override
  public Object execute(
      AscendAndExecutePlan plan,
      Object source,
      Object qualifier
  ) throws TraverseException {
    var sourceReflection = (SystemReflection) source;
    var overlyingReflection = sourceReflection.overlyingReflection();
    return plan.executionPlan().execute(overlyingReflection, qualifier, this);
  }

  @Override
  public Object execute(
      MapSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source
  ) throws TraverseException {
    return getOrThrowExecutionPlan(plan, source, ReflectionForms.Reflection)
        .execute(source, this);
  }

  @Override
  public int executeReturnInt(
      MapSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source
  ) throws TraverseException {
    return getOrThrowExecutionPlan(plan, source, ReflectionForms.Primitive)
        .executeReturnInt(source, this);
  }

  @Override
  public double executeReturnDouble(
      MapSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source
  ) throws TraverseException {
    return getOrThrowExecutionPlan(plan, source, ReflectionForms.Primitive)
        .executeReturnDouble(source, this);
  }

  @Override
  public Object execute(
      MapSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      Object qualifier
  ) throws TraverseException {
    return getOrThrowExecutionPlan(plan, source, ReflectionForms.Reflection)
        .execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MapSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2
  ) throws TraverseException {
    return getOrThrowExecutionPlan(plan, source, ReflectionForms.Reflection)
        .execute(source, qualifier1, qualifier2, this);
  }

  @Override
  public Object execute(
      MapSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3
  ) throws TraverseException {
    return getOrThrowExecutionPlan(plan, source, ReflectionForms.Reflection)
        .execute(source, qualifier1, qualifier2, qualifier3, this);
  }

  @Override
  public Object execute(
      MoveSourceSpecifiedClassThruIdentifiedChannelPlan plan,
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
      MoveSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      Object qualifier
  ) throws TraverseException {
    return getOrThrowExecutionPlan(plan, source, ReflectionForms.Reflection)
      .execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MoveSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2
  ) throws TraverseException {
    return getOrThrowExecutionPlan(plan, source, ReflectionForms.Reflection)
      .execute(source, qualifier1, qualifier2, this);
  }

  @Override
  public Object execute(
      MoveSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3
  ) throws TraverseException {
    return getOrThrowExecutionPlan(plan, source, ReflectionForms.Reflection)
        .execute(source, qualifier1, qualifier2, qualifier3, this);
  }

  @Override
  public Object execute(
      MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source
  ) throws TraverseException {
    return getOrThrowExecutionPlan(plan, source, ReflectionForms.Reflection)
        .execute(source, this);
  }

  @Override
  public int executeReturnInt(
      MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source
  ) throws TraverseException {
    return getOrThrowExecutionPlan(plan, source, ReflectionForms.Primitive)
      .executeReturnInt(source, this);
  }

  @Override
  public double executeReturnDouble(
      MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source
  ) throws TraverseException {
    return getOrThrowExecutionPlan(plan, source, ReflectionForms.Primitive)
      .executeReturnDouble(source, this);
  }

  @Override
  public Object execute(
      MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      Object qualifier
  ) throws TraverseException {
    return getOrThrowExecutionPlan(plan, source, ReflectionForms.Reflection)
      .execute(source, qualifier, this);
  }

  @Override
  public Object execute(
      MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2
  ) throws TraverseException {
    return getOrThrowExecutionPlan(plan, source, ReflectionForms.Reflection)
      .execute(source, qualifier1, qualifier2, this);
  }

  @Override
  public Object execute(
      MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3
  ) throws TraverseException {
    return getOrThrowExecutionPlan(plan, source, ReflectionForms.Reflection)
      .execute(source, qualifier1, qualifier2, qualifier3, this);
  }

  @Override
  public Object execute(
      MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      Object qualifier1,
      Object qualifier2,
      Object qualifier3,
      Object qualifier4
  ) throws TraverseException {
    return getOrThrowExecutionPlan(plan, source, ReflectionForms.Reflection)
      .execute(source, qualifier1, qualifier2, qualifier3, qualifier4, this);
  }

  @Override
  public Object execute(
      MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan plan
  ) throws TraverseException {
    TraversePlan executionPlan = analyzer.buildExecutionPlan(plan);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map specified source to domain {0}",
          plan.targetDomain());
    }
    if (executionPlan instanceof MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan) {
      return executionPlan.execute(this);
    }
    return executionPlan.execute(plan.source(), this);
  }

  @Override
  public int executeReturnInt(
      MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan plan
  ) throws TraverseException {
    throw NotImplementedExceptions.withCode("qkAfJQ");
  }

  @Override
  public double executeReturnDouble(
      MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan plan
  ) throws TraverseException {
    throw NotImplementedExceptions.withCode("30u4tw");
  }

  private ExecutionTraversePlan getOrThrowExecutionPlan(
      MapSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      ReflectionForm targetForm
  ) {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan, source, targetForm);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map source of class {0} through " +
          "channel {1}", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan;
  }

  private ExecutionTraversePlan getOrThrowExecutionPlan(
      MoveSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      ReflectionForm targetForm
  ) {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan, source, targetForm);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to move source of class {0} through " +
          "channel {1}", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan;
  }

  private ExecutionTraversePlan getOrThrowExecutionPlan(
      MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan plan,
      Object source,
      ReflectionForm targetForm
  ) {
    ExecutionTraversePlan executionPlan = analyzer.buildExecutionPlan(plan, source, targetForm);
    if (executionPlan == null) {
      throw TraverseExceptions.withMessage("Cannot to build traverse plan to map of moving source of class {0}" +
          "through channel {1}", source.getClass().getCanonicalName(), plan.channelId());
    }
    return executionPlan;
  }
}
