package tech.intellispaces.reflections.framework.engine.impl;

import java.util.List;
import java.util.stream.Collectors;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.core.Domain;
import tech.intellispaces.reflections.framework.guide.Guide;
import tech.intellispaces.reflections.framework.guide.GuideKind;
import tech.intellispaces.reflections.framework.guide.GuideKinds;
import tech.intellispaces.reflections.framework.guide.n0.Guide0;
import tech.intellispaces.reflections.framework.guide.n1.Guide1;
import tech.intellispaces.reflections.framework.guide.n2.Guide2;
import tech.intellispaces.reflections.framework.guide.n3.Guide3;
import tech.intellispaces.reflections.framework.guide.n4.Guide4;
import tech.intellispaces.reflections.framework.reflection.Reflection;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;
import tech.intellispaces.reflections.framework.system.GuideProvider;
import tech.intellispaces.reflections.framework.system.TraverseAnalyzer;
import tech.intellispaces.reflections.framework.traverse.plan.AscendAndExecutePlan1Impl;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide0PlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide1PlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide2PlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide3PlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide4PlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.ExecutionTraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel0Plan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel0PlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel1TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel2TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel3TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel3TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel4TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingThruChannel4TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecificReflectionToSpecificDomainAndClassTraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecificReflectionToSpecificDomainAndClassTraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MapThruChannel0TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapThruChannel0TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MapThruChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapThruChannel1TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MapThruChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapThruChannel2TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MapThruChannel3TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapThruChannel3TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MoveThruChannel0TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveThruChannel0TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MoveThruChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveThruChannel1TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MoveThruChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveThruChannel2TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MoveThruChannel3TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveThruChannel3TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.TraversePlanType;
import tech.intellispaces.reflections.framework.traverse.plan.TraversePlanTypes;
import tech.intellispaces.reflections.framework.traverse.plan.TraverseThruChannelPlan;

class TraverseAnalyzerImpl implements TraverseAnalyzer {
  private final GuideProvider guideProvider;

  public TraverseAnalyzerImpl(GuideProvider guideProvider) {
    this.guideProvider = guideProvider;
  }

  @Override
  public MapSpecificReflectionToSpecificDomainAndClassTraversePlan buildMapT0Plan(
      tech.intellispaces.core.Reflection source,
      Domain targetDomain,
      Class<?> targetClass
  ) {
    var declarativePlan = new MapSpecificReflectionToSpecificDomainAndClassTraversePlanImpl(
        source, targetDomain, targetClass
    );
    buildPreliminaryExecutionPlan(declarativePlan);
    return declarativePlan;
  }

