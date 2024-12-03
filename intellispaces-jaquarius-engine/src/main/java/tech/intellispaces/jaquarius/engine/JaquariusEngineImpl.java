package tech.intellispaces.jaquarius.engine;

import com.google.auto.service.AutoService;
import tech.intellispaces.action.Action;
import tech.intellispaces.action.cache.CachedSupplierActions;
import tech.intellispaces.action.delegate.DelegateActions;
import tech.intellispaces.entity.exception.NotImplementedExceptions;
import tech.intellispaces.jaquarius.action.TraverseActions;
import tech.intellispaces.jaquarius.channel.Channel0;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.channel.Channel2;
import tech.intellispaces.jaquarius.channel.Channel3;
import tech.intellispaces.jaquarius.channel.Channel4;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleInstance;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleInstanceImpl;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleMethod;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleMethodPurposes;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleType;
import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleTypeImpl;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForms;
import tech.intellispaces.jaquarius.system.Injection;
import tech.intellispaces.jaquarius.system.Module;
import tech.intellispaces.jaquarius.system.ObjectHandleWrapper;
import tech.intellispaces.jaquarius.system.injection.AutoGuideInjections;
import tech.intellispaces.jaquarius.system.injection.GuideInjections;
import tech.intellispaces.jaquarius.system.kernel.KernelFunctions;
import tech.intellispaces.jaquarius.system.kernel.ModuleLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@AutoService(JaquariusEngine.class)
public class JaquariusEngineImpl implements JaquariusEngine {

  @Override
  public Module createModule(List<Class<?>> unitClasses, String[] args) {
    return ModuleLoader.loadModule(unitClasses, args);
  }

  @Override
  public <H, W extends H> ObjectHandleType registerObjectHandleType(
      Class<W> objectHandleWrapperClass, Class<H> objectHandleClass, ObjectHandleMethod... methods
  ) {
    return new ObjectHandleTypeImpl(
        objectHandleClass,
        objectHandleWrapperClass,
        Arrays.asList(methods),
        buildMethodActions(objectHandleClass, methods),
        buildGuideActions(methods),
        buildInjections(methods)
    );
  }

  @Override
  public <W> ObjectHandleInstance registerObjectHandleInstance(W objectHandleWrapper, ObjectHandleType type) {
    var typeImpl = (ObjectHandleTypeImpl) type;
    return new ObjectHandleInstanceImpl(
        type,
        typeImpl.methodActions(),
        typeImpl.guideActions(),
        typeImpl.injections()
    );
  }

