package tech.intellispaces.reflections.framework.engine.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.core.Channel;
import tech.intellispaces.core.Domain;
import tech.intellispaces.core.Rid;
import tech.intellispaces.core.repository.OntologyRepository;
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
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;
import tech.intellispaces.reflections.framework.system.GuideProvider;
import tech.intellispaces.reflections.framework.system.ReflectionRegistry;
import tech.intellispaces.reflections.framework.system.TraverseAnalyzer;
import tech.intellispaces.reflections.framework.traverse.plan.AscendAndExecutePlan1Impl;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide0PlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide1PlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide2PlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide3PlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.CallGuide4PlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.ExecutionTraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel0Plan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel0PlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel1TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel2TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel3TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel3TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel4TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapOfMovingSpecifiedClassSourceThruIdentifiedChannel4TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecifiedClassSourceThruIdentifiedChannel0TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecifiedClassSourceThruIdentifiedChannel0TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecifiedClassSourceThruIdentifiedChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecifiedClassSourceThruIdentifiedChannel1TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecifiedClassSourceThruIdentifiedChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecifiedClassSourceThruIdentifiedChannel2TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecifiedClassSourceThruIdentifiedChannel3TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecifiedClassSourceThruIdentifiedChannel3TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MoveSpecifiedClassSourceThruIdentifiedChannel0TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveSpecifiedClassSourceThruIdentifiedChannel0TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MoveSpecifiedClassSourceThruIdentifiedChannel1TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveSpecifiedClassSourceThruIdentifiedChannel1TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MoveSpecifiedClassSourceThruIdentifiedChannel2TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveSpecifiedClassSourceThruIdentifiedChannel2TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.MoveSpecifiedClassSourceThruIdentifiedChannel3TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.MoveSpecifiedClassSourceThruIdentifiedChannel3TraversePlanImpl;
import tech.intellispaces.reflections.framework.traverse.plan.TraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.TraversePlanType;
import tech.intellispaces.reflections.framework.traverse.plan.TraversePlanTypes;
import tech.intellispaces.reflections.framework.traverse.plan.TraverseSpecifiedClassSourceThruIdentifierChannelTraversePlan;

class TraverseAnalyzerImpl implements TraverseAnalyzer {
  private final OntologyRepository spaceRepository;
  private final GuideProvider guideProvider;
  private final ReflectionRegistry reflectionRegistry;

  public TraverseAnalyzerImpl(
      OntologyRepository spaceRepository,
      GuideProvider guideProvider,
      ReflectionRegistry reflectionRegistry
  ) {
    this.spaceRepository = spaceRepository;
    this.guideProvider = guideProvider;
    this.reflectionRegistry = reflectionRegistry;
  }

  @Override
  public MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlan buildMapToTraversePlan(
      tech.intellispaces.core.Reflection source,
      Domain targetDomain,
      Class<?> targetClass
  ) {
    var declarativePlan = new MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlanImpl(
        source, targetDomain, targetClass
    );
    buildPreliminaryExecutionPlan(declarativePlan);
    return declarativePlan;
  }

