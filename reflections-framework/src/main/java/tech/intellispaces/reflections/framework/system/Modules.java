package tech.intellispaces.reflections.framework.system;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import tech.intellispaces.reflections.framework.engine.Engines;
import tech.intellispaces.reflections.framework.exception.ConfigurationExceptions;

/**
 * System modules provider.
 */
public class Modules {
  private final static AtomicReference<Module> MODULE = new AtomicReference<>();

  /**
   * Loads a new system module.
   *
   * @param moduleClass the module class.
   * @param args command line arguments.
   * @return the created module.
   */
  public static Module load(Class<?> moduleClass, String[] args) {
    return load(List.of(moduleClass), args);
  }

  /**
   * Loads a new system module.
   *
   * @param unitClasses unit classes.
   * @return the created module.
   */
  public static Module load(Class<?>... unitClasses) {
    return load(Arrays.stream(unitClasses).toList(), new String[0]);
  }

  /**
   * Loads a new system module.
   *
   * @param unitClasses unit classes.
   * @param args command line arguments.
   * @return the created module.
   */
  public static Module load(List<Class<?>> unitClasses, String[] args) {
    if (MODULE.get() != null) {
      throw ConfigurationExceptions.withMessage("The module has already been uploaded");
    }
    Engines.get().load(unitClasses, args);
    Module module = Engines.get().createModule(unitClasses, args);    // todo: remove
    if (!MODULE.compareAndSet(null, module)) {
      throw ConfigurationExceptions.withMessage("The module has already been uploaded");
    };
    return module;
  }

  /**
   * Unloads the current module.
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
   * Returns the current loaded module.<p/>
   *
   * If there is no loaded module, an exception will be thrown.
   */
  public static Module current() {
    Module module = currentSilently();
    if (module == null) {
      throw ConfigurationExceptions.withMessage("There are no loaded module");
    }
    return module;
  }

  /**
   * Returns the current loaded module.
   *
   * @return current module or <code>null</code> if there are no loaded module.
   */
  public static Module currentSilently() {
    return MODULE.get();
  }

  private Modules() {}
}
