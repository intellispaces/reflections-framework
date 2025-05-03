package tech.intellispaces.reflectionsj.engine.impl;

/**
 * Default system module starter.
 */
public interface ModuleStarterFunctions {

  static void startModule(Module module) {
    loadProjections(module);
    invokeStartupAction(module);
  }

  private static void loadProjections(Module module) {
    module.projectionRegistry().load();
  }

  private static void invokeStartupAction(Module module) {
    Unit mainUnit = module.mainUnit();
    if (mainUnit.startupAction().isPresent()) {
      mainUnit.startupAction().get().castToAction0().execute();
    }
  }
}
