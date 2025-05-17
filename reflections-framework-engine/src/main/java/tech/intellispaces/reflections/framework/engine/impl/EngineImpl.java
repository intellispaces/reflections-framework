package tech.intellispaces.reflections.framework.engine.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.google.auto.service.AutoService;

import tech.intellispaces.actions.Action;
import tech.intellispaces.actions.Action0;
import tech.intellispaces.actions.Action1;
import tech.intellispaces.actions.Action10;
import tech.intellispaces.actions.Action2;
import tech.intellispaces.actions.Action3;
import tech.intellispaces.actions.Action4;
import tech.intellispaces.actions.Action5;
import tech.intellispaces.actions.Action6;
import tech.intellispaces.actions.Action7;
import tech.intellispaces.actions.Action8;
import tech.intellispaces.actions.Action9;
import tech.intellispaces.actions.cache.CachedSupplierActions;
import tech.intellispaces.actions.delegate.DelegateActions;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.type.Type;
import tech.intellispaces.reflections.framework.action.TraverseActions;
import tech.intellispaces.reflections.framework.channel.Channel0;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.channel.Channel2;
import tech.intellispaces.reflections.framework.channel.Channel3;
import tech.intellispaces.reflections.framework.channel.Channel4;
import tech.intellispaces.reflections.framework.engine.Engine;
import tech.intellispaces.reflections.framework.reflection.ReflectionBroker;
import tech.intellispaces.reflections.framework.reflection.ReflectionBrokerImpl;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.reflections.framework.reflection.ReflectionFunctions;
import tech.intellispaces.reflections.framework.reflection.ReflectionImplementationMethod;
import tech.intellispaces.reflections.framework.reflection.ReflectionImplementationMethodPurposes;
import tech.intellispaces.reflections.framework.reflection.ReflectionImplementationType;
import tech.intellispaces.reflections.framework.space.channel.ChannelFunctions;
import tech.intellispaces.reflections.framework.system.FactoryRegistry;
import tech.intellispaces.reflections.framework.system.Injection;
import tech.intellispaces.reflections.framework.system.Module;
import tech.intellispaces.reflections.framework.system.injection.AutoGuideInjections;
import tech.intellispaces.reflections.framework.system.injection.GuideInjections;
import tech.intellispaces.reflections.framework.traverse.MappingOfMovingTraverse;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;
import tech.intellispaces.reflections.framework.traverse.plan.DeclarativeTraversePlan;
import tech.intellispaces.reflections.framework.traverse.plan.TraverseAnalyzer;
import tech.intellispaces.reflections.framework.traverse.plan.TraverseExecutor;
import tech.intellispaces.reflections.framework.traverse.plan.TraversePlan;

@AutoService(Engine.class)
public class EngineImpl implements Engine {
  private TraverseAnalyzer traverseAnalyzer;
  private TraverseExecutor traverseExecutor;
  private FactoryRegistry factoryRegistry;

  public EngineImpl() {
    factoryRegistry = new LocalFactoryRegistry();
  }

  public EngineImpl(
      TraverseAnalyzer traverseAnalyzer,
      TraverseExecutor traverseExecutor,
      FactoryRegistry factoryRegistry
  ) {
    this.traverseAnalyzer = traverseAnalyzer;
    this.traverseExecutor = traverseExecutor;
    this.factoryRegistry = factoryRegistry;
  }

  @Override
  public void start() {

  }

  @Override
  public void start(String[] args) {

  }

  @Override
  public void stop() {

  }

  @Override
  public Module createModule(List<Class<?>> unitClasses, String[] args) {
    return ModuleLoader.loadModule(unitClasses, args);
  }





  @Override
  @SuppressWarnings("unchecked")
  public <S, T> T mapThruChannel0(S source, String cid) {
    DeclarativeTraversePlan traversePlan = traverseAnalyzer.buildMapThruChannel0Plan(
        ReflectionFunctions.getReflectionClass(source.getClass()), cid, ReflectionForms.Reflection);
    return (T) traversePlan.execute(source, traverseExecutor);
  }

