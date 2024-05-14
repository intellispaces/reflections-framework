package tech.intellispacesframework.core.system;

public class ModuleLoaders {
  private static final ModuleLoader DEFAULT_LOADER = new ModuleDefaultLoader();

  public static ModuleLoader defaultLoader() {
    return DEFAULT_LOADER;
  }

  private ModuleLoaders() {}
}
