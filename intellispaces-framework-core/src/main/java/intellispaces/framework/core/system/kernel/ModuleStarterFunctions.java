package intellispaces.framework.core.system.kernel;

/**
 * Default system module starter.
 */
public interface ModuleStarterFunctions {

  static void startModule(SystemModule module) {
    loadProjections(module);
    invokeStartupAction(module);
  }

  private static void loadProjections(SystemModule module) {
    module.projectionRegistry().load();
  }

  private static void invokeStartupAction(SystemModule module) {
    SystemUnit mainUnit = module.mainUnit();
    if (mainUnit.startupAction().isPresent()) {
      mainUnit.startupAction().get().execute();
    }
  }
}
