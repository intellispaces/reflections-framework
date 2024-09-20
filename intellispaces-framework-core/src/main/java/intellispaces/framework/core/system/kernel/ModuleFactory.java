package intellispaces.framework.core.system.kernel;

import intellispaces.common.action.Action;
import intellispaces.common.base.collection.Streams;
import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.framework.core.annotation.Configuration;
import intellispaces.framework.core.annotation.Guide;
import intellispaces.framework.core.annotation.Module;
import intellispaces.framework.core.annotation.Shutdown;
import intellispaces.framework.core.annotation.Startup;
import intellispaces.framework.core.aop.AopFunctions;
import intellispaces.framework.core.common.NameConventionFunctions;
import intellispaces.framework.core.guide.GuideFunctions;
import intellispaces.framework.core.system.AttachedUnitGuide;
import intellispaces.framework.core.system.ModuleFunctions;
import intellispaces.framework.core.system.ProjectionDefinition;
import intellispaces.framework.core.system.Unit;
import intellispaces.framework.core.system.UnitFunctions;
import intellispaces.framework.core.system.UnitWrapper;
import intellispaces.framework.core.system.action.InvokeUnitMethodAction;
import intellispaces.framework.core.system.empty.EmptyModule;
import intellispaces.framework.core.system.empty.EmptyModuleWrapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Internal module factory.
 */
public class ModuleFactory {

  public SystemModule createModule(Class<?> unitClass) {
    return createModule(List.of(unitClass));
  }

  public SystemModule createModule(List<Class<?>> unitClasses) {
    List<SystemUnit> units = createUnits(unitClasses);
    applyAdvises(units);
    var objectRegistry = new ObjectRegistryImpl();
    ProjectionRegistry projectionRegistry = createProjectionRegistry(units);
    var guideRegistry = new GuideRegistryImpl();
    loadAttachedUnitGuides(guideRegistry, units);
    var traverseAnalyzer = new TraverseAnalyzerImpl(guideRegistry);
    var traverseExecutor = new TraverseExecutorImpl(traverseAnalyzer);
    return new SystemModuleImpl(
        units,
        objectRegistry,
        projectionRegistry,
        guideRegistry,
        traverseAnalyzer,
        traverseExecutor
    );
  }

  private List<SystemUnit> createUnits(List<Class<?>> unitClasses) {
    if (unitClasses == null || unitClasses.isEmpty()) {
      return List.of(createEmptyMainUnit());
    } else if (unitClasses.size() == 1) {
      Class<?> unitclass = unitClasses.get(0);
      if (unitclass.isAnnotationPresent(Module.class)) {
        return createModuleUnits(unitclass);
      } else if (unitclass.isAnnotationPresent(Configuration.class) || unitclass.isAnnotationPresent(Guide.class)) {
        return createEmptyMainUnitAndIncludedUnits(List.of(unitclass));
      } else {
        throw UnexpectedViolationException.withMessage("Expected module, configuration or guide class");
      }
    } else {
      return createEmptyMainUnitAndIncludedUnits(unitClasses);
    }
  }

  private List<SystemUnit> createModuleUnits(Class<?> moduleclass) {
    List<SystemUnit> units = new ArrayList<>();
    units.add(createUnit(moduleclass, true));
    createIncludedUnits(moduleclass, units);
    return units;
  }

  private List<SystemUnit> createEmptyMainUnitAndIncludedUnits(List<Class<?>> unitClasses) {
    List<SystemUnit> units = new ArrayList<>();
    units.add(createEmptyMainUnit());
    unitClasses.stream()
        .map(this::createIncludedUnit)
        .forEach(units::add);
    return units;
  }

  private SystemUnit createEmptyMainUnit() {
    var unit = new SystemUnitImpl(true, EmptyModule.class);

    UnitWrapper unitInstance = createUnitInstance(EmptyModule.class, EmptyModuleWrapper.class);
    unitInstance.$init(unit);
    unit.setInstance(unitInstance);
    return unit;
  }