  @Override
  public <S, T, C extends Channel0 & MappingTraverse> T mapThruChannel0(S source, Class<C> channelClass) {
    return mapThruChannel0(source, ChannelFunctions.getChannelId(channelClass));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, T, Q> T mapThruChannel1(S source, String cid, Q qualifier) {
    DeclarativeTraversePlan traversePlan = traverseAnalyzer.buildMapThruChannel1Plan(
        ReflectionFunctions.getReflectionClass(source.getClass()), cid, ReflectionForms.Reflection);
    return (T) traversePlan.execute(source, qualifier, traverseExecutor);
  }

  @Override
  public <S, T, Q, C extends Channel1 & MappingTraverse> T mapThruChannel1(S source, Class<C> channelClass, Q qualifier) {
    return mapThruChannel1(source, ChannelFunctions.getChannelId(channelClass), qualifier);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, R> R moveThruChannel0(S source, String cid) {
    TraversePlan traversePlan = traverseAnalyzer.buildMoveThruChannel0Plan(
        ReflectionFunctions.getReflectionClass(source.getClass()), cid, ReflectionForms.Reflection);
    return (R) traversePlan.execute(source, traverseExecutor);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, R, Q> R moveThruChannel1(S source, String cid, Q qualifier) {
    TraversePlan traversePlan = traverseAnalyzer.buildMoveThruChannel1Plan(
        ReflectionFunctions.getReflectionClass(source.getClass()), cid, ReflectionForms.Reflection);
    return (R) traversePlan.execute(source, qualifier, traverseExecutor);
  }

  @Override
  public <S, R, Q, C extends Channel1 & MappingOfMovingTraverse> R mapOfMovingThruChannel1(
      S source, Class<C> channelClass, Q qualifier
  ) {
    return mapOfMovingThruChannel1(source, ChannelFunctions.getChannelId(channelClass), qualifier);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S, R, Q> R mapOfMovingThruChannel1(S source, String cid, Q qualifier) {
    TraversePlan traversePlan = traverseAnalyzer.buildMapOfMovingThruChannel1Plan(
        ReflectionFunctions.getReflectionClass(source.getClass()), cid, ReflectionForms.Reflection);
    return (R) traversePlan.execute(source, qualifier, traverseExecutor);
  }

  @Override
  public <R, W extends R> ReflectionImplementationType registerReflectionImplementationType(
          Class<W> reflectionWrapperClass,
          Class<R> reflectionImplClass,
          ReflectionImplementationMethod... methods
  ) {
    return new tech.intellispaces.reflections.framework.engine.impl.ReflectionImplementationType(
            reflectionImplClass,
            reflectionWrapperClass,
        Arrays.asList(methods),
        buildMethodActions(reflectionImplClass, methods),
        buildGuideActions(methods),
        buildInjections(methods)
    );
  }

  @Override
  public <W> ReflectionBroker registerReflection(W reflectionWrapper, ReflectionImplementationType type) {
    var typeImpl = (tech.intellispaces.reflections.framework.engine.impl.ReflectionImplementationType) type;
    return new ReflectionBrokerImpl(
        type,
        typeImpl.methodActions(),
        typeImpl.guideActions(),
        typeImpl.injections()
    );
  }

  @Override
  public <R> Action0<R> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(targetDomainClass, contractType, targetReflectionType);
  }

  @Override
  public <R, Q> Action1<R, Q> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q> contractQualifierType,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(
        targetDomainClass, contractType, contractQualifierType, targetReflectionType
    );
  }

  @Override
  public <R, Q1, Q2> Action2<R, Q1, Q2> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        targetReflectionType
    );
  }

