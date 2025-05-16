package tech.intellispaces.reflections.framework.engine.impl;

/**
 * Default system module starter.
 */
public interface ModuleStarterFunctions {

  static void startModule(ModuleImpl module) {
    loadProjections(module);
    invokeStartupAction(module);
  }

  private static void loadProjections(ModuleImpl module) {
    ((LocalProjectionRegistry) module.projectionRegistry()).load();
  }

  private static void invokeStartupAction(ModuleImpl module) {
    UnitImpl mainUnit = module.mainUnit();
    if (mainUnit.startupAction().isPresent()) {
      mainUnit.startupAction().get().castToAction0().execute();
    }
  }
}
