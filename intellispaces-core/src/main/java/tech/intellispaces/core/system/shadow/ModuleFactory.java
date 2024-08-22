package tech.intellispaces.core.system.shadow;

import tech.intellispaces.actions.Action;
import tech.intellispaces.commons.exception.UnexpectedViolationException;
import tech.intellispaces.core.annotation.Configuration;
import tech.intellispaces.core.annotation.Guide;
import tech.intellispaces.core.annotation.Module;
import tech.intellispaces.core.annotation.Shutdown;
import tech.intellispaces.core.annotation.Startup;
import tech.intellispaces.core.aop.AopFunctions;
import tech.intellispaces.core.common.NameConventionFunctions;
import tech.intellispaces.core.guide.GuideFunctions;
import tech.intellispaces.core.system.ObjectGuideRegistry;
import tech.intellispaces.core.system.ObjectRegistry;
import tech.intellispaces.core.system.ProjectionDefinition;
import tech.intellispaces.core.system.ProjectionRegistry;
import tech.intellispaces.core.system.Unit;
import tech.intellispaces.core.system.UnitGuideRegistry;
import tech.intellispaces.core.system.UnitWrapper;
import tech.intellispaces.core.system.action.InvokeUnitMethodAction;
import tech.intellispaces.core.system.empty.EmptyModule;
import tech.intellispaces.core.system.empty.EmptyModuleWrapper;
import tech.intellispaces.core.traverse.TraverseAnalyzer;
import tech.intellispaces.core.traverse.TraverseExecutor;

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

  public ShadowModule createModule(Class<?> unitClass) {
    return createModule(List.of(unitClass));
  }

  public ShadowModule createModule(List<Class<?>> unitClasses) {
    List<ShadowUnit> units = createUnits(unitClasses);
    applyAdvises(units);
    ObjectRegistry objectRegistry = new ObjectRegistryImpl();
    ProjectionRegistry projectionRegistry = createProjectionRegistry(units);
    ObjectGuideRegistry objectGuideRegistry = new ObjectGuideRegistryImpl();
    UnitGuideRegistry unitGuideRegistry = createUnitGuideRegistry(units);
    TraverseAnalyzer traverseAnalyzer = new TraverseAnalyzerImpl(unitGuideRegistry, objectGuideRegistry);
    TraverseExecutor traverseExecutor = new TraverseExecutorImpl(traverseAnalyzer);
    return new ModuleImpl(units, objectRegistry, projectionRegistry, traverseAnalyzer, traverseExecutor);
  }

  private List<ShadowUnit> createUnits(List<Class<?>> unitClasses) {
    if (unitClasses == null || unitClasses.isEmpty()) {
      return List.of(createEmptyMainUnit());
    } else if (unitClasses.size() == 1) {
      Class<?> unitclass = unitClasses.get(0);
      if (unitclass.isAnnotationPresent(tech.intellispaces.core.annotation.Module.class)) {
        return createModuleUnits(unitclass);
      } else if (unitclass.isAnnotationPresent(Configuration.class) ||
          unitclass.isAnnotationPresent(Guide.class)
      ) {
        return createEmptyMainUnitAndIncludedUnits(List.of(unitclass));
      } else {
        throw UnexpectedViolationException.withMessage("Expected module, configuration or guide class");
      }
    } else {
      return createEmptyMainUnitAndIncludedUnits(unitClasses);
    }
  }

  private List<ShadowUnit> createModuleUnits(Class<?> moduleclass) {
    List<ShadowUnit> units = new ArrayList<>();
    units.add(createUnit(moduleclass, true));
    createIncludedUnits(moduleclass, units);
    return units;
  }

  private List<ShadowUnit> createEmptyMainUnitAndIncludedUnits(List<Class<?>> unitClasses) {
    List<ShadowUnit> units = new ArrayList<>();
    units.add(createEmptyMainUnit());
    unitClasses.stream()
        .map(this::createIncludedUnit)
        .forEach(units::add);
    return units;
  }

  private ShadowUnit createEmptyMainUnit() {
    var unit = new ShadowUnitImpl(true, EmptyModule.class);

    UnitWrapper unitInstance = createUnitInstance(EmptyModule.class, EmptyModuleWrapper.class);
    unitInstance.$init(unit);
    unit.setInstance(unitInstance);
    return unit;
  }

  private void createIncludedUnits(Class<?> moduleClass, List<ShadowUnit> units) {
    Arrays.stream(moduleClass.getAnnotation(Module.class).units())
        .map(this::createIncludedUnit)
        .forEach(units::add);
  }

  private ShadowUnit createIncludedUnit(Class<?> unitClass) {
    if (unitClass != Void.class) {
      return createUnit(unitClass, false);
    }
    throw new UnsupportedOperationException("Not implemented yet");
  }

  private ShadowUnit createUnit(Class<?> unitClass, boolean main) {
    Optional<Method> startupMethod = findStartupMethod(unitClass);
    Optional<Method> shutdownMethod = findShutdownMethod(unitClass);

    var unit = new ShadowUnitImpl(main, unitClass);

    Class<?> unitWrapperClass = getUnitWrapperClass(unitClass);
    UnitWrapper unitInstance = createUnitInstance(unitClass, unitWrapperClass);
    unitInstance.$init(unit);
    unit.setInstance(unitInstance);

    unit.setStartupAction(startupMethod.map(m -> new InvokeUnitMethodAction<Void>(unitInstance, m)).orElse(null));
    unit.setShutdownAction(shutdownMethod.map(m -> new InvokeUnitMethodAction<Void>(unitInstance, m)).orElse(null));

    List<tech.intellispaces.core.guide.Guide<?, ?>> unitGuides = GuideFunctions.loadUnitGuides(unitClass, unitInstance);
    unit.setGuides(Collections.unmodifiableList(unitGuides));

    return unit;
  }

  private Class<?> getUnitWrapperClass(Class<?> unitClass) {
    try {
      String wrapperClassName = NameConventionFunctions.getUnitWrapperCanonicalName(unitClass.getName());
      return Class.forName(wrapperClassName);
    } catch (Exception e) {
      throw UnexpectedViolationException.withCauseAndMessage(e, "Could not get wrapper class of module unit {}",
          unitClass.getCanonicalName());
    }
  }

  private <U> UnitWrapper createUnitInstance(Class<U> unitClass, Class<?> wrapperClass) {
    try {
      return (UnitWrapper) wrapperClass.getConstructor().newInstance();
    } catch (Exception e) {
      throw UnexpectedViolationException.withCauseAndMessage(e, "Error creating module unit {}",
          unitClass.getCanonicalName());
    }
  }

  private ProjectionRegistry createProjectionRegistry(List<ShadowUnit> units) {
    List<ProjectionDefinition> projectionDefinitions = new ArrayList<>();
    units.stream()
        .map(Unit::projectionDefinitions)
        .flatMap(List::stream)
        .forEach(projectionDefinitions::add);
    return new ProjectionRegistryDefault(projectionDefinitions);
  }

  private UnitGuideRegistry createUnitGuideRegistry(List<ShadowUnit> units) {
    var registry = new UnitGuideRegistryImpl();
    units.stream()
        .map(Unit::guides)
        .flatMap(List::stream)
        .forEach(registry::addGuide);
    return registry;
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

  private void applyAdvises(List<ShadowUnit> units) {
    units.forEach(this::applyAdvises);
  }

  private void applyAdvises(ShadowUnit unit) {
    applyStartupActionAdvises(unit);
  }

  @SuppressWarnings("unchecked")
  private void applyStartupActionAdvises(ShadowUnit unit) {
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
