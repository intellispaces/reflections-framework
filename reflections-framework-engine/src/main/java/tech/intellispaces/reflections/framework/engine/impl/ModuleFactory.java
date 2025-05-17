package tech.intellispaces.reflections.framework.engine.impl;

import java.util.ArrayList;
import java.util.List;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.exception.NotImplementedExceptions;
import tech.intellispaces.commons.exception.UnexpectedExceptions;
import tech.intellispaces.commons.stream.Streams;
import tech.intellispaces.jstatements.method.MethodStatement;
import tech.intellispaces.reflections.framework.action.InvokeUnitMethodAction;
import tech.intellispaces.reflections.framework.annotation.Configuration;
import tech.intellispaces.reflections.framework.annotation.Guide;
import tech.intellispaces.reflections.framework.aop.AopFunctions;
import tech.intellispaces.reflections.framework.naming.NameConventionFunctions;
import tech.intellispaces.reflections.framework.system.LocalGuideRegistry;
import tech.intellispaces.reflections.framework.system.LocalProjectionRegistry;
import tech.intellispaces.reflections.framework.system.LocalTraverseExecutor;
import tech.intellispaces.reflections.framework.system.ModuleFunctions;
import tech.intellispaces.reflections.framework.system.ProjectionDefinition;
import tech.intellispaces.reflections.framework.system.ProjectionRegistry;
import tech.intellispaces.reflections.framework.system.UnitFunctions;
import tech.intellispaces.reflections.framework.system.UnitGuide;
import tech.intellispaces.reflections.framework.system.UnitHandle;
import tech.intellispaces.reflections.framework.system.UnitHandleImpl;
import tech.intellispaces.reflections.framework.system.UnitHandleLoader;
import tech.intellispaces.reflections.framework.system.UnitWrapper;
import tech.intellispaces.reflections.framework.system.empty.EmptyModule;
import tech.intellispaces.reflections.framework.system.empty.EmptyModuleWrapper;

/**
 * The module factory.
 */
class ModuleFactory {

  static ModuleImpl createModule(Class<?> unitClass) {
    return createModule(List.of(unitClass));
  }

  static ModuleImpl createModule(List<Class<?>> unitClasses) {
    List<UnitHandle> units = createUnits(unitClasses);
    ProjectionRegistry projectionRegistry = createProjectionRegistry(units);
    applyAdvises(units, projectionRegistry);
    var guideRegistry = new LocalGuideRegistry();
    loadAttachedUnitGuides(guideRegistry, units);
    var traverseAnalyzer = new TraverseAnalyzerImpl(guideRegistry);
    var traverseExecutor = new LocalTraverseExecutor(traverseAnalyzer);
    return new ModuleImpl(
        units,
        projectionRegistry,
        guideRegistry,
        traverseAnalyzer,
        traverseExecutor
    );
  }

