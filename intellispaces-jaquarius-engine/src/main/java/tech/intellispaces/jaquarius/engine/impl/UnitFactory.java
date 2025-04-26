package tech.intellispaces.jaquarius.engine.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.jaquarius.action.InvokeUnitMethodAction;
import tech.intellispaces.jaquarius.engine.description.UnitMethodDescription;
import tech.intellispaces.jaquarius.engine.description.UnitMethodPurposes;
import tech.intellispaces.jaquarius.guide.n0.UnitMapper0;
import tech.intellispaces.jaquarius.guide.n0.UnitMapperOfMoving0;
import tech.intellispaces.jaquarius.guide.n1.UnitMapper1;
import tech.intellispaces.jaquarius.guide.n1.UnitMapperOfMoving1;
import tech.intellispaces.jaquarius.guide.n2.UnitMapper2;
import tech.intellispaces.jaquarius.guide.n2.UnitMapperOfMoving2;
import tech.intellispaces.jaquarius.guide.n3.UnitMapper3;
import tech.intellispaces.jaquarius.guide.n3.UnitMapperOfMoving3;
import tech.intellispaces.jaquarius.object.reference.ObjectReferenceForm;
import tech.intellispaces.jaquarius.system.Injection;
import tech.intellispaces.jaquarius.system.UnitGuide;
import tech.intellispaces.jaquarius.system.UnitProjectionDefinition;
import tech.intellispaces.jaquarius.system.UnitWrapper;
import tech.intellispaces.jaquarius.system.injection.AutoGuideInjections;
import tech.intellispaces.jaquarius.system.injection.GuideInjections;
import tech.intellispaces.jaquarius.system.injection.InjectionKinds;
import tech.intellispaces.jaquarius.system.injection.ProjectionInjections;
import tech.intellispaces.jaquarius.system.projection.ProjectionDefinitionBasedOnMethodActions;
import tech.intellispaces.reflection.method.MethodStatement;
import tech.intellispaces.reflection.method.Methods;

/**
 * The unit factory.
 */
class UnitFactory {

  static <U,  W extends UnitWrapper> Unit createModule(
      W wrapper, Class<U> unitClass, UnitMethodDescription... methods
  ) {
    var unit = new Unit(unitClass);
    unit.setWrapper(wrapper);
    unit.setStartupAction(buildStartupAction(wrapper, unitClass, methods));
    unit.setShutdownAction(buildShutdownAction(wrapper, unitClass, methods));
    unit.setInjections(buildUnitInjections(unitClass, methods));
    unit.setProjectionDefinitions(buildUnitProjectionDefinitions(unitClass, methods));
    unit.setGuideActions(buildUnitGuideActions(methods));
    unit.setGuides(buildGuides(wrapper, unitClass, methods));
    return unit;
  }

  static Action buildStartupAction(UnitWrapper wrapper, Class<?> unitClass, UnitMethodDescription... methods) {
    List<UnitMethodDescription> startupMethods = Arrays.stream(methods)
        .filter(m -> UnitMethodPurposes.StartupMethod.is(m.purpose()))
        .toList();
    if (startupMethods.isEmpty()) {
      return null;
    }
    if (startupMethods.size() == 1) {
      return new InvokeUnitMethodAction<>(
          wrapper,
          Methods.of(unitClass, startupMethods.get(0).name(), startupMethods.get(0).paramClasses()),
          startupMethods.get(0).action()
      );
    }
    throw NotImplementedExceptions.withCode("KElPQi4");
  }

  static Action buildShutdownAction(UnitWrapper wrapper, Class<?> unitClass, UnitMethodDescription... methods) {
    List<UnitMethodDescription> startupMethods = Arrays.stream(methods)
        .filter(m -> UnitMethodPurposes.ShutdownMethod.is(m.purpose()))
        .toList();
    if (startupMethods.isEmpty()) {
      return null;
    }
    if (startupMethods.size() == 1) {
      return new InvokeUnitMethodAction<>(
          wrapper,
          Methods.of(unitClass, startupMethods.get(0).name(), startupMethods.get(0).paramClasses()),
          startupMethods.get(0).action()
      );
    }
    throw NotImplementedExceptions.withCode("dGVuSg");
  }

