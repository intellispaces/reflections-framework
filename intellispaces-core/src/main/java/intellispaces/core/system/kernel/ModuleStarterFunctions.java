package intellispaces.core.system.kernel;

/**
 * Default system module starter.
 */
public interface ModuleStarterFunctions {

  static void startModule(KernelModule module) {
    loadProjections(module);
    invokeStartupAction(module);
  }

  private static void loadProjections(KernelModule module) {
    module.projectionRegistry().load();
  }

  private static void invokeStartupAction(KernelModule module) {
    KernelUnit mainUnit = module.mainUnit();
    if (mainUnit.startupAction().isPresent()) {
      mainUnit.startupAction().get().execute();
    }
  }
}
