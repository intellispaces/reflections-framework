package tech.intellispacesframework.core.system;

import tech.intellispacesframework.commons.action.ActionBuilders;
import tech.intellispacesframework.commons.action.Getter;
import tech.intellispacesframework.commons.action.ResettableGetter;
import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.commons.type.TypeFunctions;
import tech.intellispacesframework.core.annotation.Module;
import tech.intellispacesframework.core.annotation.Projection;
import tech.intellispacesframework.core.annotation.Shutdown;
import tech.intellispacesframework.core.annotation.Startup;
import tech.intellispacesframework.core.exception.ConfigurationException;
import tech.intellispacesframework.core.traverse.TraverseAnalyzer;
import tech.intellispacesframework.core.traverse.TraverseExecutor;
import tech.intellispacesframework.dynamicproxy.DynamicProxy;
import tech.intellispacesframework.dynamicproxy.proxy.contract.ProxyContract;
import tech.intellispacesframework.dynamicproxy.proxy.contract.ProxyContractBuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Default module factory.
 */
class ModuleDefaultFactory {
  private final UnitValidator unitValidator = new UnitValidator();
  private final ModuleValidator moduleValidator = new ModuleValidator();

  public ModuleDefault createModule(Class<?> moduleClass, String[] args) {
    ResettableGetter<ProjectionRegistry> projectionRegistryGetter = ActionBuilders.resettableGetter();
    List<Unit> units = assembleUnits(moduleClass, projectionRegistryGetter);

    ProjectionRegistryDefault projectionRegistry = createProjectionRegistry(units);
    projectionRegistryGetter.set(projectionRegistry);

    TraverseAnalyzer traverseAnalyzer = new TraverseAnalyzerDefault();
    TraverseExecutor traverseExecutor = new TraverseExecutorDefault(traverseAnalyzer);

    var module = new ModuleDefault(units, projectionRegistry, traverseAnalyzer, traverseExecutor);
    moduleValidator.validateModule(module);

    return module;
  }

  private List<Unit> assembleUnits(Class<?> moduleClass, Getter<ProjectionRegistry> projectionRegistryGetter) {
    List<Unit> units = new ArrayList<>();
    units.add(createUnit(moduleClass, true, projectionRegistryGetter));
    addIncludedUnits(moduleClass, units, projectionRegistryGetter);
    return units;
  }

  private void addIncludedUnits(Class<?> moduleClass, List<Unit> units, Getter<ProjectionRegistry> projectionRegistryGetter) {
    Arrays.stream(moduleClass.getAnnotation(Module.class).include())
        .map(unit -> processIncludedUnit(unit, projectionRegistryGetter))
        .forEach(units::addAll);
  }

  private List<UnitDefault> processIncludedUnit(Class<?> unitClass, Getter<ProjectionRegistry> projectionRegistryGetter) {
    if (unitClass != Void.class) {
      return List.of(createUnit(unitClass, false, projectionRegistryGetter));
    }
    throw new UnsupportedOperationException("Not implemented yet");
  }

  private UnitDefault createUnit(Class<?> unitClass, boolean main, Getter<ProjectionRegistry> projectionRegistryGetter) {
    unitValidator.validateUnitDeclaration(unitClass, main);

    List<Injection> injections = new ArrayList<>();
    List<UnitProjectionProvider> projectionProviders = new ArrayList<>();
    Optional<Method> startupMethod = findStartupMethod(unitClass);
    Optional<Method> shutdownMethod = findShutdownMethod(unitClass);

    Object unitInstance = createUnitInstance(unitClass, injections, projectionRegistryGetter);
    var unit = new UnitDefault(
        main,
        unitClass,
        unitInstance,
        injections,
        Collections.unmodifiableList(projectionProviders),
        startupMethod.orElse(null),
        shutdownMethod.orElse(null)
    );

    addProjectionProviders(unitClass, unit, projectionProviders);
    return unit;
  }

  private <T> T createUnitInstance(Class<T> unitClass, List<Injection> injections, Getter<ProjectionRegistry> projectionRegistryGetter) {
    if (unitClass.isInterface()) {
      throw new UnsupportedOperationException("Not implemented yet");
    } else if (TypeFunctions.isAbstractClass(unitClass)) {
      return createUnitInstanceWhenAbstractClass(unitClass, injections, projectionRegistryGetter);
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

  private <T> T createUnitInstanceWhenAbstractClass(Class<T> unitClass, List<Injection> injections, Getter<ProjectionRegistry> projectionRegistryGetter) {
    ProxyContractBuilder<T> proxyContractBuilder = ProxyContractBuilder.get()
        .className(unitClass.getCanonicalName() + "Proxy")
        .type(unitClass);

    List<Method> injectedMethods = findInjectedMethods(unitClass);
    for (Method injectedMethod : injectedMethods) {
      createInjection(unitClass, injectedMethod, proxyContractBuilder, injections, projectionRegistryGetter);
    }

    ProxyContract<T> proxyContract = proxyContractBuilder.build();
    Class<T> unitProxyClass = DynamicProxy.createProxyClass(proxyContract);
    try {
      return unitProxyClass.getConstructor().newInstance();
    } catch (Exception e) {
      throw UnexpectedViolationException.withCauseAndMessage(e, "Error creating module unit {}", unitClass.getCanonicalName());
    }
  }

  private <T> void createInjection(
      Class<?> unitClass, Method method, ProxyContractBuilder<T> proxyContractBuilder, List<Injection> injections, Getter<ProjectionRegistry> projectionRegistryGetter
  ) {
    if (method.getParameterCount() == 0) {
      createProjectionInjection(unitClass, method, proxyContractBuilder, injections, projectionRegistryGetter);
    } else {
      throw ConfigurationException.withMessage("Unit projection injection method can't have parameters. See method {} in unit {}",
          method.getName(), method.getDeclaringClass().getCanonicalName()
      );
    }
  }

  private <T> void createProjectionInjection(
      Class<?> unitClass, Method method, ProxyContractBuilder<T> proxyContractBuilder, List<Injection> injections, Getter<ProjectionRegistry> projectionRegistryGetter
  ) {
    var injection = new ProjectionInjectionDefault(method.getName(), unitClass, method.getReturnType(), projectionRegistryGetter);
    injections.add(injection);
    proxyContractBuilder.whenCall(method).then(injection::value);
  }

  private List<Method> findInjectedMethods(Class<?> unitClass) {
    return Arrays.stream(unitClass.getDeclaredMethods())
        .filter(TypeFunctions::isAbstractMethod)
        .toList();
  }

  private void addProjectionProviders(Class<?> unitClass, UnitDefault unit, List<UnitProjectionProvider> projectionProviders) {
    Arrays.stream(unitClass.getDeclaredMethods())
        .filter(m -> m.isAnnotationPresent(Projection.class))
        .map(m -> createProjectionProvider(unit, m))
        .forEach(projectionProviders::add);
  }

  private UnitProjectionProvider createProjectionProvider(Unit unit, Method method) {
    Projection annotation = method.getAnnotation(Projection.class);
    return new UnitProjectionProviderDefault(
        annotation.value().trim().isBlank() ? method.getName() : annotation.value().trim(),
        method.getReturnType(),
        unit,
        annotation.lazy(),
        method
    );
  }

  private ProjectionRegistryDefault createProjectionRegistry(List<Unit> units) {
    List<ProjectionProvider> projectionProviders = new ArrayList<>();
    units.stream()
        .map(Unit::projectionProviders)
        .flatMap(List::stream)
        .forEach(projectionProviders::add);
    return new ProjectionRegistryDefault(projectionProviders);
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