  private Action[] buildMethodActions(Class<?> objectHandleClass, ObjectHandleMethod... methods) {
    if (methods == null || methods.length == 0) {
      return new Action[0];
    }

    List<Action> actions = new ArrayList<>(methods.length);
    for (ObjectHandleMethod method : methods) {
      if (ObjectHandleMethodPurposes.TraverseMethod.is(method.purpose())) {
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
  private Action buildMethodAction0(Class<?> objectHandleClass, ObjectHandleMethod method) {
    if (method.traverseType().isMapping()) {
      return DelegateActions.delegateAction1(CachedSupplierActions.get(TraverseActions::mapThruChannel0,
          objectHandleClass,
          (Class<Channel0>) method.channelClass(),
          ObjectReferenceForms.Object)
      );
    } else if (method.traverseType().isMoving()) {
      return DelegateActions.delegateAction1(CachedSupplierActions.get(TraverseActions::moveThruChannel0,
          objectHandleClass,
          (Class<Channel0>) method.channelClass(),
          ObjectReferenceForms.Object)
      );
    } else {
      return DelegateActions.delegateAction1(CachedSupplierActions.get(TraverseActions::mapOfMovingThruChannel0,
          objectHandleClass,
          (Class<Channel0>) method.channelClass(),
          ObjectReferenceForms.Object)
      );
    }
  }

  @SuppressWarnings("unchecked")
  private Action buildMethodAction1(Class<?> objectHandleClass, ObjectHandleMethod method) {
    if (method.traverseType().isMapping()) {
      return DelegateActions.delegateAction2(CachedSupplierActions.get(TraverseActions::mapThruChannel1,
          objectHandleClass,
          (Class<Channel1>) method.channelClass(),
          ObjectReferenceForms.Object)
      );
    } else if (method.traverseType().isMoving()) {
      return DelegateActions.delegateAction2(CachedSupplierActions.get(TraverseActions::moveThruChannel1,
          objectHandleClass,
          (Class<Channel1>) method.channelClass(),
          ObjectReferenceForms.Object)
      );
    } else {
      return DelegateActions.delegateAction2(CachedSupplierActions.get(TraverseActions::mapOfMovingThruChannel1,
          objectHandleClass,
          (Class<Channel1>) method.channelClass(),
          ObjectReferenceForms.Object)
      );
    }
  }

  @SuppressWarnings("unchecked")
  private Action buildMethodAction2(Class<?> objectHandleClass, ObjectHandleMethod method) {
    if (method.traverseType().isMapping()) {
      return DelegateActions.delegateAction3(CachedSupplierActions.get(TraverseActions::mapThruChannel2,
          objectHandleClass,
          (Class<Channel2>) method.channelClass(),
          ObjectReferenceForms.Object)
      );
    } else if (method.traverseType().isMoving()) {
      return DelegateActions.delegateAction3(CachedSupplierActions.get(TraverseActions::moveThruChannel2,
          objectHandleClass,
          (Class<Channel2>) method.channelClass(),
          ObjectReferenceForms.Object)
      );
    } else {
      return DelegateActions.delegateAction3(CachedSupplierActions.get(TraverseActions::mapOfMovingThruChannel2,
          objectHandleClass,
          (Class<Channel2>) method.channelClass(),
          ObjectReferenceForms.Object)
      );
    }
  }

  @SuppressWarnings("unchecked")
  private Action buildMethodAction3(Class<?> objectHandleClass, ObjectHandleMethod method) {
    if (method.traverseType().isMapping()) {
      return DelegateActions.delegateAction4(CachedSupplierActions.get(TraverseActions::mapThruChannel3,
          objectHandleClass,
          (Class<Channel3>) method.channelClass(),
          ObjectReferenceForms.Object)
      );
    } else if (method.traverseType().isMoving()) {
      return DelegateActions.delegateAction4(CachedSupplierActions.get(TraverseActions::moveThruChannel3,
          objectHandleClass,
          (Class<Channel3>) method.channelClass(),
          ObjectReferenceForms.Object)
      );
    } else {
      return DelegateActions.delegateAction4(CachedSupplierActions.get(TraverseActions::mapOfMovingThruChannel3,
          objectHandleClass,
          (Class<Channel3>) method.channelClass(),
          ObjectReferenceForms.Object)
      );
    }
  }

  @SuppressWarnings("unchecked")
  private Action buildMethodAction4(Class<?> objectHandleClass, ObjectHandleMethod method) {
    if (method.traverseType().isMapping()) {
      throw NotImplementedExceptions.withCode("GYhrXA==");
    } else if (method.traverseType().isMoving()) {
      throw NotImplementedExceptions.withCode("8xOuXA==");
    } else {
      return DelegateActions.delegateAction5(CachedSupplierActions.get(TraverseActions::mapOfMovingThruChannel4,
          objectHandleClass,
          (Class<Channel4>) method.channelClass(),
          ObjectReferenceForms.Object)
      );
    }
  }

  private Action[] buildGuideActions(ObjectHandleMethod... methods) {
    if (methods == null || methods.length == 0) {
      return new Action[0];
    }

    int maxOrdinal = Arrays.stream(methods)
        .filter(m -> ObjectHandleMethodPurposes.TraverseMethod.is(m.purpose()))
        .map(ObjectHandleMethod::traverseOrdinal)
        .max(Comparator.naturalOrder())
        .orElse(0);
    Action[] actions = new Action[maxOrdinal + 1];
    for (ObjectHandleMethod method : methods) {
      if (ObjectHandleMethodPurposes.GuideMethod.is(method.purpose())) {
        actions[method.traverseOrdinal()] = method.action();
      }
    }
    return actions;
  }

  private Injection[] buildInjections(ObjectHandleMethod... methods) {
    if (methods == null || methods.length == 0) {
      return new Injection[0];
    }

    int maxOrdinal = Arrays.stream(methods)
        .filter(m -> ObjectHandleMethodPurposes.InjectionMethod.is(m.purpose()))
        .map(ObjectHandleMethod::injectionOrdinal)
        .max(Comparator.naturalOrder())
        .orElse(0);
    Injection[] injections = new Injection[maxOrdinal + 1];
    for (ObjectHandleMethod method : methods) {
      if (ObjectHandleMethodPurposes.InjectionMethod.is(method.purpose())) {
        injections[method.injectionOrdinal()] = buildInjection(method);
      }
    }
    return injections;
  }

  private Injection buildInjection(ObjectHandleMethod method) {
    if ("autoguide".equals(method.injectionKind())) {
      return AutoGuideInjections.get(null, method.injectionName(), method.injectionType());
    } else if ("specguide".equals(method.injectionKind())) {
      return GuideInjections.get(null, method.injectionName(), method.injectionType());
    } else {
      throw NotImplementedExceptions.withCode("DfsonQ==");
    }
  }
}
