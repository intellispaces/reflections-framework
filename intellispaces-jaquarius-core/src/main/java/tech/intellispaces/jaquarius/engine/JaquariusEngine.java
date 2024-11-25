package tech.intellispaces.jaquarius.engine;

import tech.intellispaces.jaquarius.system.Module;

import java.util.List;

/**
 * Internal Jaquarius engine API.
 */
public interface JaquariusEngine {

  /**
   * Loads system module into application.<p/>
   *
   * Only one module can be loaded in application.
   * If the module is already loaded in application, it will be unloaded.
   *
   * @param unitClasses module unit classes.
   * @param args command line arguments.
   */
  Module loadModule(List<Class<?>> unitClasses, String[] args);

  /**
   * Returns current module loaded into application.
   *
   * @return current loaded module or <code>null</code> if module is not loaded into application.
   */
  Module getCurrentModule();
}