  static List<UnitHandle> createUnits(List<Class<?>> unitClasses) {
    if (unitClasses == null || unitClasses.isEmpty()) {
      return List.of(createEmptyMainUnit());
    } else if (unitClasses.size() == 1) {
      Class<?> unitclass = unitClasses.get(0);
      if (unitclass.isAnnotationPresent(tech.intellispaces.reflections.framework.annotation.Module.class)) {
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

  static List<UnitHandle> createModuleUnits(Class<?> moduleclass) {
    List<UnitHandle> units = new ArrayList<>();
    units.add(createUnit(moduleclass, true));
    createIncludedUnits(moduleclass, units);
    return units;
  }

  static List<UnitHandle> createEmptyMainUnitAndIncludedUnits(List<Class<?>> unitClasses) {
    List<UnitHandle> units = new ArrayList<>();
    units.add(createEmptyMainUnit());
    unitClasses.stream()
        .map(ModuleFactory::createIncludedUnit)
        .forEach(units::add);
    return units;
  }

  static UnitHandle createEmptyMainUnit() {
    var unitHandle = new UnitHandleImpl();
    unitHandle.setUnitClass(EmptyModule.class);
    unitHandle.setMain(true);
    UnitWrapper unitWrapper = createUnitWrapper(EmptyModule.class, EmptyModuleWrapper.class, unitHandle);
    unitHandle.setUnitInstance(unitWrapper);
    return unitHandle;
  }

  static void createIncludedUnits(Class<?> moduleClass, List<UnitHandle> units) {
    Iterable<Class<?>> unitClasses = ModuleFunctions.getIncludedUnits(moduleClass);
    Streams.get(unitClasses)
        .map(ModuleFactory::createIncludedUnit)
        .forEach(units::add);
  }

  static UnitHandle createIncludedUnit(Class<?> unitClass) {
    if (unitClass != Void.class) {
      return createUnit(unitClass, false);
    }
    throw NotImplementedExceptions.withCode("3fHY1g");
  }

  static UnitHandle createUnit(Class<?> unitClass, boolean main) {
    Class<?> unitWrapperClass = getUnitWrapperClass(unitClass);

    var unitHandle = new UnitHandleImpl();
    UnitWrapper unitWrapper = createUnitWrapper(unitClass, unitWrapperClass, unitHandle);
    UnitHandleLoader.loadUnitHandle(unitHandle, unitWrapper);
    unitHandle.setMain(main);
    return unitHandle;
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

  static <U> UnitWrapper createUnitWrapper(Class<U> unitClass, Class<?> wrapperClass, UnitHandle unitHandle) {
    try {
      return (UnitWrapper) wrapperClass.getConstructor(UnitHandle.class).newInstance(unitHandle);
    } catch (Exception e) {
      throw UnexpectedExceptions.withCauseAndMessage(e, "Error creating unit {0}", unitClass.getCanonicalName());
    }
  }

  static ProjectionRegistry createProjectionRegistry(List<UnitHandle> units) {
    List<ProjectionDefinition> projectionDefinitions = new ArrayList<>();
    units.stream()
        .map(tech.intellispaces.reflections.framework.system.Unit::projectionDefinitions)
        .flatMap(List::stream)
        .forEach(projectionDefinitions::add);
    return new LocalProjectionRegistry(projectionDefinitions);
  }

  static void loadAttachedUnitGuides(LocalGuideRegistry guideRegistry, List<UnitHandle> unitHandles) {
    unitHandles.stream()
        .filter(u -> UnitFunctions.isGuideUnit(u.unitClass()))
        .forEach(u -> guideRegistry.addGuideUnit(u.unitClass(), u.unitInstance(), u.guides()));
  }

  static void applyAdvises(List<UnitHandle> unitHandles, ProjectionRegistry projectionRegistry) {
    unitHandles.forEach(u -> applyAdvises(u, projectionRegistry));
  }

  static void applyAdvises(UnitHandle unitHandle, ProjectionRegistry projectionRegistry) {
    applyStartupActionAdvises(unitHandle, projectionRegistry);
    applyGuideActionAdvises(unitHandle, projectionRegistry);
  }

  @SuppressWarnings("unchecked")
  static void applyStartupActionAdvises(UnitHandle unitHandle, ProjectionRegistry projectionRegistry) {
    if (unitHandle.startupAction().isEmpty()) {
      return;
    }
    var startupAction = (InvokeUnitMethodAction<Void>) unitHandle.startupAction().get();
    MethodStatement startupMethod = startupAction.method();
    Action chainAction = AopFunctions.buildChainAction(startupMethod, startupAction, projectionRegistry);
    if (chainAction != startupAction) {
      unitHandle.setStartupAction(chainAction);
    }
  }

  static void applyGuideActionAdvises(UnitHandle unitHandle, ProjectionRegistry projectionRegistry) {
    List<UnitGuide<? ,?>> guides = unitHandle.guides();
    for (UnitGuide<?, ?> guide : guides) {
      MethodStatement method = guide.guideMethod();
      Action originalAction = unitHandle.guideAction(guide.guideOrdinal());
      Action chainAction = AopFunctions.buildChainAction(method, originalAction, projectionRegistry);
      if (chainAction != originalAction) {
        unitHandle.setGuideAction(guide.guideOrdinal(), chainAction);
      }
    }
  }
}
