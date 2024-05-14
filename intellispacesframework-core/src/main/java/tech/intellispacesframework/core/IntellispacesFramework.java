package tech.intellispacesframework.core;

import tech.intellispacesframework.core.system.ModuleLoaders;
import tech.intellispacesframework.core.system.Module;

public interface IntellispacesFramework {

  /**
   * Loads system module to current application.
   *
   * @param moduleClass the module class.
   * @return system module.
   */
  static Module loadModule(Class<?> moduleClass) {
    return loadModule(moduleClass, new String[] {});
  }

  /**
   * Loads system module to current application.
   *
   * @param moduleClass the module class.
   * @param args the command line arguments.
   * @return system module.
   */
  static Module loadModule(Class<?> moduleClass, String[] args) {
    return ModuleLoaders.defaultLoader().loadModule(moduleClass, args);
  }
}
