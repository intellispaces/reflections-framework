package tech.intellispaces.reflections.framework.engine.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.core.Channel;
import tech.intellispaces.core.Domain;
import tech.intellispaces.core.Rid;
import tech.intellispaces.core.repository.OntologyRepository;
import tech.intellispaces.reflections.framework.guide.GuideType;
import tech.intellispaces.reflections.framework.guide.GuideTypes;
import tech.intellispaces.reflections.framework.guide.SystemGuide;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;
import tech.intellispaces.reflections.framework.reflection.SystemReflection;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;
import tech.intellispaces.reflections.framework.system.GuideProvider;
import tech.intellispaces.reflections.framework.system.ReflectionRegistry;
import tech.intellispaces.reflections.framework.system.TraverseAnalyzer;
import tech.intellispaces.reflections.framework.task.plan.AscendAndExecutePlanImpl;
import tech.intellispaces.reflections.framework.task.plan.CallGuidePlanImpl;
import tech.intellispaces.reflections.framework.task.plan.ExecutionTraversePlan;
import tech.intellispaces.reflections.framework.task.plan.MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan;
import tech.intellispaces.reflections.framework.task.plan.MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlanImpl;
import tech.intellispaces.reflections.framework.task.plan.MapSourceSpecifiedClassThruIdentifiedChannelPlan;
import tech.intellispaces.reflections.framework.task.plan.MapSourceSpecifiedClassThruIdentifiedChannelPlanImpl;
import tech.intellispaces.reflections.framework.task.plan.MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan;
import tech.intellispaces.reflections.framework.task.plan.MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlanImpl;
import tech.intellispaces.reflections.framework.task.plan.MoveSourceSpecifiedClassThruIdentifiedChannelPlan;
import tech.intellispaces.reflections.framework.task.plan.MoveSourceSpecifiedClassThruIdentifiedChannelPlanImpl;
import tech.intellispaces.reflections.framework.task.plan.TaskPlanType;
import tech.intellispaces.reflections.framework.task.plan.TaskPlanTypes;
import tech.intellispaces.reflections.framework.task.plan.TraversePlan;
import tech.intellispaces.reflections.framework.task.plan.TraverseSourceSpecifiedClassThruIdentifierChannelTraversePlan;

class TraverseAnalyzerImpl implements TraverseAnalyzer {
  private final OntologyRepository ontologyRepository;
  private final GuideProvider guideProvider;
  private final ReflectionRegistry reflectionRegistry;

  public TraverseAnalyzerImpl(
      OntologyRepository ontologyRepository,
      GuideProvider guideProvider,
      ReflectionRegistry reflectionRegistry
  ) {
    this.ontologyRepository = ontologyRepository;
    this.guideProvider = guideProvider;
    this.reflectionRegistry = reflectionRegistry;
  }

  @Override
  public MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan buildMapToDomainPlan(
      tech.intellispaces.core.Reflection source,
      Domain targetDomain,
      Class<?> targetClass
  ) {
    var declarativePlan = new MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlanImpl(
        source, targetDomain, targetClass
    );
    buildPreliminaryExecutionPlan(declarativePlan);
    return declarativePlan;
  }

