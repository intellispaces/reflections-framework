package tech.intellispaces.reflections.framework.engine.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.core.Channels;
import tech.intellispaces.core.Domains;
import tech.intellispaces.core.OntologyRepository;
import tech.intellispaces.core.ReflectionChannel;
import tech.intellispaces.core.ReflectionDomain;
import tech.intellispaces.core.ReflectionPoint;
import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.engine.Engine;
import tech.intellispaces.reflections.framework.guide.GuideType;
import tech.intellispaces.reflections.framework.guide.GuideTypes;
import tech.intellispaces.reflections.framework.guide.SystemGuide;
import tech.intellispaces.reflections.framework.node.ReflectionsNodeFunctions;
import tech.intellispaces.reflections.framework.reflection.NativeForeignReflectionPoint;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;
import tech.intellispaces.reflections.framework.reflection.SystemReflection;
import tech.intellispaces.reflections.framework.settings.DomainAssignments;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;
import tech.intellispaces.reflections.framework.system.GuideProvider;
import tech.intellispaces.reflections.framework.system.TraverseAnalyzer;
import tech.intellispaces.reflections.framework.task.plan.AscendAndExecutePlanImpl;
import tech.intellispaces.reflections.framework.task.plan.CallGuidePlanImpl;
import tech.intellispaces.reflections.framework.task.plan.ExecutionTraversePlan;
import tech.intellispaces.reflections.framework.task.plan.MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlan;
import tech.intellispaces.reflections.framework.task.plan.MapOfMovingSourceSpecifiedClassThruIdentifiedChannelPlanImpl;
import tech.intellispaces.reflections.framework.task.plan.MapSourceSpecifiedClassThruIdentifiedChannelPlan;
import tech.intellispaces.reflections.framework.task.plan.MapSourceSpecifiedClassThruIdentifiedChannelPlanImpl;
import tech.intellispaces.reflections.framework.task.plan.MapSpecifiedSourceAndQualifierToSpecifiedTargetDomainAndClassPlan;
import tech.intellispaces.reflections.framework.task.plan.MapSpecifiedSourceAndQualifierToSpecifiedTargetDomainAndClassPlanImpl;
import tech.intellispaces.reflections.framework.task.plan.MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan;
import tech.intellispaces.reflections.framework.task.plan.MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlanImpl;
import tech.intellispaces.reflections.framework.task.plan.MoveSourceSpecifiedClassThruIdentifiedChannelPlan;
import tech.intellispaces.reflections.framework.task.plan.MoveSourceSpecifiedClassThruIdentifiedChannelPlanImpl;
import tech.intellispaces.reflections.framework.task.plan.MoveSpecifiedSourceAndQualifierThruChannel1Plan;
import tech.intellispaces.reflections.framework.task.plan.MoveSpecifiedSourceAndQualifierThruChannel1PlanImpl;
import tech.intellispaces.reflections.framework.task.plan.TaskPlanType;
import tech.intellispaces.reflections.framework.task.plan.TaskPlanTypes;
import tech.intellispaces.reflections.framework.task.plan.TraversePlan;
import tech.intellispaces.reflections.framework.task.plan.TraverseSourceSpecifiedClassThruIdentifierChannelTraversePlan;

class TraverseAnalyzerImpl implements TraverseAnalyzer {
  private final OntologyRepository ontologyRepository;
  private final GuideProvider guideProvider;
  private Engine engine;

  public TraverseAnalyzerImpl(
      OntologyRepository ontologyRepository,
      GuideProvider guideProvider
  ) {
    this.ontologyRepository = ontologyRepository;
    this.guideProvider = guideProvider;
  }

  void setEngine(Engine engine) {
    this.engine = engine;
  }

  @Override
  public MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlan buildMapToDomainPlan(
      ReflectionPoint source,
      ReflectionDomain targetDomain,
      Class<?> targetClass
  ) {
    var declarativePlan = new MapSpecifiedSourceToSpecifiedTargetDomainAndClassPlanImpl(
        source, targetDomain, targetClass
    );
    buildPreliminaryExecutionPlan(declarativePlan);
    return declarativePlan;
  }

