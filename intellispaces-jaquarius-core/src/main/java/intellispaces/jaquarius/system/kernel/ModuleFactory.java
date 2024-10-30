package intellispaces.jaquarius.system.kernel;

import intellispaces.common.action.Action;
import intellispaces.common.base.collection.Streams;
import intellispaces.common.base.exception.UnexpectedViolationException;
import intellispaces.jaquarius.annotation.Configuration;
import intellispaces.jaquarius.annotation.Guide;
import intellispaces.jaquarius.annotation.Module;
import intellispaces.jaquarius.annotation.Shutdown;
import intellispaces.jaquarius.annotation.Startup;
import intellispaces.jaquarius.aop.AopFunctions;
import intellispaces.jaquarius.common.NameConventionFunctions;
import intellispaces.jaquarius.guide.GuideFunctions;
import intellispaces.jaquarius.system.ModuleFunctions;
import intellispaces.jaquarius.system.ProjectionDefinition;
import intellispaces.jaquarius.system.Unit;
import intellispaces.jaquarius.system.UnitFunctions;
import intellispaces.jaquarius.system.UnitGuide;
import intellispaces.jaquarius.system.UnitWrapper;
import intellispaces.jaquarius.system.action.InvokeUnitMethodAction;
import intellispaces.jaquarius.system.empty.EmptyModule;
import intellispaces.jaquarius.system.empty.EmptyModuleWrapper;

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

  public KernelModule createModule(Class<?> unitClass) {
    return createModule(List.of(unitClass));
  }

  public KernelModule createModule(List<Class<?>> unitClasses) {
    List<KernelUnit> units = createUnits(unitClasses);
    ProjectionRegistry projectionRegistry = createProjectionRegistry(units);
    applyAdvises(units, projectionRegistry);
    var guideRegistry = new GuideRegistryImpl();
    loadAttachedUnitGuides(guideRegistry, units);
    var traverseAnalyzer = new TraverseAnalyzerImpl(guideRegistry);
    var traverseExecutor = new TraverseExecutorImpl(traverseAnalyzer);
    var objectRegistry = new ObjectRegistryImpl();
    return new KernelModuleImpl(
        units,
        objectRegistry,
        projectionRegistry,
        guideRegistry,
        traverseAnalyzer,
        traverseExecutor
    );
  }

  private List<KernelUnit> createUnits(List<Class<?>> unitClasses) {
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

  private List<KernelUnit> createModuleUnits(Class<?> moduleclass) {
    List<KernelUnit> units = new ArrayList<>();
    units.add(createUnit(moduleclass, true));
    createIncludedUnits(moduleclass, units);
    return units;
  }

  private List<KernelUnit> createEmptyMainUnitAndIncludedUnits(List<Class<?>> unitClasses) {
    List<KernelUnit> units = new ArrayList<>();
    units.add(createEmptyMainUnit());
    unitClasses.stream()
        .map(this::createIncludedUnit)
        .forEach(units::add);
    return units;
  }

  private KernelUnit createEmptyMainUnit() {
    var unit = new KernelUnitImpl(true, EmptyModule.class);

    UnitWrapper unitInstance = createUnitInstance(EmptyModule.class, EmptyModuleWrapper.class);
    unitInstance.$init(unit);
    unit.setInstance(unitInstance);
    return unit;
  }

  private void createIncludedUnits(Class<?> moduleClass, List<KernelUnit> units) {
    Iterable<Class<?>> unitClasses = ModuleFunctions.getIncludedUnits(moduleClass);
    Streams.get(unitClasses)
        .map(this::createIncludedUnit)
        .forEach(units::add);
  }

  private KernelUnit createIncludedUnit(Class<?> unitClass) {
    if (unitClass != Void.class) {
      return createUnit(unitClass, false);
    }
    throw new UnsupportedOperationException("Not implemented yet");
  }

  private KernelUnit createUnit(Class<?> unitClass, boolean main) {
    Optional<Method> startupMethod = findStartupMethod(unitClass);
    Optional<Method> shutdownMethod = findShutdownMethod(unitClass);

    var unit = new KernelUnitImpl(main, unitClass);

    Class<?> unitWrapperClass = getUnitWrapperClass(unitClass);
    UnitWrapper unitInstance = createUnitInstance(unitClass, unitWrapperClass);
    unitInstance.$init(unit);
    unit.setInstance(unitInstance);

    unit.setStartupAction(startupMethod.map(m -> new InvokeUnitMethodAction<Void>(unitInstance, m)).orElse(null));
    unit.setShutdownAction(shutdownMethod.map(m -> new InvokeUnitMethodAction<Void>(unitInstance, m)).orElse(null));

    List<UnitGuide<?, ?>> unitGuides = GuideFunctions.readUnitGuides(unitClass, unitInstance);
    unit.setGuides(Collections.unmodifiableList(unitGuides));
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

  private ProjectionRegistry createProjectionRegistry(List<KernelUnit> units) {
    List<ProjectionDefinition> projectionDefinitions = new ArrayList<>();
    units.stream()
        .map(Unit::projectionDefinitions)
        .flatMap(List::stream)
        .forEach(projectionDefinitions::add);
    return new ProjectionRegistryImpl(projectionDefinitions);
  }

  private void loadAttachedUnitGuides(GuideRegistry guideRegistry, List<KernelUnit> units) {
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

  private void applyAdvises(List<KernelUnit> units, ProjectionRegistry projectionRegistry) {
    units.forEach(u -> applyAdvises(u, projectionRegistry));
  }

  private void applyAdvises(KernelUnit unit, ProjectionRegistry projectionRegistry) {
    applyStartupActionAdvises(unit, projectionRegistry);
    applyGuideActionAdvises(unit, projectionRegistry);
  }

  @SuppressWarnings("unchecked")
  private void applyStartupActionAdvises(KernelUnit unit, ProjectionRegistry projectionRegistry) {
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

  private void applyGuideActionAdvises(KernelUnit unit, ProjectionRegistry projectionRegistry) {
    List<UnitGuide<? ,?>> guides = unit.guides();
    for (UnitGuide<?, ?> guide : guides) {
      KernelUnitGuide<?, ?> kernelGuide = (KernelUnitGuide<?, ?>) guide;
      Method method = kernelGuide.guideMethod();
      Action originalAction = unit.getGuideAction(kernelGuide.guideOrdinal());
      Action chainAction = AopFunctions.buildChainAction(method, originalAction, projectionRegistry);
      if (chainAction != originalAction) {
        unit.setGuideAction(kernelGuide.guideOrdinal(), chainAction);
      }
    }
  }
}