  private void createIncludedUnits(Class<?> moduleClass, List<SystemUnit> units) {
    Iterable<Class<?>> unitClasses = ModuleFunctions.getIncludedUnits(moduleClass);
    Streams.get(unitClasses)
        .map(this::createIncludedUnit)
        .forEach(units::add);
  }

  private SystemUnit createIncludedUnit(Class<?> unitClass) {
    if (unitClass != Void.class) {
      return createUnit(unitClass, false);
    }
    throw new UnsupportedOperationException("Not implemented yet");
  }

  private SystemUnit createUnit(Class<?> unitClass, boolean main) {
    Optional<Method> startupMethod = findStartupMethod(unitClass);
    Optional<Method> shutdownMethod = findShutdownMethod(unitClass);

    var unit = new SystemUnitImpl(main, unitClass);

    Class<?> unitWrapperClass = getUnitWrapperClass(unitClass);
    UnitWrapper unitInstance = createUnitInstance(unitClass, unitWrapperClass);
    unitInstance.$init(unit);
    unit.setInstance(unitInstance);

    unit.setStartupAction(startupMethod.map(m -> new InvokeUnitMethodAction<Void>(unitInstance, m)).orElse(null));
    unit.setShutdownAction(shutdownMethod.map(m -> new InvokeUnitMethodAction<Void>(unitInstance, m)).orElse(null));

    List<AttachedUnitGuide> attachedUnitGuides = GuideFunctions.readAttachedUnitGuides(unitClass, unitInstance);
    unit.setGuides(Collections.unmodifiableList(attachedUnitGuides));

    return unit;
  }

  private Class<?> getUnitWrapperClass(Class<?> unitClass) {
    try {
      String wrapperClassName = NameConventionFunctions.getUnitWrapperCanonicalName(unitClass.getName());
      return Class.forName(wrapperClassName);
    } catch (Exception e) {
      throw UnexpectedViolationException.withCauseAndMessage(e, "Could not get wrapper class of module unit {0}",
          unitClass.getCanonicalName());
    }
  }

  private <U> UnitWrapper createUnitInstance(Class<U> unitClass, Class<?> wrapperClass) {
    try {
      return (UnitWrapper) wrapperClass.getConstructor().newInstance();
    } catch (Exception e) {
      throw UnexpectedViolationException.withCauseAndMessage(e, "Error creating module unit {0}",
          unitClass.getCanonicalName());
    }
  }

  private ProjectionRegistry createProjectionRegistry(List<SystemUnit> units) {
    List<ProjectionDefinition> projectionDefinitions = new ArrayList<>();
    units.stream()
        .map(Unit::projectionDefinitions)
        .flatMap(List::stream)
        .forEach(projectionDefinitions::add);
    return new ProjectionRegistryImpl(projectionDefinitions);
  }

  private void loadAttachedUnitGuides(GuideRegistry guideRegistry, List<SystemUnit> units) {
    units.stream()
        .filter(u -> UnitFunctions.isGuideUnit(u.unitClass()))
        .forEach(guideRegistry::addGuideUnit);
  }

  private Optional<Method> findStartupMethod(Class<?> unitClass) {
    return Arrays.stream(unitClass.getDeclaredMethods())
        .filter(m -> m.isAnnotationPresent(Startup.class))
        .findAny();
  }

  private Optional<Method> findShutdownMethod(Class<?> unitClass) {
    return Arrays.stream(unitClass.getDeclaredMethods())
        .filter(m -> m.isAnnotationPresent(Shutdown.class))
        .findAny();
  }

  private void applyAdvises(List<SystemUnit> units) {
    units.forEach(this::applyAdvises);
  }

  private void applyAdvises(SystemUnit unit) {
    applyStartupActionAdvises(unit);
  }

  @SuppressWarnings("unchecked")
  private void applyStartupActionAdvises(SystemUnit unit) {
    if (unit.startupAction().isEmpty()) {
      return;
    }
    var startupAction = (InvokeUnitMethodAction<Void>) unit.startupAction().get();
    Method startupMethod = startupAction.getUnitMethod();
    Action chainAction = AopFunctions.buildChainAction(startupMethod, startupAction);
    if (chainAction != startupAction) {
      unit.setStartupAction(chainAction);
    }
  }
}
