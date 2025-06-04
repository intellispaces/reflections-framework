package tech.intellispaces.reflections.framework.system;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.core.Rid;
import tech.intellispaces.javareflection.method.MethodStatement;
import tech.intellispaces.javareflection.method.Methods;
import tech.intellispaces.reflections.framework.action.InvokeUnitMethodAction;
import tech.intellispaces.reflections.framework.guide.n0.UnitMapper0;
import tech.intellispaces.reflections.framework.guide.n0.UnitMapperOfMoving0;
import tech.intellispaces.reflections.framework.guide.n1.UnitMapper1;
import tech.intellispaces.reflections.framework.guide.n1.UnitMapperOfMoving1;
import tech.intellispaces.reflections.framework.guide.n2.UnitMapper2;
import tech.intellispaces.reflections.framework.guide.n2.UnitMapperOfMoving2;
import tech.intellispaces.reflections.framework.guide.n3.UnitMapper3;
import tech.intellispaces.reflections.framework.guide.n3.UnitMapperOfMoving3;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.system.injection.AutoGuideInjections;
import tech.intellispaces.reflections.framework.system.injection.GuideInjections;
import tech.intellispaces.reflections.framework.system.injection.InjectionKinds;
import tech.intellispaces.reflections.framework.system.injection.ProjectionInjections;
import tech.intellispaces.reflections.framework.system.projection.UnitMethodProjectionDefinitions;

/**
 * The unit handle loader.
 */
public class UnitHandleLoader {

  public static void loadUnitHandle(UnitHandleImpl handle, UnitWrapper unitInstance) {
    UnitType unitType = unitInstance.$unitType();
    Class<?> unitClass = unitType.unitClass();
    List<UnitMethod> unitMethods = unitType.methods();

    handle.setUnitClass(unitClass);
    handle.setUnitInstance(unitInstance);
    handle.setStartupAction(buildStartupAction(unitInstance, unitClass, unitMethods));
    handle.setShutdownAction(buildShutdownAction(unitInstance, unitClass, unitMethods));
    handle.setInjections(buildUnitInjections(unitClass, unitMethods));
    handle.setProjectionDefinitions(buildUnitProjectionDefinitions(unitInstance, unitClass, unitMethods));
    handle.setGuideActions(buildUnitGuideActions(unitInstance, unitMethods));
    handle.setGuides(buildGuides(unitInstance, unitClass, unitMethods));
  }

  static Action buildStartupAction(UnitWrapper unitInstance, Class<?> unitClass, List<UnitMethod> methods) {
    List<UnitMethod> startupMethods = methods.stream()
        .filter(m -> UnitMethodPurposes.StartupMethod.is(m.purpose()))
        .toList();
    if (startupMethods.isEmpty()) {
      return null;
    }
    if (startupMethods.size() == 1) {
      return new InvokeUnitMethodAction<>(
          unitInstance,
          Methods.of(unitClass, startupMethods.get(0).name(), startupMethods.get(0).paramClasses()),
          startupMethods.get(0).action()
      );
    }
    throw NotImplementedExceptions.withCode("KElPQi4");
  }

  static Action buildShutdownAction(UnitWrapper unitInstance, Class<?> unitClass, List<UnitMethod> methods) {
    List<UnitMethod> startupMethods = methods.stream()
        .filter(m -> UnitMethodPurposes.ShutdownMethod.is(m.purpose()))
        .toList();
    if (startupMethods.isEmpty()) {
      return null;
    }
    if (startupMethods.size() == 1) {
      return new InvokeUnitMethodAction<>(
          unitInstance,
          Methods.of(unitClass, startupMethods.get(0).name(), startupMethods.get(0).paramClasses()),
          startupMethods.get(0).action()
      );
    }
    throw NotImplementedExceptions.withCode("dGVuSg");
  }

  static List<Injection> buildUnitInjections(Class<?> unitClass, List<UnitMethod> methods) {
    if (methods == null || methods.isEmpty()) {
      return List.of();
    }
    return methods.stream()
        .filter(m -> m.purpose().is(UnitMethodPurposes.InjectionMethod.name()))
        .sorted(Comparator.comparing(UnitMethod::injectionOrdinal))
        .map(m -> buildUnitInjection(unitClass, m))
        .toList();
  }

  static Injection buildUnitInjection(Class<?> unitClass, UnitMethod method) {
    if (InjectionKinds.Projection.is(method.injectionKind())) {
      return ProjectionInjections.get(unitClass, method.injectionName(), method.injectionClass());
    }
    if (InjectionKinds.AutoGuide.is(method.injectionKind())) {
      return AutoGuideInjections.get(unitClass, method.injectionName(), method.injectionClass());
    }
    if (InjectionKinds.SpecificGuide.is(method.injectionKind())) {
      return GuideInjections.get(unitClass, method.injectionName(), method.injectionClass());
    }
    throw UnexpectedExceptions.withMessage("Unsupported injection type '{0}'", method.injectionKind());
  }