  @Override
  public MapSourceSpecifiedClassThruIdentifiedChannelPlan buildMapThruChannelPlan(
      Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  ) {
    var declarativePlan = new MapSourceSpecifiedClassThruIdentifiedChannelPlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MoveSourceSpecifiedClassThruIdentifiedChannelPlan buildMoveThruChannelPlan(
    Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  ) {
    var declarativePlan = new MoveSourceSpecifiedClassThruIdentifiedChannelPlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan buildMapOfMovingThruChannelPlan(
      Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  ) {
    var declarativePlan = new MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  private void buildPreliminaryExecutionPlan(
      MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan plan
  ) {
    buildExecutionPlan(plan);
  }

  private void buildPreliminaryExecutionPlan(
      TraverseSourceSpecifiedClassThruIdentifierChannelTraversePlan plan, Class<?> sourceClass, ReflectionForm targetForm
  ) {
    buildExecutionPlan(plan, sourceClass, targetForm);
  }

  @Override
  public ExecutionTraversePlan buildExecutionPlan(
      TraverseSourceSpecifiedClassThruIdentifierChannelTraversePlan plan, Class<?> sourceClass, ReflectionForm targetForm
  ) {
    return buildExecutionPlan(plan, sourceClass, null, targetForm);
  }

  @Override
  public ExecutionTraversePlan buildExecutionPlan(
      TraverseSourceSpecifiedClassThruIdentifierChannelTraversePlan plan, Object source, ReflectionForm targetForm
  ) {
    return buildExecutionPlan(plan, source.getClass(), source, targetForm);
  }

  private ExecutionTraversePlan buildExecutionPlan(
      TraverseSourceSpecifiedClassThruIdentifierChannelTraversePlan plan,
      Class<?> sourceClass,
      Object source,
      ReflectionForm targetForm
  ) {
    ExecutionTraversePlan executionPlan = plan.executionPlan(sourceClass);
    if (executionPlan != null) {
      return executionPlan;
    }

    TaskPlanType planType = plan.type();
    Rid cid = plan.channelId();

    if (!ReflectionFunctions.isObjectFormClass(sourceClass)) {
      throw UnexpectedExceptions.withMessage("Traverse plan of type {0} expected any object form to input", planType);
    }
    Class<?> reflectionClass = ReflectionFunctions.getReflectionClass(sourceClass);
    executionPlan = plan.executionPlan(reflectionClass);
    if (executionPlan != null) {
      return executionPlan;
    }

    if (sourceClass != plan.sourceClass() && !plan.sourceClass().isAssignableFrom(sourceClass)) {
      throw UnexpectedExceptions.withMessage("Traverse plan of type {0} expected reflection of class {1} " +
          "or it subclass", planType, plan.sourceClass());
    }
    executionPlan = plan.executionPlan(plan.sourceClass());
    if (executionPlan != null) {
      return executionPlan;
    }

    executionPlan = buildExecutionTraversePlan(planType, cid, sourceClass, targetForm);
    if (executionPlan != null) {
      plan.addExecutionPlan(sourceClass, executionPlan);
      return executionPlan;
    } else {
      executionPlan = buildExecutionTraversePlan(planType, cid, reflectionClass, targetForm);
      if (executionPlan != null) {
        plan.addExecutionPlan(sourceClass, executionPlan);
        plan.addExecutionPlan(reflectionClass, executionPlan);
        return executionPlan;
      }
    }

    if (source != null) {
      var sourceReflection = (SystemReflection) source;
      while (sourceReflection.overlyingReflection() != null) {
        sourceReflection = sourceReflection.overlyingReflection();
        sourceClass = sourceReflection.getClass();

        executionPlan = plan.executionPlan(sourceClass);
        if (executionPlan != null) {
          return executionPlan;
        }

        reflectionClass = ReflectionFunctions.getReflectionClass(sourceClass);
        Class<?> domainClass = ReflectionFunctions.getReflectionDomainClass(reflectionClass);
        Rid originCid = ChannelFunctions.getOriginDomainChannelId(domainClass, cid);
        if (originCid != null) {
          executionPlan = buildExecutionTraversePlan(planType, originCid, sourceClass, targetForm);
          if (executionPlan != null) {
            executionPlan = new AscendAndExecutePlanImpl(executionPlan);
            break;
          }
        }
      }
    }
    return executionPlan;
  }

  @Override
  public TraversePlan buildExecutionPlan(
      MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan plan
  ) {
    Domain sourceDomain = plan.source().domain();
    Channel channel = findChannel(sourceDomain, plan.targetDomain());
    if (channel == null) {
      return null;
    }

    ExecutionTraversePlan executionPlan = buildExecutionTraversePlan(
        plan.type(), channel.rid(), plan.source(), ReflectionForms.Reflection
    );
    if (executionPlan == null) {
      tech.intellispaces.core.Reflection registeredReflection = reflectionRegistry.get(plan.source().rid());
      if (registeredReflection != null) {
        executionPlan = buildExecutionTraversePlan(
            plan.type(), channel.rid(), registeredReflection, ReflectionForms.Reflection
        );
        if (executionPlan != null) {
          var replaceSourcePlan = new MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlanImpl(
              registeredReflection,
              plan.targetDomain(),
              plan.targetClass()
          );
          replaceSourcePlan.setExecutionPlan(executionPlan);
          return replaceSourcePlan;
        }
      }
    }
    return executionPlan;
  }

  private @Nullable Channel findChannel(Domain sourceDomain, Domain targetDomain) {
    Channel channel = ontologyRepository.findChannel(sourceDomain, targetDomain);
    if (channel == null && sourceDomain.foreignDomainName() != null) {
      Domain foreignSourceDomain = ontologyRepository.findDomain(sourceDomain.foreignDomainName());
      if (foreignSourceDomain != null) {
        channel = ontologyRepository.findChannel(foreignSourceDomain, targetDomain);
      }
    }
    return channel;
  }

  private ExecutionTraversePlan buildExecutionTraversePlan(
      TaskPlanType planType,
      Rid cid,
      Class<?> sourceClass,
      ReflectionForm targetForm
  ) {
    GuideType guideType = getGuideType(planType);
    List<SystemGuide<?, ?>> guides = findGuides(guideType, cid, sourceClass, targetForm);
    if (guides.isEmpty()) {
      return null;
    }
    if (guides.size() > 1) {
      throw NotImplementedExceptions.withCodeAndMessage("e9iXkw", "Multiple guides are found:\n{0}",
          guides.stream().map(Object::toString).collect(Collectors.joining("\n"))
      );
    }
    return new CallGuidePlanImpl(guides.get(0));
  }

  private ExecutionTraversePlan buildExecutionTraversePlan(
      TaskPlanType planType,
      Rid cid,
      tech.intellispaces.core.Reflection source,
      ReflectionForm targetForm
  ) {
    GuideType guideType = getGuideType(planType);
    List<SystemGuide<?, ?>> guides = findGuides(guideType, cid, source, targetForm);
    if (guides.isEmpty()) {
      return null;
    }
    if (guides.size() > 1) {
      throw NotImplementedExceptions.withCodeAndMessage("J2ni0Q", "Multiple guides are found:\n{0}",
          guides.stream().map(Object::toString).collect(Collectors.joining("\n"))
      );
    }
    return new CallGuidePlanImpl(guides.get(0));
  }

  private List<SystemGuide<?, ?>> findGuides(
      GuideType guideType,
      Rid cid,
      Class<?> sourceClass,
      ReflectionForm targetForm
  ) {
    List<SystemGuide<?, ?>> guides = guideProvider.findGuides(cid, guideType, sourceClass, targetForm);
    if (guides.isEmpty()) {
      Class<?> domainClass = ReflectionFunctions.getReflectionDomainClass(sourceClass);
      Rid originDomainChannelId = ChannelFunctions.getOriginDomainChannelId(domainClass, cid);
      if (originDomainChannelId != null) {
        guides = guideProvider.findGuides(originDomainChannelId, guideType, sourceClass, targetForm);
      }
    }
    return guides;
  }

  private List<SystemGuide<?, ?>> findGuides(
      GuideType guideType,
      Rid cid,
      tech.intellispaces.core.Reflection source,
      ReflectionForm targetForm
  ) {
    List<SystemGuide<?, ?>> guides = new ArrayList<>();
    guides.addAll(guideProvider.findGuides(cid, guideType, source.getClass(), targetForm));
    return guides;
  }

  private GuideType getGuideType(TaskPlanType planType) {
    return switch (TaskPlanTypes.of(planType)) {
      case MapSourceSpecifiedClassThruIdentifiedChannel -> GuideTypes.Mapper;
      case MoveSourceSpecifiedClassThruIdentifiedChannel -> GuideTypes.Mover;
      case MapOfMovingSourceSpecifiedClassThruIdentifiedChannel -> GuideTypes.MapperOfMoving;
      case MapSpecifiedSourceToSpecifiedTargetDomainAndClass -> GuideTypes.Mapper;
      default -> throw NotImplementedExceptions.withCode("5cYSWA");
    };
  }
}
