package tech.intellispaces.jaquarius.engine.impl;

import tech.intellispaces.commons.action.Action;
import tech.intellispaces.commons.base.exception.NotImplementedExceptions;
import tech.intellispaces.commons.base.exception.UnexpectedExceptions;
import tech.intellispaces.commons.base.stream.Streams;
import tech.intellispaces.commons.java.reflection.method.MethodStatement;
import tech.intellispaces.jaquarius.action.InvokeUnitMethodAction;
import tech.intellispaces.jaquarius.annotation.Configuration;
import tech.intellispaces.jaquarius.annotation.Guide;
import tech.intellispaces.jaquarius.aop.AopFunctions;
import tech.intellispaces.jaquarius.naming.NameConventionFunctions;
import tech.intellispaces.jaquarius.system.ModuleFunctions;
import tech.intellispaces.jaquarius.system.ProjectionDefinition;
import tech.intellispaces.jaquarius.system.UnitFunctions;
import tech.intellispaces.jaquarius.system.UnitWrapper;
import tech.intellispaces.jaquarius.system.empty.EmptyModule;
import tech.intellispaces.jaquarius.system.empty.EmptyModuleWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * The module factory.
 */
class ModuleFactory {

  static Module createModule(Class<?> unitClass) {
    return createModule(List.of(unitClass));
  }

  static Module createModule(List<Class<?>> unitClasses) {
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

  static List<Unit> createUnits(List<Class<?>> unitClasses) {
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

  static List<Unit> createModuleUnits(Class<?> moduleclass) {
    List<Unit> units = new ArrayList<>();
    units.add(createUnit(moduleclass, true));
    createIncludedUnits(moduleclass, units);
    return units;
  }

  static List<Unit> createEmptyMainUnitAndIncludedUnits(List<Class<?>> unitClasses) {
    List<Unit> units = new ArrayList<>();
    units.add(createEmptyMainUnit());
    unitClasses.stream()
        .map(ModuleFactory::createIncludedUnit)
        .forEach(units::add);
    return units;
  }

  static Unit createEmptyMainUnit() {
    var unit = new Unit(EmptyModule.class);
    unit.setMain(true);
    UnitWrapper unitWrapper = createUnitWrapper(EmptyModule.class, EmptyModuleWrapper.class);
    unit.setWrapper(unitWrapper);
    return unit;
  }

  static void createIncludedUnits(Class<?> moduleClass, List<Unit> units) {
    Iterable<Class<?>> unitClasses = ModuleFunctions.getIncludedUnits(moduleClass);
    Streams.get(unitClasses)
        .map(ModuleFactory::createIncludedUnit)
        .forEach(units::add);
  }

  static Unit createIncludedUnit(Class<?> unitClass) {
    if (unitClass != Void.class) {
      return createUnit(unitClass, false);
    }
    throw NotImplementedExceptions.withCode("3fHY1g");
  }

  static Unit createUnit(Class<?> unitClass, boolean main) {
    Class<?> unitWrapperClass = getUnitWrapperClass(unitClass);
    UnitWrapper unitWrapper = createUnitWrapper(unitClass, unitWrapperClass);
    Unit unit = (Unit) unitWrapper.$broker();
    unit.setMain(main);
    return unit;
  }

  static Class<?> getUnitWrapperClass(Class<?> unitClass) {
    try {
      String wrapperClassName = NameConventionFunctions.getUnitWrapperCanonicalName(unitClass.getName());
      return Class.forName(wrapperClassName);
    } catch (Exception e) {
      throw UnexpectedExceptions.withCauseAndMessage(e, "Could not get wrapper class of module unit {0}",
          unitClass.getCanonicalName());
    }
  }

  static <U> UnitWrapper createUnitWrapper(Class<U> unitClass, Class<?> wrapperClass) {
    try {
      return (UnitWrapper) wrapperClass.getConstructor().newInstance();
    } catch (Exception e) {
      throw UnexpectedExceptions.withCauseAndMessage(e, "Error creating module unit {0}",
          unitClass.getCanonicalName());
    }
  }

  static ProjectionRegistry createProjectionRegistry(List<Unit> units) {
    List<ProjectionDefinition> projectionDefinitions = new ArrayList<>();
    units.stream()
        .map(tech.intellispaces.jaquarius.system.Unit::projectionDefinitions)
        .flatMap(List::stream)
        .forEach(projectionDefinitions::add);
    return new ProjectionRegistry(projectionDefinitions);
  }

  static void loadAttachedUnitGuides(GuideRegistry guideRegistry, List<Unit> units) {
    units.stream()
        .filter(u -> UnitFunctions.isGuideUnit(u.unitClass()))
        .forEach(guideRegistry::addGuideUnit);
  }

  static void applyAdvises(List<Unit> units, ProjectionRegistry projectionRegistry) {
    units.forEach(u -> applyAdvises(u, projectionRegistry));
  }

  static void applyAdvises(Unit unit, ProjectionRegistry projectionRegistry) {
    applyStartupActionAdvises(unit, projectionRegistry);
    applyGuideActionAdvises(unit, projectionRegistry);
  }

  @SuppressWarnings("unchecked")
  static void applyStartupActionAdvises(Unit unit, ProjectionRegistry projectionRegistry) {
    if (unit.startupAction().isEmpty()) {
      return;
    }
    var startupAction = (InvokeUnitMethodAction<Void>) unit.startupAction().get();
    MethodStatement startupMethod = startupAction.method();
    Action chainAction = AopFunctions.buildChainAction(startupMethod, startupAction, projectionRegistry);
    if (chainAction != startupAction) {
      unit.setStartupAction(chainAction);
    }
  }

  static void applyGuideActionAdvises(Unit unit, ProjectionRegistry projectionRegistry) {
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