  static List<Action> buildUnitGuideActions(UnitWrapper unitInstance, List<UnitMethod> methods) {
    if (methods == null || methods.isEmpty()) {
      return List.of();
    }
    return methods.stream()
        .filter(m -> m.purpose().is(UnitMethodPurposes.Guide.name()))
        .sorted(Comparator.comparing(UnitMethod::guideOrdinal))
        .map(m -> downgradeAction(m.action(), unitInstance))
        .toList();
  }

  static List<UnitProjectionDefinition> buildUnitProjectionDefinitions(
      UnitWrapper unitInstance, Class<?> unitClass, List<UnitMethod> methods
  ) {
    if (methods == null || methods.isEmpty()) {
      return List.of();
    }
    return methods.stream()
        .filter(m -> m.purpose().is(UnitMethodPurposes.ProjectionDefinition.name()))
        .map(m -> buildUnitProjectionDefinition(unitInstance, unitClass, m))
        .toList();
  }

  static UnitProjectionDefinition buildUnitProjectionDefinition(
      UnitWrapper unitInstance, Class<?> unitClass, UnitMethod method
  ) {
    int numRequiredProjections = method.requiredProjections() != null ? method.requiredProjections().size() : 0;
    return switch (numRequiredProjections) {
      case 0 -> UnitMethodProjectionDefinitions.get(
          unitClass,
          method.projectionName(),
          method.targetClass(),
          method.lazyLoading(),
          downgradeAction(method.action(), unitInstance));
      case 1 -> UnitMethodProjectionDefinitions.get(
          unitClass,
          method.projectionName(),
          method.targetClass(),
          method.lazyLoading(),
          downgradeAction(method.action(), unitInstance),
          method.requiredProjections().get(0).name(),
          method.requiredProjections().get(0).targetClass());
      case 2 -> UnitMethodProjectionDefinitions.get(
          unitClass,
          method.projectionName(),
          method.targetClass(),
          method.lazyLoading(),
          downgradeAction(method.action(), unitInstance),
          method.requiredProjections().get(0).name(),
          method.requiredProjections().get(0).targetClass(),
          method.requiredProjections().get(1).name(),
          method.requiredProjections().get(1).targetClass());
      case 3 -> UnitMethodProjectionDefinitions.get(
          unitClass,
          method.projectionName(),
          method.targetClass(),
          method.lazyLoading(),
          downgradeAction(method.action(), unitInstance),
          method.requiredProjections().get(0).name(),
          method.requiredProjections().get(0).targetClass(),
          method.requiredProjections().get(1).name(),
          method.requiredProjections().get(1).targetClass(),
          method.requiredProjections().get(2).name(),
          method.requiredProjections().get(2).targetClass());
      case 4 -> UnitMethodProjectionDefinitions.get(
          unitClass,
          method.projectionName(),
          method.targetClass(),
          method.lazyLoading(),
          downgradeAction(method.action(), unitInstance),
          method.requiredProjections().get(0).name(),
          method.requiredProjections().get(0).targetClass(),
          method.requiredProjections().get(1).name(),
          method.requiredProjections().get(1).targetClass(),
          method.requiredProjections().get(2).name(),
          method.requiredProjections().get(2).targetClass(),
          method.requiredProjections().get(3).name(),
          method.requiredProjections().get(3).targetClass());
      case 5 -> UnitMethodProjectionDefinitions.get(
          unitClass,
          method.projectionName(),
          method.targetClass(),
          method.lazyLoading(),
          downgradeAction(method.action(), unitInstance),
          method.requiredProjections().get(0).name(),
          method.requiredProjections().get(0).targetClass(),
          method.requiredProjections().get(1).name(),
          method.requiredProjections().get(1).targetClass(),
          method.requiredProjections().get(2).name(),
          method.requiredProjections().get(2).targetClass(),
          method.requiredProjections().get(3).name(),
          method.requiredProjections().get(3).targetClass(),
          method.requiredProjections().get(4).name(),
          method.requiredProjections().get(4).targetClass());
      default -> throw NotImplementedExceptions.withCode("uOJm+w");
    };
  }

