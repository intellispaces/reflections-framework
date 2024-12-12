package tech.intellispaces.jaquarius.engine.impl;

import tech.intellispaces.action.Action;
import tech.intellispaces.general.exception.NotImplementedExceptions;
import tech.intellispaces.general.exception.UnexpectedExceptions;
import tech.intellispaces.general.stream.Streams;
import tech.intellispaces.jaquarius.annotation.Configuration;
import tech.intellispaces.jaquarius.annotation.Guide;
import tech.intellispaces.jaquarius.annotation.Shutdown;
import tech.intellispaces.jaquarius.annotation.Startup;
import tech.intellispaces.jaquarius.aop.AopFunctions;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.guide.GuideFunctions;
import tech.intellispaces.jaquarius.action.InvokeUnitMethodAction;
import tech.intellispaces.jaquarius.system.ModuleFunctions;
import tech.intellispaces.jaquarius.system.ProjectionDefinition;
import tech.intellispaces.jaquarius.system.UnitFunctions;
import tech.intellispaces.jaquarius.system.UnitWrapper;
import tech.intellispaces.jaquarius.system.empty.EmptyModule;
import tech.intellispaces.jaquarius.system.empty.EmptyModuleWrapper;
import tech.intellispaces.java.reflection.method.MethodStatement;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The module factory.
 */
public class ModuleFactory {

  Module createModule(Class<?> unitClass) {
    return createModule(List.of(unitClass));
  }

  Module createModule(List<Class<?>> unitClasses) {
    List<Unit> units = createUnits(unitClasses);
    ProjectionRegistry projectionRegistry = createProjectionRegistry(units);
    applyAdvises(units, projectionRegistry);
    var guideRegistry = new GuideRegistry();
    loadAttachedUnitGuides(guideRegistry, units);
    var traverseAnalyzer = new TraverseAnalyzer(guideRegistry);
    var traverseExecutor = new TraverseExecutor(traverseAnalyzer);
    return new Module(
        units,
        projectionRegistry,
        guideRegistry,
        traverseAnalyzer,
        traverseExecutor
    );
  }

  private List<Unit> createUnits(List<Class<?>> unitClasses) {
    if (unitClasses == null || unitClasses.isEmpty()) {
      return List.of(createEmptyMainUnit());
    } else if (unitClasses.size() == 1) {
      Class<?> unitclass = unitClasses.get(0);
      if (unitclass.isAnnotationPresent(tech.intellispaces.jaquarius.annotation.Module.class)) {
        return createModuleUnits(unitclass);
      } else if (unitclass.isAnnotationPresent(Configuration.class) || unitclass.isAnnotationPresent(Guide.class)) {
        return createEmptyMainUnitAndIncludedUnits(List.of(unitclass));
      } else {
        throw UnexpectedExceptions.withMessage("Expected module, configuration or guide class");
      }
    } else {
      return createEmptyMainUnitAndIncludedUnits(unitClasses);
    }
  }

  private List<Unit> createModuleUnits(Class<?> moduleclass) {
    List<Unit> units = new ArrayList<>();
    units.add(createUnit(moduleclass, true));
    createIncludedUnits(moduleclass, units);
    return units;
  }

  private List<Unit> createEmptyMainUnitAndIncludedUnits(List<Class<?>> unitClasses) {
    List<Unit> units = new ArrayList<>();
    units.add(createEmptyMainUnit());
    unitClasses.stream()
        .map(this::createIncludedUnit)
        .forEach(units::add);
    return units;
  }

  private Unit createEmptyMainUnit() {
    var unit = new Unit(true, EmptyModule.class);
    UnitWrapper unitWrapper = createUnitWrapper(EmptyModule.class, EmptyModuleWrapper.class);
//    unitWrapper.$init(unit);
    unit.setWrapper(unitWrapper);
    return unit;
  }

  private void createIncludedUnits(Class<?> moduleClass, List<Unit> units) {
    Iterable<Class<?>> unitClasses = ModuleFunctions.getIncludedUnits(moduleClass);
    Streams.get(unitClasses)
        .map(this::createIncludedUnit)
        .forEach(units::add);
  }

  private Unit createIncludedUnit(Class<?> unitClass) {
    if (unitClass != Void.class) {
      return createUnit(unitClass, false);
    }
    throw NotImplementedExceptions.withCode("3fHY1g");
  }

