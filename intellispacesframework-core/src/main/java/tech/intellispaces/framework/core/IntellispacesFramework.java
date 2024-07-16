package tech.intellispaces.framework.core;

import tech.intellispaces.framework.core.system.Module;
import tech.intellispaces.framework.core.system.ModuleLoader;

public interface IntellispacesFramework {

  /**
   * Loads system module to current application.
   *
   * @param classes the module class.
   * @return system module.
   */
  static Module loadModule(Class<?>... classes) {
    return ModuleLoader.loadDefaultModule(classes);
  }
}