  @Override
  public MapSpecifiedClassSourceThruIdentifiedChannel0TraversePlan buildMapThruChannel0TraversePlan(
      Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  ) {
    var declarativePlan = new MapSpecifiedClassSourceThruIdentifiedChannel0TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapSpecifiedClassSourceThruIdentifiedChannel1TraversePlan buildMapThruChannel1TraversePlan(
    Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  ) {
    var declarativePlan = new MapSpecifiedClassSourceThruIdentifiedChannel1TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapSpecifiedClassSourceThruIdentifiedChannel2TraversePlan buildMapThruChannel2TraversePlan(
    Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  ) {
    var declarativePlan = new MapSpecifiedClassSourceThruIdentifiedChannel2TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapSpecifiedClassSourceThruIdentifiedChannel3TraversePlan buildMapThruChannel3TraversePlan(
    Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  ) {
    var declarativePlan = new MapSpecifiedClassSourceThruIdentifiedChannel3TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MoveSpecifiedClassSourceThruIdentifiedChannel0TraversePlan buildMoveThruChannel0TraversePlan(
    Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  ) {
    var declarativePlan = new MoveSpecifiedClassSourceThruIdentifiedChannel0TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MoveSpecifiedClassSourceThruIdentifiedChannel1TraversePlan buildMoveThruChannel1TraversePlan(
    Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  ) {
    var declarativePlan = new MoveSpecifiedClassSourceThruIdentifiedChannel1TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MoveSpecifiedClassSourceThruIdentifiedChannel2TraversePlan buildMoveThruChannel2TraversePlan(
    Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  ) {
    var declarativePlan = new MoveSpecifiedClassSourceThruIdentifiedChannel2TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MoveSpecifiedClassSourceThruIdentifiedChannel3TraversePlan buildMoveThruChannel3TraversePlan(
    Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  ) {
    var declarativePlan = new MoveSpecifiedClassSourceThruIdentifiedChannel3TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingSpecifiedClassSourceThruIdentifiedChannel0Plan buildMapOfMovingThruChannel0TraversePlan(
      Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  ) {
    var declarativePlan = new MapOfMovingSpecifiedClassSourceThruIdentifiedChannel0PlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingSpecifiedClassSourceThruIdentifiedChannel1TraversePlan buildMapOfMovingThruChannel1TraversePlan(
      Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  ) {
    var declarativePlan = new MapOfMovingSpecifiedClassSourceThruIdentifiedChannel1TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingSpecifiedClassSourceThruIdentifiedChannel2TraversePlan buildMapOfMovingThruChannel2TraversePlan(
      Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  ) {
    var declarativePlan = new MapOfMovingSpecifiedClassSourceThruIdentifiedChannel2TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingSpecifiedClassSourceThruIdentifiedChannel3TraversePlan buildMapOfMovingThruChannel3TraversePlan(
      Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  ) {
    var declarativePlan = new MapOfMovingSpecifiedClassSourceThruIdentifiedChannel3TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  @Override
  public MapOfMovingSpecifiedClassSourceThruIdentifiedChannel4TraversePlan buildMapOfMovingThruChannel4TraversePlan(
      Class<?> sourceClass, Rid cid, ReflectionForm targetForm
  ) {
    var declarativePlan = new MapOfMovingSpecifiedClassSourceThruIdentifiedChannel4TraversePlanImpl(sourceClass, cid);
    buildPreliminaryExecutionPlan(declarativePlan, sourceClass, targetForm);
    return declarativePlan;
  }

  private void buildPreliminaryExecutionPlan(
      MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlan plan
  ) {
    buildExecutionPlan(plan);
  }

  private void buildPreliminaryExecutionPlan(
      TraverseSpecifiedClassSourceThruIdentifierChannelTraversePlan plan, Class<?> sourceClass, ReflectionForm targetForm
  ) {
    buildExecutionPlan(plan, sourceClass, targetForm);
  }

  @Override
  public ExecutionTraversePlan buildExecutionPlan(
      TraverseSpecifiedClassSourceThruIdentifierChannelTraversePlan plan, Class<?> sourceClass, ReflectionForm targetForm
  ) {
    return buildExecutionPlan(plan, sourceClass, null, targetForm);
  }

  @Override
  public ExecutionTraversePlan buildExecutionPlan(
      TraverseSpecifiedClassSourceThruIdentifierChannelTraversePlan plan, Object source, ReflectionForm targetForm
  ) {
    return buildExecutionPlan(plan, source.getClass(), source, targetForm);
  }

  private ExecutionTraversePlan buildExecutionPlan(
      TraverseSpecifiedClassSourceThruIdentifierChannelTraversePlan plan,
      Class<?> sourceClass,
      Object source,
      ReflectionForm targetForm
  ) {
    ExecutionTraversePlan executionPlan = plan.executionPlan(sourceClass);
    if (executionPlan != null) {
      return executionPlan;
    }

    TraversePlanType planType = plan.type();
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
      plan.setExecutionPlan(sourceClass, executionPlan);
      return executionPlan;
    } else {
      executionPlan = buildExecutionTraversePlan(planType, cid, reflectionClass, targetForm);
      if (executionPlan != null) {
        plan.setExecutionPlan(sourceClass, executionPlan);
        plan.setExecutionPlan(reflectionClass, executionPlan);
        return executionPlan;
      }
    }

    if (source != null) {
      var sourceReflection = (Reflection<?>) source;
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
            executionPlan = new AscendAndExecutePlan1Impl(executionPlan);
            break;
          }
        }
      }
    }
    return executionPlan;
  }

  @Override
  public TraversePlan buildExecutionPlan(
      MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlan plan
  ) {
    Channel channel = spaceRepository.findChannel(plan.source().domain(), plan.targetDomain());
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
          var replaceSourcePlan = new MapSpecifiedSourceToSpecifiedTargetDomainAndClassTraversePlanImpl(
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

  private ExecutionTraversePlan buildExecutionTraversePlan(
      TraversePlanType planType,
      Rid cid,
      Class<?> sourceClass,
      ReflectionForm targetForm
  ) {
    GuideKinds guideKind = getGuideKind(planType);
    List<Guide<?, ?>> guides = findGuides(guideKind, cid, sourceClass, targetForm);
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

  private ExecutionTraversePlan buildExecutionTraversePlan(
      TraversePlanType planType,
      Rid cid,
      tech.intellispaces.core.Reflection source,
      ReflectionForm targetForm
  ) {
    GuideKinds guideKind = getGuideKind(planType);
    List<Guide<?, ?>> guides = findGuides(guideKind, cid, source, targetForm);
    if (guides.isEmpty()) {
      return null;
    }
    if (guides.size() > 1) {
      throw NotImplementedExceptions.withCodeAndMessage("J2ni0Q", "Multiple guides are found:\n{0}",
          guides.stream().map(Object::toString).collect(Collectors.joining("\n"))
      );
    }
    return switch (guideKind) {
      case Mapper0, Mover0, MapperOfMoving0 -> new CallGuide0PlanImpl((Guide0<?, ?>) guides.get(0));
      case Mapper1, Mover1, MapperOfMoving1 -> new CallGuide1PlanImpl((Guide1<?, ?, ?>) guides.get(0));
      case Mapper2, Mover2, MapperOfMoving2 -> new CallGuide2PlanImpl((Guide2<?, ?, ?, ?>) guides.get(0));
      case Mapper3, Mover3, MapperOfMoving3 -> new CallGuide3PlanImpl((Guide3<?, ?, ?, ?, ?>) guides.get(0));
      case Mapper4, Mover4, MapperOfMoving4 -> new CallGuide4PlanImpl((Guide4<?, ?, ?, ?, ?, ?>) guides.get(0));
      default -> throw NotImplementedExceptions.withCode("Ma6ylg");
    };
  }

  private List<Guide<?, ?>> findGuides(
      GuideKind guideKind,
      Rid cid,
      Class<?> sourceClass,
      ReflectionForm targetForm
  ) {
    List<Guide<?, ?>> guides = guideProvider.findGuides(cid, guideKind, sourceClass, targetForm);
    if (guides.isEmpty()) {
      Class<?> domainClass = ReflectionFunctions.getReflectionDomainClass(sourceClass);
      Rid originDomainChannelId = ChannelFunctions.getOriginDomainChannelId(domainClass, cid);
      if (originDomainChannelId != null) {
        guides = guideProvider.findGuides(originDomainChannelId, guideKind, sourceClass, targetForm);
      }
    }
    return guides;
  }

  private List<Guide<?, ?>> findGuides(
      GuideKind guideKind,
      Rid cid,
      tech.intellispaces.core.Reflection source,
      ReflectionForm targetForm
  ) {
    List<Guide<?, ?>> guides = new ArrayList<>();
    guides.addAll(guideProvider.findGuides(cid, guideKind, source.getClass(), targetForm));
    return guides;
  }

  private GuideKinds getGuideKind(TraversePlanType planType) {
    return switch (TraversePlanTypes.of(planType)) {
      case MapSpecifiedClassSourceThruIdentifiedChannel0 -> GuideKinds.Mapper0;
      case MapSpecifiedClassSourceThruIdentifiedChannel1 -> GuideKinds.Mapper1;
      case MapSpecifiedClassSourceThruIdentifiedChannel2 -> GuideKinds.Mapper2;
      case MapSpecifiedClassSourceThruIdentifiedChannel3 -> GuideKinds.Mapper3;
      case MoveSpecifiedClassSourceThruIdentifiedChannel0 -> GuideKinds.Mover0;
      case MoveSpecifiedClassSourceThruIdentifiedChannel1 -> GuideKinds.Mover1;
      case MoveSpecifiedClassSourceThruIdentifiedChannel2 -> GuideKinds.Mover2;
      case MoveSpecifiedClassSourceThruIdentifiedChannel3 -> GuideKinds.Mover3;
      case MapOfMovingSpecifiedClassSourceThruIdentifiedChannel0 -> GuideKinds.MapperOfMoving0;
      case MapOfMovingSpecifiedClassSourceThruIdentifiedChannel1 -> GuideKinds.MapperOfMoving1;
      case MapOfMovingSpecifiedClassSourceThruIdentifiedChannel2 -> GuideKinds.MapperOfMoving2;
      case MapOfMovingSpecifiedClassSourceThruIdentifiedChannel3 -> GuideKinds.MapperOfMoving3;
      case MapOfMovingSpecifiedClassSourceThruIdentifiedChannel4 -> GuideKinds.MapperOfMoving4;
      case MapSpecifiedSourceToSpecifiedTargetDomainAndClass -> GuideKinds.Mapper0;
      default -> throw NotImplementedExceptions.withCode("5cYSWA");
    };
  }
}
