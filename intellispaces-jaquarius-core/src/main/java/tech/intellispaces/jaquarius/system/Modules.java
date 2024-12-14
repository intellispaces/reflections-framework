package tech.intellispaces.jaquarius.system;

import tech.intellispaces.jaquarius.engine.JaquariusEngines;
import tech.intellispaces.jaquarius.exception.ConfigurationExceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * System modules provider.
 */
public class Modules {
  private final static List<Module> MODULES = new ArrayList<>();

  /**
   * Creates new system module in the application.<p/>
   *
   * @param moduleClass the module class.
   * @param args command line arguments.
   * @return the created module.
   */
  public static Module load(Class<?> moduleClass, String[] args) {
    Module module = JaquariusEngines.get().createModule(List.of(moduleClass), args);
    MODULES.add(module);
    return module;
  }

  /**
   * Creates new system module in the application.<p/>
   *
   * @param unitClasses unit classes.
   * @return the created module.
   */
  public static Module load(Class<?>... unitClasses) {
    Module module = JaquariusEngines.get().createModule(Arrays.stream(unitClasses).toList(), new String[0]);
    MODULES.add(module);
    return module;
  }

  /**
   * Creates new system module in the application.<p/>
   *
   * @param unitClasses unit classes.
   * @param args command line arguments.
   * @return the created module.
   */
  public static Module load(List<Class<?>> unitClasses, String[] args) {
    Module module = JaquariusEngines.get().createModule(unitClasses, new String[0]);
    MODULES.add(module);
    return module;
  }

  /**
   * Unloads current module.
   */
  public static void unload() {
    unload(current());
  }

  /**
   * Unloads given module.
   */
  public static void unload(Module module) {
    module.stop();
    MODULES.remove(module);
  }

  public static void flare(Class<?>... unitClasses) {
    try {
      Modules.load(unitClasses).start();
    } finally {
      Modules.unload();
    }
  }

  /**
   * Returns the current module loaded into the application.<p/>
   *
   * If module is not loaded to application, then exception can be thrown.
   */
  public static Module current() {
    Module module = currentSilently();
    if (module == null) {
      throw ConfigurationExceptions.withMessage("There are no modules loaded into the application");
    }
    return module;
  }

  /**
   * Returns the current module loaded into the application.
   *
   * @return current module or <code>null</code> is module is not loaded into the application.
   */
  public static Module currentSilently() {
    if (MODULES.isEmpty()) {
      return null;
    }
    if (MODULES.size() > 1) {
      throw ConfigurationExceptions.withMessage("Multiple modules are loaded into the application");
    }
    return MODULES.get(0);
  }

  private Modules() {}
}