  static List<UnitGuide<?, ?>> buildGuides(
      UnitWrapper unitInstance, Class<?> unitClass, List<UnitMethod> methods
  ) {
    List<UnitGuide<?, ?>> guides = new ArrayList<>();
    for (UnitMethod method : methods) {
      if (UnitMethodPurposes.Guide.is(method.purpose())) {
        if (method.guideKind().isMapper()) {
          UnitGuide<?, ?> mapper = createMapper(unitInstance, unitClass, method);
          guides.add(mapper);
        } else if (method.guideKind().isMover()) {
          UnitGuide<?, ?> mover = createMover(unitInstance, unitClass, method);
          guides.add(mover);
        } else if (method.guideKind().isMapperOfMoving()) {
          UnitGuide<?, ?> mapperOfMoving = createUnitMapperOfMoving(unitInstance, unitClass, method);
          guides.add(mapperOfMoving);
        }
      }
    }
    return guides;
  }

  static UnitGuide<?, ?> createMapper(
      UnitWrapper unitInstance, Class<?> unitClass, UnitMethod unitMethod
  ) {
    Rid cid = unitMethod.guideChannelId();
    int guideOrdinal = unitMethod.guideOrdinal();
    Class<?> sourceClass = unitMethod.paramClasses().get(0);
    int qualifiersCount = unitMethod.paramClasses().size() - 1;
    ReflectionForm targetForm = unitMethod.guideTargetForm();
    MethodStatement guideMethod = getGuideMethod(unitClass, unitMethod);
    return switch (qualifiersCount) {
      case 0 -> new UnitMapper0<>(cid, unitInstance, guideMethod, guideOrdinal, sourceClass, targetForm);
      case 1 -> new UnitMapper1<>(cid, unitInstance, guideMethod, guideOrdinal, sourceClass, targetForm);
      case 2 -> new UnitMapper2<>(cid, unitInstance, guideMethod, guideOrdinal, sourceClass, targetForm);
      case 3 -> new UnitMapper3<>(cid, unitInstance, guideMethod, guideOrdinal, sourceClass, targetForm);
      default -> throw UnexpectedExceptions.withMessage("Unsupported number of guide qualifiers: {0}",
          qualifiersCount);
    };
  }

  static UnitGuide<?, ?> createMover(
      UnitWrapper unitInstance, Class<?> unitClass, UnitMethod unitMethod
  ) {
    Rid cid = unitMethod.guideChannelId();
    throw NotImplementedExceptions.withCode("4GL2+g");
  }

  static UnitGuide<?, ?> createUnitMapperOfMoving(
      UnitWrapper unitInstance, Class<?> unitClass, UnitMethod unitMethod
  ) {
    Rid cid = unitMethod.guideChannelId();
    int guideOrdinal = unitMethod.guideOrdinal();
    Class<?> sourceClass = unitMethod.paramClasses().get(0);
    int qualifiersCount = unitMethod.paramClasses().size() - 1;
    ReflectionForm targetForm = unitMethod.guideTargetForm();
    MethodStatement guideMethod = getGuideMethod(unitClass, unitMethod);
    return switch (qualifiersCount) {
      case 0 -> new UnitMapperOfMoving0<>(cid, unitInstance, guideMethod, guideOrdinal, sourceClass, targetForm);
      case 1 -> new UnitMapperOfMoving1<>(cid, unitInstance, guideMethod, guideOrdinal, sourceClass, targetForm);
      case 2 -> new UnitMapperOfMoving2<>(cid, unitInstance, guideMethod, guideOrdinal, sourceClass, targetForm);
      case 3 -> new UnitMapperOfMoving3<>(cid, unitInstance, guideMethod, guideOrdinal, sourceClass, targetForm);
      default -> throw UnexpectedExceptions.withMessage("Unsupported number of guide qualifiers: {0}",
          qualifiersCount);
    };
  }

  static MethodStatement getGuideMethod(Class<?> unitClass, UnitMethod unitMethod) {
    return Methods.of(unitClass, unitMethod.prototypeMethodName(), unitMethod.paramClasses());
  }

  static Action downgradeAction(Action action, UnitWrapper unitInstance) {
    return switch (action.order()) {
      case 1 -> action.castToAction1().convertToAction0(unitInstance);
      case 2 -> action.castToAction2().convertToAction1(unitInstance);
      case 3 -> action.castToAction3().convertToAction2(unitInstance);
      case 4 -> action.castToAction4().convertToAction3(unitInstance);
      case 5 -> action.castToAction5().convertToAction4(unitInstance);
      case 6 -> action.castToAction6().convertToAction5(unitInstance);
      case 7 -> action.castToAction7().convertToAction6(unitInstance);
      case 8 -> action.castToAction8().convertToAction7(unitInstance);
      case 9 -> action.castToAction9().convertToAction8(unitInstance);
      case 10 -> action.castToAction10().convertToAction9(unitInstance);
      default -> throw UnexpectedExceptions.withMessage("Unsupported action order: {0}", action.order());
    };
  }
}