  @Override
  public <R, Q1, Q2, Q3> Action3<R, Q1, Q2, Q3> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        targetReflectionType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4> Action4<R, Q1, Q2, Q3, Q4> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        contractQualifierType4,
        targetReflectionType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4, Q5> Action5<R, Q1, Q2, Q3, Q4, Q5> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        contractQualifierType4,
        contractQualifierType5,
        targetReflectionType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4, Q5, Q6> Action6<R, Q1, Q2, Q3, Q4, Q5, Q6> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        contractQualifierType4,
        contractQualifierType5,
        contractQualifierType6,
        targetReflectionType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7> Action7<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<Q7> contractQualifierType7,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        contractQualifierType4,
        contractQualifierType5,
        contractQualifierType6,
        contractQualifierType7,
        targetReflectionType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8> Action8<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<Q7> contractQualifierType7,
      Type<Q8> contractQualifierType8,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        contractQualifierType4,
        contractQualifierType5,
        contractQualifierType6,
        contractQualifierType7,
        contractQualifierType8,
        targetReflectionType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9> Action9<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<Q7> contractQualifierType7,
      Type<Q8> contractQualifierType8,
      Type<Q9> contractQualifierType9,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        contractQualifierType4,
        contractQualifierType5,
        contractQualifierType6,
        contractQualifierType7,
        contractQualifierType8,
        contractQualifierType9,
        targetReflectionType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10> Action10<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10> getFactoryAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<Q7> contractQualifierType7,
      Type<Q8> contractQualifierType8,
      Type<Q9> contractQualifierType9,
      Type<Q10> contractQualifierType10,
      Type<R> targetReflectionType
  ) {
    return factoryRegistry.getFactoryAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        contractQualifierType4,
        contractQualifierType5,
        contractQualifierType6,
        contractQualifierType7,
        contractQualifierType8,
        contractQualifierType9,
        contractQualifierType10,
        targetReflectionType
    );

  }

  private Action[] buildMethodActions(Class<?> reflectionClass, ReflectionImplementationMethod... methods) {
    if (methods == null || methods.length == 0) {
      return new Action[0];
    }

    List<Action> actions = new ArrayList<>(methods.length);
    for (ReflectionImplementationMethod method : methods) {
      if (ReflectionImplementationMethodPurposes.TraverseMethod.is(method.purpose())) {
        Action action = switch (method.paramClasses().size()) {
          case 0 -> buildMethodAction0(reflectionClass, method);
          case 1 -> buildMethodAction1(reflectionClass, method);
          case 2 -> buildMethodAction2(reflectionClass, method);
          case 3 -> buildMethodAction3(reflectionClass, method);
          case 4 -> buildMethodAction4(reflectionClass, method);
          default -> throw NotImplementedExceptions.withCode("CoVntQ==");
        };
        actions.add(action);
      }
    }
    return actions.toArray(new Action[0]);
  }

  @SuppressWarnings("unchecked")
  private Action buildMethodAction0(Class<?> reflectionClass, ReflectionImplementationMethod method) {
    if (method.traverseType().isMapping()) {
      return DelegateActions.delegateAction1(CachedSupplierActions.get(TraverseActions::mapThruChannel0,
          reflectionClass,
          (Class<Channel0>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    } else if (method.traverseType().isMoving()) {
      return DelegateActions.delegateAction1(CachedSupplierActions.get(TraverseActions::moveThruChannel0,
          reflectionClass,
          (Class<Channel0>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    } else {
      return DelegateActions.delegateAction1(CachedSupplierActions.get(TraverseActions::mapOfMovingThruChannel0,
          reflectionClass,
          (Class<Channel0>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    }
  }

  @SuppressWarnings("unchecked")
  private Action buildMethodAction1(Class<?> reflectionClass, ReflectionImplementationMethod method) {
    if (method.traverseType().isMapping()) {
      return DelegateActions.delegateAction2(CachedSupplierActions.get(TraverseActions::mapThruChannel1,
          reflectionClass,
          (Class<Channel1>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    } else if (method.traverseType().isMoving()) {
      return DelegateActions.delegateAction2(CachedSupplierActions.get(TraverseActions::moveThruChannel1,
          reflectionClass,
          (Class<Channel1>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    } else {
      return DelegateActions.delegateAction2(CachedSupplierActions.get(TraverseActions::mapOfMovingThruChannel1,
          reflectionClass,
          (Class<Channel1>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    }
  }

  @SuppressWarnings("unchecked")
  private Action buildMethodAction2(Class<?> reflectionClass, ReflectionImplementationMethod method) {
    if (method.traverseType().isMapping()) {
      return DelegateActions.delegateAction3(CachedSupplierActions.get(TraverseActions::mapThruChannel2,
          reflectionClass,
          (Class<Channel2>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    } else if (method.traverseType().isMoving()) {
      return DelegateActions.delegateAction3(CachedSupplierActions.get(TraverseActions::moveThruChannel2,
          reflectionClass,
          (Class<Channel2>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    } else {
      return DelegateActions.delegateAction3(CachedSupplierActions.get(TraverseActions::mapOfMovingThruChannel2,
          reflectionClass,
          (Class<Channel2>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    }
  }

  @SuppressWarnings("unchecked")
  private Action buildMethodAction3(Class<?> reflectionClass, ReflectionImplementationMethod method) {
    if (method.traverseType().isMapping()) {
      return DelegateActions.delegateAction4(CachedSupplierActions.get(TraverseActions::mapThruChannel3,
          reflectionClass,
          (Class<Channel3>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    } else if (method.traverseType().isMoving()) {
      return DelegateActions.delegateAction4(CachedSupplierActions.get(TraverseActions::moveThruChannel3,
          reflectionClass,
          (Class<Channel3>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    } else {
      return DelegateActions.delegateAction4(CachedSupplierActions.get(TraverseActions::mapOfMovingThruChannel3,
          reflectionClass,
          (Class<Channel3>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    }
  }

  @SuppressWarnings("unchecked")
  private Action buildMethodAction4(Class<?> reflectionClass, ReflectionImplementationMethod method) {
    if (method.traverseType().isMapping()) {
      throw NotImplementedExceptions.withCode("GYhrXA==");
    } else if (method.traverseType().isMoving()) {
      throw NotImplementedExceptions.withCode("8xOuXA==");
    } else {
      return DelegateActions.delegateAction5(CachedSupplierActions.get(TraverseActions::mapOfMovingThruChannel4,
          reflectionClass,
          (Class<Channel4>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    }
  }

  private Action[] buildGuideActions(ReflectionImplementationMethod... methods) {
    if (methods == null || methods.length == 0) {
      return new Action[0];
    }

    int maxOrdinal = Arrays.stream(methods)
        .filter(m -> ReflectionImplementationMethodPurposes.TraverseMethod.is(m.purpose()))
        .map(ReflectionImplementationMethod::traverseOrdinal)
        .max(Comparator.naturalOrder())
        .orElse(0);
    Action[] actions = new Action[maxOrdinal + 1];
    for (ReflectionImplementationMethod method : methods) {
      if (ReflectionImplementationMethodPurposes.GuideMethod.is(method.purpose())) {
        actions[method.traverseOrdinal()] = method.action();
      }
    }
    return actions;
  }

  private Injection[] buildInjections(ReflectionImplementationMethod... methods) {
    if (methods == null || methods.length == 0) {
      return new Injection[0];
    }

    int maxOrdinal = Arrays.stream(methods)
        .filter(m -> ReflectionImplementationMethodPurposes.InjectionMethod.is(m.purpose()))
        .map(ReflectionImplementationMethod::injectionOrdinal)
        .max(Comparator.naturalOrder())
        .orElse(0);
    Injection[] injections = new Injection[maxOrdinal + 1];
    for (ReflectionImplementationMethod method : methods) {
      if (ReflectionImplementationMethodPurposes.InjectionMethod.is(method.purpose())) {
        injections[method.injectionOrdinal()] = buildInjection(method);
      }
    }
    return injections;
  }

  private Injection buildInjection(ReflectionImplementationMethod method) {
    if ("autoguide".equals(method.injectionKind())) {
      return AutoGuideInjections.get(null, method.injectionName(), method.injectionType());
    } else if ("specguide".equals(method.injectionKind())) {
      return GuideInjections.get(null, method.injectionName(), method.injectionType());
    } else {
      throw NotImplementedExceptions.withCode("DfsonQ==");
    }
  }
}
