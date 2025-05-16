package tech.intellispaces.reflections.framework.system;

import java.util.Arrays;
import java.util.List;

import tech.intellispaces.reflections.framework.engine.Engine;
import tech.intellispaces.reflections.framework.engine.Engines;
import tech.intellispaces.reflections.framework.exception.ConfigurationExceptions;

/**
 * System modules provider.
 */
public class Modules {
  private static System SYSTEM;
  private static Module MODULE ;

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
    synchronized (Modules.class) {
      if (MODULE != null) {
        throw ConfigurationExceptions.withMessage("The module has already been uploaded");
      }

      Engine engine = Engines.create(args);
      Module module = ModuleFactory.createModule(unitClasses, engine);
      module = Engines.get().createModule(unitClasses, args);    // todo: remove

      MODULE = module;
      SYSTEM = new SystemImpl(module, Engines.get());
    }
    return MODULE;
  }

  /**
   * Unloads the current module.
   */
  public static void unload() {
    synchronized (Modules.class) {
      current().stop();
      MODULE = null;
      SYSTEM = null;
    }
  }

  /**
   * Returns the current loaded system.
   * <p>
   * If there is no current system, an exception will be thrown.
   */
  public static System currentSystem() {
    if (SYSTEM == null) {
      throw ConfigurationExceptions.withMessage("There are no loaded system");
    }
    return SYSTEM;
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
    return MODULE;
  }

  private Modules() {}
}
