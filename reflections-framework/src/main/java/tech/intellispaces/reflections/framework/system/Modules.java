package tech.intellispaces.reflections.framework.system;

import java.util.Arrays;
import java.util.List;

import tech.intellispaces.reflections.framework.engine.Engine;
import tech.intellispaces.reflections.framework.engine.Engines;
import tech.intellispaces.reflections.framework.exception.ConfigurationExceptions;
import tech.intellispaces.reflections.framework.node.NodeFunctions;

import static tech.intellispaces.reflections.framework.engine.EngineLoader.loadEngine;
import static tech.intellispaces.reflections.framework.system.ModuleFactory.createModule;
import static tech.intellispaces.reflections.framework.system.ModuleValidator.validateModule;

/**
 * System modules provider.
 */
public class Modules {
  private static System SYSTEM;
  private static ModuleHandle MODULE;

  /**
   * Loads a new system module.
   *
   * @param moduleClass the module class.
   * @param args command line arguments.
   * @return the created module.
   */
  public static ModuleHandle load(Class<?> moduleClass, String[] args) {
    return load(List.of(moduleClass), args);
  }

  /**
   * Loads a new system module.
   *
   * @param unitClasses unit classes.
   * @return the created module.
   */
  public static ModuleHandle load(Class<?>... unitClasses) {
    return load(Arrays.stream(unitClasses).toList(), new String[0]);
  }

  /**
   * Loads a new system module.
   *
   * @param unitClasses unit classes.
   * @param args command line arguments.
   * @return the created module.
   */
  public static ModuleHandle load(List<Class<?>> unitClasses, String[] args) {
    synchronized (Modules.class) {
      if (MODULE != null) {
        throw ConfigurationExceptions.withMessage("The module has already been uploaded");
      }

      Engine engine = Engines.create(args);
      NodeFunctions.engineHolder().set(engine);
      ModuleHandleImpl module = createModule(unitClasses, engine);
      validateModule(module);
      loadEngine(engine, module);

      MODULE = module;
      SYSTEM = new SystemImpl(module, engine);
    }
    return MODULE;
  }

  /**
   * Unloads the current module.
   */
  public static void unload() {
    synchronized (Modules.class) {
      current().stop();
      NodeFunctions.engineHolder().set(null);
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
  public static ModuleHandle current() {
    ModuleHandle module = currentSilently();
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
  public static ModuleHandle currentSilently() {
    return MODULE;
  }

  private Modules() {}
}
