package tech.intellispacesframework.core.system;

import tech.intellispacesframework.core.exception.ConfigurationException;

/**
 * Default system module loader.
 */
class SystemModuleDefaultLoader {

  public void loadModule(SystemModuleDefault module) {
    loadProjectionRegistry(module);
  }

  private void loadProjectionRegistry(SystemModuleDefault module) {
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
}