  @Override
  public MapThruChannel0TraversePlan buildMapThruChannel0Plan(
    Class<?> sourceClass, String cid, ReflectionForm targetForm
  ) {
    MapThruChannel0TraversePlan declarativePlan = new MapThruChannel0TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapThruChannel1TraversePlan buildMapThruChannel1Plan(
    Class<?> sourceClass, String cid, ReflectionForm targetForm
  ) {
    MapThruChannel1TraversePlan declarativePlan = new MapThruChannel1TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapThruChannel2TraversePlan buildMapThruChannel2Plan(
    Class<?> sourceClass, String cid, ReflectionForm targetForm
  ) {
    MapThruChannel2TraversePlan declarativePlan = new MapThruChannel2TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapThruChannel3TraversePlan buildMapThruChannel3Plan(
    Class<?> sourceClass, String cid, ReflectionForm targetForm
  ) {
    MapThruChannel3TraversePlan declarativePlan = new MapThruChannel3TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MoveThruChannel0TraversePlan buildMoveThruChannel0Plan(
    Class<?> sourceClass, String cid, ReflectionForm targetForm
  ) {
    MoveThruChannel0TraversePlan declarativePlan = new MoveThruChannel0TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MoveThruChannel1TraversePlan buildMoveThruChannel1Plan(
    Class<?> sourceClass, String cid, ReflectionForm targetForm
  ) {
    MoveThruChannel1TraversePlan declarativePlan = new MoveThruChannel1TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MoveThruChannel2TraversePlan buildMoveThruChannel2Plan(
    Class<?> sourceClass, String cid, ReflectionForm targetForm
  ) {
    MoveThruChannel2TraversePlan declarativePlan = new MoveThruChannel2TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MoveThruChannel3TraversePlan buildMoveThruChannel3Plan(
    Class<?> sourceClass, String cid, ReflectionForm targetForm
  ) {
    MoveThruChannel3TraversePlan declarativePlan = new MoveThruChannel3TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingThruChannel0Plan buildMapOfMovingThruChannel0Plan(
      Class<?> sourceClass, String cid, ReflectionForm targetForm
  ) {
    MapOfMovingThruChannel0Plan declarativePlan = new MapOfMovingThruChannel0PlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingThruChannel1TraversePlan buildMapOfMovingThruChannel1Plan(
      Class<?> sourceClass, String cid, ReflectionForm targetForm
  ) {
    MapOfMovingThruChannel1TraversePlan declarativePlan = new MapOfMovingThruChannel1TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingThruChannel2TraversePlan buildMapOfMovingThruChannel2Plan(
      Class<?> sourceClass, String cid, ReflectionForm targetForm
  ) {
    MapOfMovingThruChannel2TraversePlan declarativePlan = new MapOfMovingThruChannel2TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingThruChannel3TraversePlan buildMapOfMovingThruChannel3Plan(
      Class<?> sourceClass, String cid, ReflectionForm targetForm
  ) {
    MapOfMovingThruChannel3TraversePlan declarativePlan = new MapOfMovingThruChannel3TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingThruChannel4TraversePlan buildMapOfMovingThruChannel4Plan(
      Class<?> sourceClass, String cid, ReflectionForm targetForm
  ) {
    MapOfMovingThruChannel4TraversePlan declarativePlan = new MapOfMovingThruChannel4TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  private void buildPreliminaryExecutionPlan(
      MapSpecificReflectionToSpecificDomainAndClassTraversePlan plan
  ) {
    getExecutionPlan(plan);
  }

  private void buildPreliminaryExecutionPlan(
      TraverseThruChannelPlan plan, Class<?> sourceClass, ReflectionForm targetForm
  ) {
    getExecutionPlan(plan, sourceClass, targetForm);
  }

  @Override
  public ExecutionTraversePlan getExecutionPlan(
      TraverseThruChannelPlan plan, Class<?> sourceClass, ReflectionForm targetForm
  ) {
    return getExecutionPlan(plan, sourceClass, null, targetForm);
  }

  @Override
  public ExecutionTraversePlan getExecutionPlan(
      TraverseThruChannelPlan plan, Object source, ReflectionForm targetForm
  ) {
    return getExecutionPlan(plan, source.getClass(), source, targetForm);
  }

  private ExecutionTraversePlan getExecutionPlan(
      TraverseThruChannelPlan plan, Class<?> sourceClass, Object source, ReflectionForm targetForm
  ) {
    ExecutionTraversePlan executionPlan = plan.cachedExecutionPlan(sourceClass);
    if (executionPlan != null) {
      return executionPlan;
    }

    TraversePlanType planType = plan.type();
    String cid = plan.channelId();

    if (!ReflectionFunctions.isObjectFormClass(sourceClass)) {
      throw UnexpectedExceptions.withMessage("Traverse plan of type {0} expected any object form to input", planType);
    }
    Class<?> reflectionClass = ReflectionFunctions.getReflectionClass(sourceClass);
    executionPlan = plan.cachedExecutionPlan(reflectionClass);
    if (executionPlan != null) {
      return executionPlan;
    }

    if (sourceClass != plan.reflectionClass() && !plan.reflectionClass().isAssignableFrom(sourceClass)) {
      throw UnexpectedExceptions.withMessage("Traverse plan of type {0} expected reflection of class {1} " +
          "or it subclass", planType, plan.reflectionClass());
    }
    executionPlan = plan.cachedExecutionPlan(plan.reflectionClass());
    if (executionPlan != null) {
      return executionPlan;
    }

    executionPlan = buildExecutionTraversePlan(planType, cid, sourceClass, targetForm);
    if (executionPlan != null) {
      plan.cacheExecutionPlan(sourceClass, executionPlan);
      return executionPlan;
    } else {
      executionPlan = buildExecutionTraversePlan(planType, cid, reflectionClass, targetForm);
      if (executionPlan != null) {
        plan.cacheExecutionPlan(sourceClass, executionPlan);
        plan.cacheExecutionPlan(reflectionClass, executionPlan);
        return executionPlan;
      }
    }

    if (source != null) {
      var sourceReflection = (Reflection<?>) source;
      while (sourceReflection.overlyingReflection() != null) {
        sourceReflection = sourceReflection.overlyingReflection();
        sourceClass = sourceReflection.getClass();

        executionPlan = plan.cachedExecutionPlan(sourceClass);
        if (executionPlan != null) {
          return executionPlan;
        }

        reflectionClass = ReflectionFunctions.getReflectionClass(sourceClass);
        Class<?> domainClass = ReflectionFunctions.getReflectionDomainClass(reflectionClass);
        String originCid = ChannelFunctions.getOriginDomainChannelId(domainClass, cid);
        if (originCid != null) {
          executionPlan = buildExecutionTraversePlan(planType, originCid, sourceClass, targetForm);
          if (executionPlan != null) {
            executionPlan = new AscendAndExecutePlan1Impl(executionPlan);
            break;
          }
        }
      }
    }
    return executionPlan;
  }

  private ExecutionTraversePlan getExecutionPlan(MapSpecificReflectionToSpecificDomainAndClassTraversePlan plan) {



    return null;
  }

  private ExecutionTraversePlan buildExecutionTraversePlan(
      TraversePlanType planType, String cid, Class<?> reflectionClass, ReflectionForm targetForm
  ) {
    GuideKinds guideKind = getGuideKind(planType);
    List<Guide<?, ?>> guides = findGuides(cid, guideKind, reflectionClass, targetForm);
    if (guides.isEmpty()) {
      return null;
    }
    if (guides.size() > 1) {
      throw NotImplementedExceptions.withCodeAndMessage("e9iXkw", "Multiple guides are found:\n{0}",
          guides.stream().map(Object::toString).collect(Collectors.joining("\n"))
      );
    }
    return switch (guideKind) {
      case Mapper0, Mover0, MapperOfMoving0 -> new CallGuide0PlanImpl((Guide0<?, ?>) guides.get(0));
      case Mapper1, Mover1, MapperOfMoving1 -> new CallGuide1PlanImpl((Guide1<?, ?, ?>) guides.get(0));
      case Mapper2, Mover2, MapperOfMoving2 -> new CallGuide2PlanImpl((Guide2<?, ?, ?, ?>) guides.get(0));
      case Mapper3, Mover3, MapperOfMoving3 -> new CallGuide3PlanImpl((Guide3<?, ?, ?, ?, ?>) guides.get(0));
      case Mapper4, Mover4, MapperOfMoving4 -> new CallGuide4PlanImpl((Guide4<?, ?, ?, ?, ?, ?>) guides.get(0));
      default -> throw NotImplementedExceptions.withCode("MlgXfQ");
    };
  }

  private List<Guide<?, ?>> findGuides(String cid, GuideKind kind, Class<?> reflectionClass, ReflectionForm form) {
    List<Guide<?, ?>> guides = guideProvider.findGuides(cid, kind, reflectionClass, form);
    if (guides.isEmpty()) {
      Class<?> domainClass = ReflectionFunctions.getReflectionDomainClass(reflectionClass);
      String originDomainChannelId = ChannelFunctions.getOriginDomainChannelId(domainClass, cid);
      if (originDomainChannelId != null) {
        guides = guideProvider.findGuides(originDomainChannelId, kind, reflectionClass, form);
      }
    }
    return guides;
  }

  private GuideKinds getGuideKind(TraversePlanType planType) {
    return switch (TraversePlanTypes.of(planType)) {
      case MapThruChannel0 -> GuideKinds.Mapper0;
      case MapThruChannel1 -> GuideKinds.Mapper1;
      case MapThruChannel2 -> GuideKinds.Mapper2;
      case MapThruChannel3 -> GuideKinds.Mapper3;
      case MoveThruChannel0 -> GuideKinds.Mover0;
      case MoveThruChannel1 -> GuideKinds.Mover1;
      case MoveThruChannel2 -> GuideKinds.Mover2;
      case MoveThruChannel3 -> GuideKinds.Mover3;
      case MapOfMovingThruChannel0 -> GuideKinds.MapperOfMoving0;
      case MapOfMovingThruChannel1 -> GuideKinds.MapperOfMoving1;
      case MapOfMovingChannel2 -> GuideKinds.MapperOfMoving2;
      case MapOfMovingThruChannel3 -> GuideKinds.MapperOfMoving3;
      case MapOfMovingThruChannel4 -> GuideKinds.MapperOfMoving4;
      default -> throw NotImplementedExceptions.withCode("5cYSWA");
    };
  }
}