  @Override
  public MapSpecifiedSourceAndQualifierToSpecifiedTargetDomainAndClassPlan buildMapToDomainPlan(
      ReflectionPoint source,
      ReflectionDomain targetDomain,
      Object qualifier,
      Class<?> targetClass
  ) {
    var declarativePlan = new MapSpecifiedSourceAndQualifierToSpecifiedTargetDomainAndClassPlanImpl(
        source, targetDomain, qualifier, targetClass
    );
    buildPreliminaryExecutionPlan(declarativePlan);
    return declarativePlan;
  }

  @Override
  public MoveSpecifiedSourceAndQualifierThruChannel1Plan buildMoveThruChannel1Plan(
      Object source, Rid cid, Object qualifier
  ) {
    var declarativePlan = new MoveSpecifiedSourceAndQualifierThruChannel1PlanImpl(
        source, Channels.build().cid(cid).get(), qualifier
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

  private void buildPreliminaryExecutionPlan(MapSpecifiedSourceAndQualifierToSpecifiedTargetDomainAndClassPlan plan) {
    buildExecutionPlan(plan);
  }

  private void buildPreliminaryExecutionPlan(MoveSpecifiedSourceAndQualifierThruChannel1Plan plan) {
    buildExecutionPlan(plan);
  }

  private void buildPreliminaryExecutionPlan(
      TraverseSourceSpecifiedClassThruIdentifierChannelTraversePlan plan,
      Class<?> sourceClass,
      ReflectionForm targetForm
  ) {
    buildExecutionPlan(plan, sourceClass, targetForm);
  }

  @Override
  public ExecutionTraversePlan buildExecutionPlan(
      TraverseSourceSpecifiedClassThruIdentifierChannelTraversePlan plan,
      Class<?> sourceClass,
      ReflectionForm targetForm
  ) {
    return buildExecutionPlan(plan, sourceClass, null, targetForm);
  }

  @Override
  public ExecutionTraversePlan buildExecutionPlan(
      TraverseSourceSpecifiedClassThruIdentifierChannelTraversePlan plan,
      Object source,
      ReflectionForm targetForm
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
    ReflectionDomain sourceDomain = engine.wrapToSystemReflection(plan.source().asPoint().domain()).asDomain();
    ReflectionChannel channel = findChannel(sourceDomain, plan.targetDomain());
    if (channel == null) {
      for (ReflectionDomain parentDomain : sourceDomain.parentDomains()) {
        channel = findChannel(parentDomain, plan.targetDomain());
        if (channel != null) {
          break;
        }
      }
    }
    if (channel == null) {
      return null;
    }

    ExecutionTraversePlan executionPlan = buildExecutionTraversePlan(
        plan.type(), channel.rid(), plan.source().getClass(), ReflectionForms.Reflection
    );
    if (executionPlan == null) {
      ReflectionPoint source = plan.source();
      ReflectionPoint registeredReflection = ontologyRepository.findReflection(source.rid(), source.domainName());
      if (registeredReflection != null) {
        executionPlan = buildExecutionTraversePlan(
            plan.type(), channel.rid(), registeredReflection.getClass(), ReflectionForms.Reflection
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

  @Override
  public TraversePlan buildExecutionPlan(
      MapSpecifiedSourceAndQualifierToSpecifiedTargetDomainAndClassPlan plan
  ) {
    ReflectionDomain sourceDomain = plan.source().asPoint().domain();
    ReflectionChannel channel = findChannel(sourceDomain, plan.targetDomain());
    if (channel == null) {
      for (ReflectionDomain parentDomain : sourceDomain.parentDomains()) {
        channel = findChannel(parentDomain, plan.targetDomain());
        if (channel != null) {
          break;
        }
      }
    }
    if (channel == null) {
      return null;
    }

    ExecutionTraversePlan executionPlan = buildExecutionTraversePlan(
        plan.type(), channel.rid(), plan.source().getClass(), ReflectionForms.Reflection
    );
    if (executionPlan == null) {
      ReflectionPoint source = plan.source();
      ReflectionPoint registeredReflection = ontologyRepository.findReflection(source.rid(), source.domainName());
      if (registeredReflection != null) {
        executionPlan = buildExecutionTraversePlan(
            plan.type(), channel.rid(), registeredReflection.getClass(), ReflectionForms.Reflection
        );
        if (executionPlan != null) {
          var replaceSourcePlan = new MapSpecifiedSourceAndQualifierToSpecifiedTargetDomainAndClassPlanImpl(
              registeredReflection,
              plan.targetDomain(),
              plan.qualifier(),
              plan.targetClass()
          );
          replaceSourcePlan.setExecutionPlan(executionPlan);
          return replaceSourcePlan;
        }
      }
    }
    return executionPlan;
  }

  @Override
  public TraversePlan buildExecutionPlan(MoveSpecifiedSourceAndQualifierThruChannel1Plan plan) {
    Object source = plan.source();
    Rid cid = plan.channel().cid();

    ExecutionTraversePlan executionPlan = buildExecutionTraversePlan(
        plan.type(), cid, source.getClass(), ReflectionForms.Reflection
    );
    if (executionPlan == null) {
      if (source instanceof ReflectionPoint sourcePoint) {
        ReflectionPoint registeredReflection = ontologyRepository.findReflection(sourcePoint.rid(), sourcePoint.domainName());
        if (registeredReflection != null) {
          return buildExecutionPlan(plan, registeredReflection);
        }
      }
    }
    return executionPlan;
  }

  private @Nullable TraversePlan buildExecutionPlan(
      MoveSpecifiedSourceAndQualifierThruChannel1Plan plan, Object sourcePoint
  ) {
    Rid cid = plan.channel().cid();
    ExecutionTraversePlan executionPlan = buildExecutionTraversePlan(
        plan.type(), cid, sourcePoint.getClass(), ReflectionForms.Reflection
    );
    if (executionPlan != null) {
      var replaceSourcePlan = new MoveSpecifiedSourceAndQualifierThruChannel1PlanImpl(
          sourcePoint,
          plan.channel(),
          plan.qualifier()
      );
      replaceSourcePlan.setExecutionPlan(executionPlan);
      return replaceSourcePlan;
    }
    if (sourcePoint instanceof NativeForeignReflectionPoint nativeReflectionPoint) {
      return buildExecutionPlan(plan, nativeReflectionPoint.foreignPoint());
    }
    return null;
  }

  private @Nullable ReflectionChannel findChannel(ReflectionDomain sourceDomain, ReflectionDomain targetDomain) {
    if (sourceDomain == null) {
      sourceDomain = Domains.create(
          ReflectionsNodeFunctions.ontologyReference().getDomainByType(DomainAssignments.Notion).domainName()
      );
    }
    ReflectionChannel channel = ontologyRepository.findChannel(sourceDomain, targetDomain);
    if (channel == null && sourceDomain.borrowedDomain() != null) {
      ReflectionDomain borrowedSourceDomain = sourceDomain.borrowedDomain();
      if (borrowedSourceDomain != null) {
        channel = ontologyRepository.findChannel(borrowedSourceDomain, targetDomain);
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

  private List<SystemGuide<?, ?>> findGuides(
      GuideType guideType,
      Rid cid,
      Class<?> sourceClass,
      ReflectionForm targetForm
  ) {
    List<SystemGuide<?, ?>> guides = guideProvider.findGuides(cid, guideType, sourceClass, targetForm);
    if (guides.isEmpty()) {
      Class<?> domainClass = ReflectionFunctions.getReflectionDomainClass(sourceClass);
      if (domainClass != null) {
        Rid originDomainChannelId = ChannelFunctions.getOriginDomainChannelId(domainClass, cid);
        if (originDomainChannelId != null) {
          guides = guideProvider.findGuides(originDomainChannelId, guideType, sourceClass, targetForm);
        }
      }
    }
    return guides;
  }

  private GuideType getGuideType(TaskPlanType planType) {
    return switch (TaskPlanTypes.of(planType)) {
      case MapSourceSpecifiedClassThruIdentifiedChannel -> GuideTypes.Mapper;
      case MoveSourceSpecifiedClassThruIdentifiedChannel -> GuideTypes.Mover;
      case MapOfMovingSourceSpecifiedClassThruIdentifiedChannel -> GuideTypes.MapperOfMoving;
      case MapSpecifiedSourceToSpecifiedTargetDomainAndClass -> GuideTypes.Mapper;
      case MapSpecifiedSourceAndQualifierToSpecifiedTargetDomainAndClass -> GuideTypes.Mapper;
      case MoveSpecifiedSourceAndQualifierThruChannel1 -> GuideTypes.Mover;
      default -> throw NotImplementedExceptions.withCode("5cYSWA");
    };
  }
}
