package tech.intellispaces.framework.core;

import tech.intellispaces.framework.core.system.Module;
import tech.intellispaces.framework.core.system.ModuleLoader;

public interface IntellispacesFramework {

  /**
   * Loads system module to current application.
   *
   * @param unitClasses unit classes.
   * @return system module.
   */
  static Module loadModule(Class<?>... unitClasses) {
    return ModuleLoader.loadDefaultModule(unitClasses);
  }
}
