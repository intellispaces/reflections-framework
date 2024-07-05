package tech.intellispaces.framework.core.system;

import tech.intellispaces.framework.commons.exception.UnexpectedViolationException;
import tech.intellispaces.framework.core.annotation.Module;
import tech.intellispaces.framework.core.annotation.Projection;
import tech.intellispaces.framework.core.annotation.Shutdown;
import tech.intellispaces.framework.core.annotation.Startup;
import tech.intellispaces.framework.core.common.NameFunctions;
import tech.intellispaces.framework.core.guide.Guide;
import tech.intellispaces.framework.core.guide.GuideFunctions;
import tech.intellispaces.framework.core.traverse.TraverseAnalyzer;
import tech.intellispaces.framework.core.traverse.TraverseExecutor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Default module factory.
 */
class ModuleDefaultFactory {

  public ModuleDefault createModule(Class<?> moduleClass) {
    List<Unit> units = createUnits(moduleClass);
    ProjectionRegistry projectionRegistry = createProjectionRegistry(units);
    AttachedGuideRegistry attachedGuideRegistry = new AttachedGuideRegistryImpl();
    ModuleGuideRegistry moduleGuideRegistry = createModuleGuideRegistry(units);;
    TraverseAnalyzer traverseAnalyzer = new TraverseAnalyzerImpl(moduleGuideRegistry, attachedGuideRegistry);
    TraverseExecutor traverseExecutor = new TraverseExecutorImpl(traverseAnalyzer);
    return new ModuleDefaultImpl(units, projectionRegistry, traverseAnalyzer, traverseExecutor);
  }

  private List<Unit> createUnits(Class<?> moduleClass) {
    List<Unit> units = new ArrayList<>();
    units.add(createUnit(moduleClass, true));
    createIncludedUnits(moduleClass, units);
    return units;
  }

  private void createIncludedUnits(Class<?> moduleClass, List<Unit> units) {
    Arrays.stream(moduleClass.getAnnotation(Module.class).units())
        .map(this::createIncludedUnit)
        .forEach(units::add);
  }

  private Unit createIncludedUnit(Class<?> unitClass) {
    if (unitClass != Void.class) {
      return createUnit(unitClass, false);
    }
    throw new UnsupportedOperationException("Not implemented yet");
  }

  private Unit createUnit(Class<?> unitClass, boolean main) {
    List<UnitProjectionDefinition> projectionProviders = new ArrayList<>();
    Optional<Method> startupMethod = findStartupMethod(unitClass);
    Optional<Method> shutdownMethod = findShutdownMethod(unitClass);

    Class<?> unitWrapperClass = getUnitWrapperClass(unitClass);
    UnitWrapper unitInstance = createUnitInstance(unitClass, unitWrapperClass);
    List<Guide<?, ?>> unitGuides = GuideFunctions.loadUnitGuides(unitClass, unitInstance);
    Unit unit = new UnitImpl(
        main,
        unitClass,
        unitInstance,
        projectionProviders,
        unitGuides,
        startupMethod.orElse(null),
        shutdownMethod.orElse(null)
    );

    addProjectionProviders(unitWrapperClass, unit, projectionProviders);
    return unit;
  }

  private Class<?> getUnitWrapperClass(Class<?> unitClass) {
    try {
      String wrapperClassName = NameFunctions.getUnitWrapperCanonicalName(unitClass.getName());
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

  private void addProjectionProviders(
      Class<?> unitWrapperClass, Unit unit, List<UnitProjectionDefinition> projectionProviders
  ) {
    Arrays.stream(unitWrapperClass.getDeclaredMethods())
        .filter(m -> m.isAnnotationPresent(Projection.class))
        .map(m -> createProjectionProvider(unit, m))
        .forEach(projectionProviders::add);
  }

  private UnitProjectionDefinition createProjectionProvider(Unit unit, Method method) {
    Projection annotation = method.getAnnotation(Projection.class);
    return new UnitProjectionDefinitionImpl(
        annotation.value().trim().isBlank() ? method.getName() : annotation.value().trim(),
        method.getReturnType(),
        unit,
        annotation.lazy(),
        method
    );
  }

  private ProjectionRegistry createProjectionRegistry(List<Unit> units) {
    List<ProjectionDefinition> projectionDefinitions = new ArrayList<>();
    units.stream()
        .map(Unit::projectionProviders)
        .flatMap(List::stream)
        .forEach(projectionDefinitions::add);
    return new ProjectionRegistryImpl(projectionDefinitions);
  }

  private ModuleGuideRegistry createModuleGuideRegistry(List<Unit> units) {
    var registry = new ModuleGuideRegistryImpl();
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
}
