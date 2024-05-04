package tech.intellispacesframework.core.system;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.exception.ConfigurationException;

import java.util.List;
import java.util.Optional;

/**
 * Default system module loader.
 */
class ModuleDefaultLoader {

  public void loadModule(ModuleDefault module) {
    loadProjectionRegistry(module);
    loadInjections(module);
  }

  private void loadProjectionRegistry(ModuleDefault module) {
    ProjectionRegistry projectionRegistry = module.projectionRegistry();
    module.units().stream()
        .<SystemProjection> mapMulti((unit, consumer) -> unit.projectionProviders().forEach(pp -> consumer.accept(createSystemProjection(pp))))
        .forEach(projectionRegistry::addProjection);
  }

  private SystemProjection createSystemProjection(UnitProjectionProvider projectionProvider) {
    final Object target;
    try {
      target = projectionProvider.providerMethod().invoke(projectionProvider.unit().instance());
    } catch (Exception e) {
      throw ConfigurationException.withCauseAndMessage(e, "Failed to create module projection {} declared in unit {}",
          projectionProvider.name(), projectionProvider.unit().unitClass().getCanonicalName());
    }
    return new SystemProjectionDefault(
        projectionProvider.name(),
        projectionProvider.type(),
        projectionProvider,
        target
    );
  }

  private void loadInjections(ModuleDefault module) {
    module.units().stream()
        .map(Unit::injections)
        .flatMap(List::stream)
        .forEach(injection -> loadInjection(injection, module));
  }

  private void loadInjection(Injection injection, ModuleDefault module) {
    if (injection.type() == InjectionTypes.ProjectionInjection) {
      loadProjectionInjection((ProjectionInjection) injection, module.projectionRegistry());
    } else {
      throw UnexpectedViolationException.withMessage("Not implemented");
    }
  }

  private void loadProjectionInjection(ProjectionInjection injection, ProjectionRegistry projectionRegistry) {
    if (injection.isLazy() || injection.isDefined()) {
      return;
    }

    Optional<?> target = projectionRegistry.getProjection(injection.name(), injection.targetClass());
    if (target.isEmpty()) {
      throw ConfigurationException.withMessage("Module projection is not found. See injection {} in unit {}",
          injection.name(), injection.targetClass().getCanonicalName());
    }

    var forcedInjection = (ForcedProjectionInjection) injection;
    forcedInjection.setValue(target.get());
  }
}
