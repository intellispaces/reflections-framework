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
import tech.intellispaces.reflections.framework.engine.ReflectionBroker;
import tech.intellispaces.reflections.framework.engine.UnitBroker;
import tech.intellispaces.reflections.framework.engine.description.ReflectionImplementationMethodDescription;
import tech.intellispaces.reflections.framework.engine.description.ReflectionImplementationMethodPurposes;
import tech.intellispaces.reflections.framework.engine.description.ReflectionImplementationType;
import tech.intellispaces.reflections.framework.engine.description.UnitMethodDescription;
import tech.intellispaces.reflections.framework.reflection.ReflectionForms;
import tech.intellispaces.reflections.framework.system.Injection;
import tech.intellispaces.reflections.framework.system.Module;
import tech.intellispaces.reflections.framework.system.UnitWrapper;
import tech.intellispaces.reflections.framework.system.injection.AutoGuideInjections;
import tech.intellispaces.reflections.framework.system.injection.GuideInjections;

@AutoService(tech.intellispaces.reflections.framework.engine.Engine.class)
public class Engine implements tech.intellispaces.reflections.framework.engine.Engine {
  private final ObjectFactoryRegistry objectFactoryRegistry = new ObjectFactoryRegistry();

  @Override
  public Module createModule(List<Class<?>> unitClasses, String[] args) {
    return ModuleLoader.loadModule(unitClasses, args);
  }

  @Override
  public <R, W extends R> ReflectionImplementationType registerReflectionImplementationType(
          Class<W> reflectionWrapperClass,
          Class<R> reflectionImplClass,
          ReflectionImplementationMethodDescription... methods
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
    return new tech.intellispaces.reflections.framework.engine.impl.ReflectionBroker(
        type,
        typeImpl.methodActions(),
        typeImpl.guideActions(),
        typeImpl.injections()
    );
  }

  @Override
  public <U, W extends UnitWrapper> UnitBroker registerUnit(
      W unitWrapper, Class<U> unitClass, UnitMethodDescription... methods
  ) {
    return UnitFactory.createModule(unitWrapper, unitClass, methods);
  }

