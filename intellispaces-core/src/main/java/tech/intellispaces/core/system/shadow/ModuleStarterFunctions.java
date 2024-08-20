package tech.intellispaces.core.system.shadow;

/**
 * Default system module starter.
 */
public interface ModuleStarterFunctions {

  static void startModule(ShadowModule module) {
    loadProjections(module);
    invokeStartupAction(module);
  }

  private static void loadProjections(ShadowModule module) {
    module.projectionRegistry().load();
  }

  private static void invokeStartupAction(ShadowModule module) {
    ShadowUnit mainUnit = module.mainUnit();
    if (mainUnit.startupAction().isPresent()) {
      mainUnit.startupAction().get().execute();
    }
  }
}