  static List<Injection> buildUnitInjections(Class<?> unitClass, UnitMethodDescription... methods) {
    if (methods == null || methods.length == 0) {
      return List.of();
    }
    return Arrays.stream(methods)
        .filter(m -> m.purpose().is(UnitMethodPurposes.InjectionMethod.name()))
        .sorted(Comparator.comparing(UnitMethodDescription::injectionOrdinal))
        .map(m -> buildUnitInjection(unitClass, m))
        .toList();
  }

  static Injection buildUnitInjection(Class<?> unitClass, UnitMethodDescription method) {
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

  static List<Action> buildUnitGuideActions(UnitMethodDescription... methods) {
    if (methods == null || methods.length == 0) {
      return List.of();
    }
    return Arrays.stream(methods)
        .filter(m -> m.purpose().is(UnitMethodPurposes.Guide.name()))
        .sorted(Comparator.comparing(UnitMethodDescription::guideOrdinal))
        .map(UnitMethodDescription::action)
        .toList();
  }

  static List<UnitProjectionDefinition> buildUnitProjectionDefinitions(
      Class<?> unitClass, UnitMethodDescription... methods
  ) {
    if (methods == null || methods.length == 0) {
      return List.of();
    }
    return Arrays.stream(methods)
        .filter(m -> m.purpose().is(UnitMethodPurposes.ProjectionDefinition.name()))
        .map(m ->buildUnitProjectionDefinition(unitClass, m))
        .toList();
  }

  static UnitProjectionDefinition buildUnitProjectionDefinition(
      Class<?> unitClass, UnitMethodDescription method
  ) {
    int numRequiredProjections = method.requiredProjections() != null ? method.requiredProjections().size() : 0;
    return switch (numRequiredProjections) {
      case 0 -> ProjectionDefinitionBasedOnMethodActions.get(
          unitClass,
          method.projectionName(),
          method.targetClass(),
          method.lazyLoading(),
          method.action());
      case 1 -> ProjectionDefinitionBasedOnMethodActions.get(
          unitClass,
          method.projectionName(),
          method.targetClass(),
          method.lazyLoading(),
          method.action(),
          method.requiredProjections().get(0).name(),
          method.requiredProjections().get(0).targetClass());
      case 2 -> ProjectionDefinitionBasedOnMethodActions.get(
          unitClass,
          method.projectionName(),
          method.targetClass(),
          method.lazyLoading(),
          method.action(),
          method.requiredProjections().get(0).name(),
          method.requiredProjections().get(0).targetClass(),
          method.requiredProjections().get(1).name(),
          method.requiredProjections().get(1).targetClass());
      case 3 -> ProjectionDefinitionBasedOnMethodActions.get(
          unitClass,
          method.projectionName(),
          method.targetClass(),
          method.lazyLoading(),
          method.action(),
          method.requiredProjections().get(0).name(),
          method.requiredProjections().get(0).targetClass(),
          method.requiredProjections().get(1).name(),
          method.requiredProjections().get(1).targetClass(),
          method.requiredProjections().get(2).name(),
          method.requiredProjections().get(2).targetClass());
      case 4 -> ProjectionDefinitionBasedOnMethodActions.get(
          unitClass,
          method.projectionName(),
          method.targetClass(),
          method.lazyLoading(),
          method.action(),
          method.requiredProjections().get(0).name(),
          method.requiredProjections().get(0).targetClass(),
          method.requiredProjections().get(1).name(),
          method.requiredProjections().get(1).targetClass(),
          method.requiredProjections().get(2).name(),
          method.requiredProjections().get(2).targetClass(),
          method.requiredProjections().get(3).name(),
          method.requiredProjections().get(3).targetClass());
      case 5 -> ProjectionDefinitionBasedOnMethodActions.get(
          unitClass,
          method.projectionName(),
          method.targetClass(),
          method.lazyLoading(),
          method.action(),
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
      default -> throw NotImplementedExceptions.withCode("uOJm+w==");
    };
  }

  static List<UnitGuide<?, ?>> buildGuides(
      UnitWrapper unitWrapper, Class<?> unitClass, UnitMethodDescription... methods
  ) {
    List<UnitGuide<?, ?>> guides = new ArrayList<>();
    for (UnitMethodDescription method : methods) {
      if (UnitMethodPurposes.Guide.is(method.purpose())) {
        if (method.guideKind().isMapper()) {
          UnitGuide<?, ?> mapper = createMapper(unitWrapper, unitClass, method);
          guides.add(mapper);
        } else if (method.guideKind().isMover()) {
          UnitGuide<?, ?> mover = createMover(unitWrapper, unitClass, method);
          guides.add(mover);
        } else if (method.guideKind().isMapperOfMoving()) {
          UnitGuide<?, ?> mapperOfMoving = createUnitMapperOfMoving(unitWrapper, unitClass, method);
          guides.add(mapperOfMoving);
        }
      }
    }
    return guides;
  }

  static UnitGuide<?, ?> createMapper(
      UnitWrapper unitWrapper, Class<?> unitClass, UnitMethodDescription methodDescriptor
  ) {
    String cid = methodDescriptor.guideChannelId();
    int guideOrdinal = methodDescriptor.guideOrdinal();
    int qualifiersCount = methodDescriptor.paramClasses().size();
    ObjectReferenceForm targetForm = methodDescriptor.guideTargetForm();
    MethodStatement guideMethod = getGuideMethod(unitClass, methodDescriptor);
    return switch (qualifiersCount) {
      case 1 -> new UnitMapper0<>(cid, unitWrapper, guideMethod, guideOrdinal, targetForm);
      case 2 -> new UnitMapper1<>(cid, unitWrapper, guideMethod, guideOrdinal, targetForm);
      case 3 -> new UnitMapper2<>(cid, unitWrapper, guideMethod, guideOrdinal, targetForm);
      case 4 -> new UnitMapper3<>(cid, unitWrapper, guideMethod, guideOrdinal, targetForm);
      default -> throw UnexpectedExceptions.withMessage("Unsupported number of guide qualifiers: {0}",
          qualifiersCount);
    };
  }

  static UnitGuide<?, ?> createMover(
      UnitWrapper unitWrapper, Class<?> unitClass, UnitMethodDescription methodDescriptor
  ) {
    String cid = methodDescriptor.guideChannelId();
    throw NotImplementedExceptions.withCode("4GL2+g");
  }

  static UnitGuide<?, ?> createUnitMapperOfMoving(
      UnitWrapper unitWrapper, Class<?> unitClass, UnitMethodDescription methodDescriptor
  ) {
    String cid = methodDescriptor.guideChannelId();
    int guideOrdinal = methodDescriptor.guideOrdinal();
    int qualifiersCount = methodDescriptor.paramClasses().size();
    ObjectReferenceForm targetForm = methodDescriptor.guideTargetForm();
    MethodStatement guideMethod = getGuideMethod(unitClass, methodDescriptor);
    return switch (qualifiersCount) {
      case 1 -> new UnitMapperOfMoving0<>(cid, unitWrapper, guideMethod, guideOrdinal, targetForm);
      case 2 -> new UnitMapperOfMoving1<>(cid, unitWrapper, guideMethod, guideOrdinal, targetForm);
      case 3 -> new UnitMapperOfMoving2<>(cid, unitWrapper, guideMethod, guideOrdinal, targetForm);
      case 4 -> new UnitMapperOfMoving3<>(cid, unitWrapper, guideMethod, guideOrdinal, targetForm);
      default -> throw UnexpectedExceptions.withMessage("Unsupported number of guide qualifiers: {0}",
          qualifiersCount);
    };
  }

  static MethodStatement getGuideMethod(Class<?> unitClass, UnitMethodDescription methodDescriptor) {
    return Methods.of(unitClass, methodDescriptor.prototypeMethodName(), methodDescriptor.paramClasses());
  }
}
