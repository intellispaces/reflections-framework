package tech.intellispacesframework.core.system;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.commons.type.TypeFunctions;
import tech.intellispacesframework.core.annotation.Include;
import tech.intellispacesframework.core.annotation.Projection;
import tech.intellispacesframework.core.annotation.Shutdown;
import tech.intellispacesframework.core.annotation.Startup;
import tech.intellispacesframework.core.exception.ConfigurationException;
import tech.intellispacesframework.core.object.ObjectFunctions;
import tech.intellispacesframework.dynamicproxy.DynamicProxy;
import tech.intellispacesframework.dynamicproxy.proxy.contract.ProxyContract;
import tech.intellispacesframework.dynamicproxy.proxy.contract.ProxyContractBuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class ModuleDefaultAssembler {
  private final UnitDeclarationValidator unitDeclarationValidator;

  public ModuleDefaultAssembler(UnitDeclarationValidator unitDeclarationValidator) {
    this.unitDeclarationValidator = unitDeclarationValidator;
  }

  public ModuleDefault assembleModule(Class<?> moduleClass, String[] args) {
    List<Unit> units = assembleUnits(moduleClass);
    ProjectionRegistry projectionRegistry = new ProjectionRegistryDefault();
    TraverseAnalyzer traverseAnalyzer = new TraverseAnalyzerDefault();
    return new ModuleDefault(units, projectionRegistry, traverseAnalyzer);
  }

  private List<Unit> assembleUnits(Class<?> moduleClass) {
    List<Unit> units = new ArrayList<>();
    units.add(createUnit(moduleClass, true));
    addIncludedUnits(moduleClass, units);
    return units;
  }

  private void addIncludedUnits(Class<?> moduleClass, List<Unit> units) {
    Arrays.stream(moduleClass.getAnnotations())
        .filter(a -> Include.class == a.annotationType())
        .map(a -> (Include) a)
        .map(this::processInclude)
        .forEach(units::addAll);
  }

  private List<UnitDefault> processInclude(Include include) {
    if (include.value() != Void.class) {
      return List.of(createUnit(include.value(), false));
    }
    throw UnexpectedViolationException.withMessage("Not implemented");
  }

  private UnitDefault createUnit(Class<?> unitClass, boolean main) {
    unitDeclarationValidator.validateUnitDeclaration(unitClass, main);

    List<Injection> injections = new ArrayList<>();
    List<UnitProjectionProvider> projectionProviders = new ArrayList<>();
    Optional<Method> startupMethod = findStartupMethod(unitClass);
    Optional<Method> shutdownMethod = findShutdownMethod(unitClass);

    Object unitInstance = createUnitInstance(unitClass, injections);
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

  private <T> T createUnitInstance(Class<T> unitClass, List<Injection> injections) {
    if (unitClass.isInterface()) {
      throw UnexpectedViolationException.withMessage("Not implemented");
    } else if (TypeFunctions.isAbstractClass(unitClass)) {
      return createUnitInstanceWhenAbstractClass(unitClass, injections);
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

  private <T> T createUnitInstanceWhenAbstractClass(Class<T> unitClass, List<Injection> injections) {
    ProxyContractBuilder<T> proxyContractBuilder = ProxyContractBuilder.get()
        .className(unitClass.getCanonicalName() + "Proxy")
        .type(unitClass);

    List<Method> injectedMethods = findInjectedMethods(unitClass);
    for (Method injectedMethod : injectedMethods) {
      createInjection(unitClass, injectedMethod, proxyContractBuilder, injections);
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
      Class<?> unitClass, Method method, ProxyContractBuilder<T> proxyContractBuilder, List<Injection> injections
  ) {
    Class<?> returnClass = method.getReturnType();
    if (ObjectFunctions.isCustomObjectHandleClass(returnClass)) {
      if (method.getParameterCount() == 0) {
        createProjectionInjection(unitClass, method, proxyContractBuilder, injections);
      } else {
        throw ConfigurationException.withMessage("Unit projection injection method can't have parameters. See method {} in unit {}",
            method.getName(), method.getDeclaringClass().getCanonicalName()
        );
      }
    } else {
      throw UnexpectedViolationException.withMessage("Not implemented");
    }
  }

  private <T> void createProjectionInjection(
      Class<?> unitClass, Method method, ProxyContractBuilder<T> proxyContractBuilder, List<Injection> injections
  ) {
    ForcedProjectionInjection injection = new ForcedProjectionInjection(method.getName(), unitClass, method.getReturnType());
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
