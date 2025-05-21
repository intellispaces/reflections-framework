package tech.intellispaces.reflections.framework;

import java.util.List;

import tech.intellispaces.core.Module;
import tech.intellispaces.reflections.framework.system.Modules;

/**
 * The reflections framework.
 */
public class ReflectionsFramework {

  /**
   * Loads a new system module.
   *
   * @param moduleClass the module class.
   * @param args command line arguments.
   * @return the loaded module.
   */
  public static Module loadModule(Class<?> moduleClass, String[] args) {
    return Modules.load(moduleClass, args);
  }

  /**
   * Loads a new system module.
   *
   * @param unitClasses unit classes.
   * @return the loaded module.
   */
  public static Module loadModule(Class<?>... unitClasses) {
    return Modules.load(unitClasses);
  }

  /**
   * Loads a new system module.
   *
   * @param unitClasses unit classes.
   * @param args command line arguments.
   * @return the loaded module.
   */
  public static Module loadModule(List<Class<?>> unitClasses, String[] args) {
    return Modules.load(unitClasses, args);
  }

  /**
   * Creates, starts and then stops and uploads module.
   *
   * @param unitClasses module unit classes.
   */
  public static void flashModule(Class<?>... unitClasses) {
    Module module = null;
    try {
      module = loadModule(unitClasses);
      module.start();
    } finally {
      if (module != null) {
        Modules.unload(module);
      }
    }
  }
}
