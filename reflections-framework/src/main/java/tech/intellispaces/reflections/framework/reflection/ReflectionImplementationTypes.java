package tech.intellispaces.reflections.framework.reflection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.actions.cache.CachedSupplierActions;
import tech.intellispaces.actions.delegate.DelegateActions;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.reflections.framework.action.TraverseActions;
import tech.intellispaces.reflections.framework.channel.Channel0;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.channel.Channel2;
import tech.intellispaces.reflections.framework.channel.Channel3;
import tech.intellispaces.reflections.framework.channel.Channel4;
import tech.intellispaces.reflections.framework.system.Injection;
import tech.intellispaces.reflections.framework.system.injection.AutoGuideInjections;
import tech.intellispaces.reflections.framework.system.injection.GuideInjections;

public interface ReflectionImplementationTypes {

  static <R, W extends R> ReflectionImplementationType create(
      Class<W> reflectionWrapperClass,
      Class<R> reflectionImplClass,
      ReflectionImplementationMethod... methods
  ) {
    return new ReflectionImplementationTypeImpl(
        reflectionImplClass,
        reflectionWrapperClass,
        Arrays.asList(methods),
        buildMethodActions(reflectionImplClass, methods),
        buildGuideActions(methods),
        buildInjections(methods)
    );
  }

  private static Action[] buildMethodActions(Class<?> reflectionClass, ReflectionImplementationMethod... methods) {
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
  private static Action buildMethodAction0(Class<?> reflectionClass, ReflectionImplementationMethod method) {
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
  private static Action buildMethodAction1(Class<?> reflectionClass, ReflectionImplementationMethod method) {
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
  private static Action buildMethodAction2(Class<?> reflectionClass, ReflectionImplementationMethod method) {
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
  private static Action buildMethodAction3(Class<?> reflectionClass, ReflectionImplementationMethod method) {
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
  private static Action buildMethodAction4(Class<?> reflectionClass, ReflectionImplementationMethod method) {
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

  private static Action[] buildGuideActions(ReflectionImplementationMethod... methods) {
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

  private static Injection[] buildInjections(ReflectionImplementationMethod... methods) {
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

  private static Injection buildInjection(ReflectionImplementationMethod method) {
    if ("autoguide".equals(method.injectionKind())) {
      return AutoGuideInjections.get(null, method.injectionName(), method.injectionType());
    } else if ("specguide".equals(method.injectionKind())) {
      return GuideInjections.get(null, method.injectionName(), method.injectionType());
    } else {
      throw NotImplementedExceptions.withCode("DfsonQ==");
    }
  }
}
