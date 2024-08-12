package tech.intellispaces.core.system;

/**
 * Default system module starter.
 */
public interface ModuleStarterFunctions {

  static void startModule(ModuleDefault module) {
    loadProjections(module);
    invokeStartupAction(module);
  }

  private static void loadProjections(ModuleDefault module) {
    module.projectionRegistry().load();
  }

  private static void invokeStartupAction(ModuleDefault module) {
    Unit mainUnit = module.mainUnit();
    if (mainUnit.startupAction().isPresent()) {
      mainUnit.startupAction().get().execute();
    }
  }
}
