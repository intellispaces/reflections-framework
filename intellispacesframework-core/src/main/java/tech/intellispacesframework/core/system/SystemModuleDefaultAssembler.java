package tech.intellispacesframework.core.system;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.annotation.Include;
import tech.intellispacesframework.core.annotation.Projection;
import tech.intellispacesframework.core.annotation.Shutdown;
import tech.intellispacesframework.core.annotation.Startup;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class SystemModuleDefaultAssembler {
  private final UnitDeclarationValidator unitDeclarationValidator;

  public SystemModuleDefaultAssembler(UnitDeclarationValidator unitDeclarationValidator) {
    this.unitDeclarationValidator = unitDeclarationValidator;
  }

  public SystemModuleDefault assembleModule(Class<?> moduleClass, String[] args) {
    List<SystemUnit> units = assembleUnits(moduleClass);
    ProjectionRegistry projectionRegistry = new ProjectionRegistryDefault();
    TraverseAnalyzer traverseAnalyzer = new TraverseAnalyzerDefault();
    return new SystemModuleDefault(units, projectionRegistry, traverseAnalyzer);
  }

  private List<SystemUnit> assembleUnits(Class<?> moduleClass) {
    List<SystemUnit> units = new ArrayList<>();
    units.add(createUnit(moduleClass, true));
    addIncludedUnits(moduleClass, units);
    return units;
  }

  private void addIncludedUnits(Class<?> moduleClass, List<SystemUnit> units) {
    Arrays.stream(moduleClass.getAnnotations())
        .filter(a -> Include.class == a.annotationType())
        .map(a -> (Include) a)
        .map(this::processInclude)
        .forEach(units::addAll);
  }

  private List<SystemUnitDefault> processInclude(Include include) {
    if (include.value() != Void.class) {
      return List.of(createUnit(include.value(), false));
    }
    throw UnexpectedViolationException.withMessage("Not implemented");
  }

  private SystemUnitDefault createUnit(Class<?> unitClass, boolean main) {
    unitDeclarationValidator.validateUnitDeclaration(unitClass, main);

    Object unitInstance = createUnitInstance(unitClass);
    Optional<Method> startupMethod = findStartupMethod(unitClass);
    Optional<Method> shutdownMethod = findShutdownMethod(unitClass);
    List<UnitProjectionProvider> projectionProviders = new ArrayList<>();
    var unit = new SystemUnitDefault(
        main,
        unitClass,
        unitInstance,
        Collections.unmodifiableList(projectionProviders),
        startupMethod.orElse(null),
        shutdownMethod.orElse(null)
    );

    addProjectionProviders(unitClass, unit, projectionProviders);
    return unit;
  }

  private <T> T createUnitInstance(Class<T> unitClass) {
    if (unitClass.isInterface()) {
      throw UnexpectedViolationException.withMessage("Not implemented");
    } else if (Modifier.isAbstract(unitClass.getModifiers())) {
      throw UnexpectedViolationException.withMessage("Not implemented");
    } else {
      return createUnitInstanceWhenClass(unitClass);
    }
  }

  private <T> T createUnitInstanceWhenClass(Class<T> unitClass) {
    try {
      return unitClass.getConstructor().newInstance();
    } catch (Exception e) {
      throw UnexpectedViolationException.withCauseAndMessage(e, "Error creating module unit {}", unitClass.getCanonicalName());
    }
  }

  private void addProjectionProviders(Class<?> unitClass, SystemUnitDefault unit, List<UnitProjectionProvider> projectionProviders) {
    Arrays.stream(unitClass.getDeclaredMethods())
        .filter(m -> m.isAnnotationPresent(Projection.class))
        .map(m -> createProjectionProvider(unit, m))
        .forEach(projectionProviders::add);
  }

  private UnitProjectionProvider createProjectionProvider(SystemUnit unit, Method method) {
    Projection annotation = method.getAnnotation(Projection.class);
    return new UnitProjectionProviderDefault(
        method.getReturnType(),
        annotation.value().trim().isBlank() ? method.getName() : annotation.value().trim(),
        unit,
        method
    );
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
