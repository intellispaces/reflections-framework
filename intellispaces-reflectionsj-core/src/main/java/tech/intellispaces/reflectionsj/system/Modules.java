package tech.intellispaces.reflectionsj.system;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import tech.intellispaces.reflectionsj.engine.JaquariusEngines;
import tech.intellispaces.reflectionsj.exception.ConfigurationExceptions;

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
  public static Module create(Class<?> moduleClass, String[] args) {
    return create(() -> JaquariusEngines.get().createModule(List.of(moduleClass), args));
  }

  /**
   * Creates new system module.
   *
   * @param unitClasses unit classes.
   * @return the created module.
   */
  public static Module create(Class<?>... unitClasses) {
    return create(() -> JaquariusEngines.get().createModule(Arrays.stream(unitClasses).toList(), new String[0]));
  }

  /**
   * Creates new system module.
   *
   * @param unitClasses unit classes.
   * @param args command line arguments.
   * @return the created module.
   */
  public static Module create(List<Class<?>> unitClasses, String[] args) {
    return create(() -> JaquariusEngines.get().createModule(unitClasses, new String[0]));
  }

  private static Module create(Supplier<Module> moduleSupplier) {
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
