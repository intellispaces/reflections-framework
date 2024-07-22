package tech.intellispaces.framework.core;

import tech.intellispaces.framework.core.system.ModuleLoader;

public interface IntellispacesFramework {

  /**
   * Loads system module to current application.
   *
   * @param moduleClass module class.
   * @param args command line arguments.
   */
  static void loadModule(Class<?> moduleClass, String[] args) {
    ModuleLoader.loadModule(moduleClass, args);
  }

  /**
   * Loads system module to current application.
   *
   * @param unitClasses unit classes.
   */
  static void loadModule(Class<?>... unitClasses) {
    ModuleLoader.loadModule(unitClasses);
  }

  /**
   * Unload current module.
   */
  static void unloadModule() {
    ModuleLoader.unloadModule();
  }
}
