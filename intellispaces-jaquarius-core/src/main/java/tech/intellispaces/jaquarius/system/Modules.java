package tech.intellispaces.jaquarius.system;

import tech.intellispaces.jaquarius.engine.JaquariusEngines;
import tech.intellispaces.jaquarius.exception.ConfigurationExceptions;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * System modules provider.
 */
public class Modules {
  private final static AtomicReference<Module> MODULE = new AtomicReference<>();

  /**
   * Creates new system module.
   *
   * @param moduleClass the module class.
   * @param args command line arguments.
   * @return the created module.
   */
  public static Module load(Class<?> moduleClass, String[] args) {
    return load(() -> JaquariusEngines.get().createModule(List.of(moduleClass), args));
  }

  /**
   * Creates new system module.
   *
   * @param unitClasses unit classes.
   * @return the created module.
   */
  public static Module load(Class<?>... unitClasses) {
    return load(() -> JaquariusEngines.get().createModule(Arrays.stream(unitClasses).toList(), new String[0]));
  }

  /**
   * Creates new system module.
   *
   * @param unitClasses unit classes.
   * @param args command line arguments.
   * @return the created module.
   */
  public static Module load(List<Class<?>> unitClasses, String[] args) {
    return load(() -> JaquariusEngines.get().createModule(unitClasses, new String[0]));
  }

  private static Module load(Supplier<Module> moduleSupplier) {
    if (MODULE.get() != null) {
      throw ConfigurationExceptions.withMessage("The module has already been uploaded to the application");
    }
    Module module = moduleSupplier.get();
    if (!MODULE.compareAndSet(null, module)) {
      throw ConfigurationExceptions.withMessage("The module has already been uploaded to the application");
    };
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
    MODULE.set(null);
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
      throw ConfigurationExceptions.withMessage("There are no module loaded into the application");
    }
    return module;
  }

  /**
   * Returns the current module loaded into the application.
   *
   * @return current module or <code>null</code> is module is not loaded into the application.
   */
  public static Module currentSilently() {
    return MODULE.get();
  }

  private Modules() {}
}