  @Override
  public <R> Action0<R> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<R> targetObjectHandleType
  ) {
    return objectFactoryRegistry.objectAssistantAction(targetDomainClass, contractType, targetObjectHandleType);
  }

  @Override
  public <R, Q> Action1<R, Q> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q> contractQualifierType,
      Type<R> targetObjectHandleType
  ) {
    return objectFactoryRegistry.objectAssistantAction(
        targetDomainClass, contractType, contractQualifierType, targetObjectHandleType
    );
  }

  @Override
  public <R, Q1, Q2> Action2<R, Q1, Q2> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<R> targetObjectHandleType
  ) {
    return objectFactoryRegistry.objectAssistantAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        targetObjectHandleType
    );
  }

  @Override
  public <R, Q1, Q2, Q3> Action3<R, Q1, Q2, Q3> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<R> targetObjectHandleType
  ) {
    return objectFactoryRegistry.objectAssistantAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        targetObjectHandleType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4> Action4<R, Q1, Q2, Q3, Q4> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<R> targetObjectHandleType
  ) {
    return objectFactoryRegistry.objectAssistantAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        contractQualifierType4,
        targetObjectHandleType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4, Q5> Action5<R, Q1, Q2, Q3, Q4, Q5> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<R> targetObjectHandleType
  ) {
    return objectFactoryRegistry.objectAssistantAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        contractQualifierType4,
        contractQualifierType5,
        targetObjectHandleType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4, Q5, Q6> Action6<R, Q1, Q2, Q3, Q4, Q5, Q6> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<R> targetObjectHandleType
  ) {
    return objectFactoryRegistry.objectAssistantAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        contractQualifierType4,
        contractQualifierType5,
        contractQualifierType6,
        targetObjectHandleType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7> Action7<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7> objectAssistantAction(
      Class<?> targetDomainClass,
      String contractType,
      Type<Q1> contractQualifierType1,
      Type<Q2> contractQualifierType2,
      Type<Q3> contractQualifierType3,
      Type<Q4> contractQualifierType4,
      Type<Q5> contractQualifierType5,
      Type<Q6> contractQualifierType6,
      Type<Q7> contractQualifierType7,
      Type<R> targetObjectHandleType
  ) {
    return objectFactoryRegistry.objectAssistantAction(
        targetDomainClass,
        contractType,
        contractQualifierType1,
        contractQualifierType2,
        contractQualifierType3,
        contractQualifierType4,
        contractQualifierType5,
        contractQualifierType6,
        contractQualifierType7,
        targetObjectHandleType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8> Action8<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8> objectAssistantAction(
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
      Type<R> targetObjectHandleType
  ) {
    return objectFactoryRegistry.objectAssistantAction(
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
        targetObjectHandleType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9> Action9<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9> objectAssistantAction(
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
      Type<R> targetObjectHandleType
  ) {
    return objectFactoryRegistry.objectAssistantAction(
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
        targetObjectHandleType
    );
  }

  @Override
  public <R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10> Action10<R, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10> objectAssistantAction(
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
      Type<R> targetObjectHandleType
  ) {
    return objectFactoryRegistry.objectAssistantAction(
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
        targetObjectHandleType
    );

  }

  private Action[] buildMethodActions(Class<?> objectHandleClass, ReflectionImplementationMethodDescription... methods) {
    if (methods == null || methods.length == 0) {
      return new Action[0];
    }

    List<Action> actions = new ArrayList<>(methods.length);
    for (ReflectionImplementationMethodDescription method : methods) {
      if (ReflectionImplementationMethodPurposes.TraverseMethod.is(method.purpose())) {
        Action action = switch (method.paramClasses().size()) {
          case 0 -> buildMethodAction0(objectHandleClass, method);
          case 1 -> buildMethodAction1(objectHandleClass, method);
          case 2 -> buildMethodAction2(objectHandleClass, method);
          case 3 -> buildMethodAction3(objectHandleClass, method);
          case 4 -> buildMethodAction4(objectHandleClass, method);
          default -> throw NotImplementedExceptions.withCode("CoVntQ==");
        };
        actions.add(action);
      }
    }
    return actions.toArray(new Action[0]);
  }

  @SuppressWarnings("unchecked")
  private Action buildMethodAction0(Class<?> objectHandleClass, ReflectionImplementationMethodDescription method) {
    if (method.traverseType().isMapping()) {
      return DelegateActions.delegateAction1(CachedSupplierActions.get(TraverseActions::mapThruChannel0,
          objectHandleClass,
          (Class<Channel0>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    } else if (method.traverseType().isMoving()) {
      return DelegateActions.delegateAction1(CachedSupplierActions.get(TraverseActions::moveThruChannel0,
          objectHandleClass,
          (Class<Channel0>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    } else {
      return DelegateActions.delegateAction1(CachedSupplierActions.get(TraverseActions::mapOfMovingThruChannel0,
          objectHandleClass,
          (Class<Channel0>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    }
  }

  @SuppressWarnings("unchecked")
  private Action buildMethodAction1(Class<?> objectHandleClass, ReflectionImplementationMethodDescription method) {
    if (method.traverseType().isMapping()) {
      return DelegateActions.delegateAction2(CachedSupplierActions.get(TraverseActions::mapThruChannel1,
          objectHandleClass,
          (Class<Channel1>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    } else if (method.traverseType().isMoving()) {
      return DelegateActions.delegateAction2(CachedSupplierActions.get(TraverseActions::moveThruChannel1,
          objectHandleClass,
          (Class<Channel1>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    } else {
      return DelegateActions.delegateAction2(CachedSupplierActions.get(TraverseActions::mapOfMovingThruChannel1,
          objectHandleClass,
          (Class<Channel1>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    }
  }

  @SuppressWarnings("unchecked")
  private Action buildMethodAction2(Class<?> objectHandleClass, ReflectionImplementationMethodDescription method) {
    if (method.traverseType().isMapping()) {
      return DelegateActions.delegateAction3(CachedSupplierActions.get(TraverseActions::mapThruChannel2,
          objectHandleClass,
          (Class<Channel2>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    } else if (method.traverseType().isMoving()) {
      return DelegateActions.delegateAction3(CachedSupplierActions.get(TraverseActions::moveThruChannel2,
          objectHandleClass,
          (Class<Channel2>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    } else {
      return DelegateActions.delegateAction3(CachedSupplierActions.get(TraverseActions::mapOfMovingThruChannel2,
          objectHandleClass,
          (Class<Channel2>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    }
  }

  @SuppressWarnings("unchecked")
  private Action buildMethodAction3(Class<?> objectHandleClass, ReflectionImplementationMethodDescription method) {
    if (method.traverseType().isMapping()) {
      return DelegateActions.delegateAction4(CachedSupplierActions.get(TraverseActions::mapThruChannel3,
          objectHandleClass,
          (Class<Channel3>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    } else if (method.traverseType().isMoving()) {
      return DelegateActions.delegateAction4(CachedSupplierActions.get(TraverseActions::moveThruChannel3,
          objectHandleClass,
          (Class<Channel3>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    } else {
      return DelegateActions.delegateAction4(CachedSupplierActions.get(TraverseActions::mapOfMovingThruChannel3,
          objectHandleClass,
          (Class<Channel3>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    }
  }

  @SuppressWarnings("unchecked")
  private Action buildMethodAction4(Class<?> objectHandleClass, ReflectionImplementationMethodDescription method) {
    if (method.traverseType().isMapping()) {
      throw NotImplementedExceptions.withCode("GYhrXA==");
    } else if (method.traverseType().isMoving()) {
      throw NotImplementedExceptions.withCode("8xOuXA==");
    } else {
      return DelegateActions.delegateAction5(CachedSupplierActions.get(TraverseActions::mapOfMovingThruChannel4,
          objectHandleClass,
          (Class<Channel4>) method.channelClass(),
          ReflectionForms.Reflection)
      );
    }
  }

  private Action[] buildGuideActions(ReflectionImplementationMethodDescription... methods) {
    if (methods == null || methods.length == 0) {
      return new Action[0];
    }

    int maxOrdinal = Arrays.stream(methods)
        .filter(m -> ReflectionImplementationMethodPurposes.TraverseMethod.is(m.purpose()))
        .map(ReflectionImplementationMethodDescription::traverseOrdinal)
        .max(Comparator.naturalOrder())
        .orElse(0);
    Action[] actions = new Action[maxOrdinal + 1];
    for (ReflectionImplementationMethodDescription method : methods) {
      if (ReflectionImplementationMethodPurposes.GuideMethod.is(method.purpose())) {
        actions[method.traverseOrdinal()] = method.action();
      }
    }
    return actions;
  }

  private Injection[] buildInjections(ReflectionImplementationMethodDescription... methods) {
    if (methods == null || methods.length == 0) {
      return new Injection[0];
    }

    int maxOrdinal = Arrays.stream(methods)
        .filter(m -> ReflectionImplementationMethodPurposes.InjectionMethod.is(m.purpose()))
        .map(ReflectionImplementationMethodDescription::injectionOrdinal)
        .max(Comparator.naturalOrder())
        .orElse(0);
    Injection[] injections = new Injection[maxOrdinal + 1];
    for (ReflectionImplementationMethodDescription method : methods) {
      if (ReflectionImplementationMethodPurposes.InjectionMethod.is(method.purpose())) {
        injections[method.injectionOrdinal()] = buildInjection(method);
      }
    }
    return injections;
  }

  private Injection buildInjection(ReflectionImplementationMethodDescription method) {
    if ("autoguide".equals(method.injectionKind())) {
      return AutoGuideInjections.get(null, method.injectionName(), method.injectionType());
    } else if ("specguide".equals(method.injectionKind())) {
      return GuideInjections.get(null, method.injectionName(), method.injectionType());
    } else {
      throw NotImplementedExceptions.withCode("DfsonQ==");
    }
  }
}