  private Unit createUnit(Class<?> unitClass, boolean main) {
    Optional<Method> startupMethod = findStartupMethod(unitClass);
    Optional<Method> shutdownMethod = findShutdownMethod(unitClass);

//    var unit = new Unit(main, unitClass);

    Class<?> unitWrapperClass = getUnitWrapperClass(unitClass);
    UnitWrapper unitWrapper = createUnitWrapper(unitClass, unitWrapperClass);
    Unit unit = (Unit) unitWrapper.$agent();
    unit.setMain(main);

//    unitWrapper.$init(unit);
    unit.setWrapper(unitWrapper);

    unit.setStartupAction(startupMethod.map(m -> new InvokeUnitMethodAction<Void>(unitWrapper, m)).orElse(null));
    unit.setShutdownAction(shutdownMethod.map(m -> new InvokeUnitMethodAction<Void>(unitWrapper, m)).orElse(null));

    List<tech.intellispaces.jaquarius.system.UnitGuide<?, ?>> unitGuides = GuideFunctions.readUnitGuides(unitClass, unitWrapper);
    unit.setGuides(Collections.unmodifiableList(unitGuides));
    return unit;
  }

  private Class<?> getUnitWrapperClass(Class<?> unitClass) {
    try {
      String wrapperClassName = NameConventionFunctions.getUnitWrapperCanonicalName(unitClass.getName());
      return Class.forName(wrapperClassName);
    } catch (Exception e) {
      throw UnexpectedExceptions.withCauseAndMessage(e, "Could not get wrapper class of module unit {0}",
          unitClass.getCanonicalName());
    }
  }

  private <U> UnitWrapper createUnitWrapper(Class<U> unitClass, Class<?> wrapperClass) {
    try {
      return (UnitWrapper) wrapperClass.getConstructor().newInstance();
    } catch (Exception e) {
      throw UnexpectedExceptions.withCauseAndMessage(e, "Error creating module unit {0}",
          unitClass.getCanonicalName());
    }
  }

  private ProjectionRegistry createProjectionRegistry(List<Unit> units) {
    List<ProjectionDefinition> projectionDefinitions = new ArrayList<>();
    units.stream()
        .map(tech.intellispaces.jaquarius.system.Unit::projectionDefinitions)
        .flatMap(List::stream)
        .forEach(projectionDefinitions::add);
    return new ProjectionRegistry(projectionDefinitions);
  }

  private void loadAttachedUnitGuides(GuideRegistry guideRegistry, List<Unit> units) {
    units.stream()
        .filter(u -> UnitFunctions.isGuideUnit(u.unitClass()))
        .forEach(guideRegistry::addGuideUnit);
  }

  private Optional<Method> findStartupMethod(Class<?> unitClass) {
    return Arrays.stream(unitClass.getMethods())
        .filter(m -> m.isAnnotationPresent(Startup.class))
        .findAny();
  }

  private Optional<Method> findShutdownMethod(Class<?> unitClass) {
    return Arrays.stream(unitClass.getMethods())
        .filter(m -> m.isAnnotationPresent(Shutdown.class))
        .findAny();
  }

  private void applyAdvises(List<Unit> units, ProjectionRegistry projectionRegistry) {
    units.forEach(u -> applyAdvises(u, projectionRegistry));
  }

  private void applyAdvises(Unit unit, ProjectionRegistry projectionRegistry) {
    applyStartupActionAdvises(unit, projectionRegistry);
    applyGuideActionAdvises(unit, projectionRegistry);
  }

  @SuppressWarnings("unchecked")
  private void applyStartupActionAdvises(Unit unit, ProjectionRegistry projectionRegistry) {
    if (unit.startupAction().isEmpty()) {
      return;
    }
    var startupAction = (InvokeUnitMethodAction<Void>) unit.startupAction().get();
    Method startupMethod = startupAction.getUnitMethod();
    Action chainAction = AopFunctions.buildChainAction(startupMethod, startupAction, projectionRegistry);
    if (chainAction != startupAction) {
      unit.setStartupAction(chainAction);
    }
  }

  private void applyGuideActionAdvises(Unit unit, ProjectionRegistry projectionRegistry) {
    List<tech.intellispaces.jaquarius.system.UnitGuide<? ,?>> guides = unit.guides();
    for (tech.intellispaces.jaquarius.system.UnitGuide<?, ?> guide : guides) {
      MethodStatement method = guide.guideMethod();
      Action originalAction = unit.guideAction(guide.guideOrdinal());
      Action chainAction = AopFunctions.buildChainAction(method, originalAction, projectionRegistry);
      if (chainAction != originalAction) {
        unit.setGuideAction(guide.guideOrdinal(), chainAction);
      }
    }
  }
}
