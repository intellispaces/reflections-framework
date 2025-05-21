package tech.intellispaces.reflections.framework.system;

import java.util.Arrays;
import java.util.List;

import tech.intellispaces.core.Module;
import tech.intellispaces.reflections.framework.engine.Engine;
import tech.intellispaces.reflections.framework.engine.Engines;
import tech.intellispaces.reflections.framework.exception.ConfigurationExceptions;

import static tech.intellispaces.reflections.framework.engine.EngineLoader.loadEngine;
import static tech.intellispaces.reflections.framework.node.ReflectionsNodeFunctions.moduleHolder;
import static tech.intellispaces.reflections.framework.system.ModuleFactory.createModule;
import static tech.intellispaces.reflections.framework.system.ModuleValidator.validateModule;

/**
 * System modules provider.
 */
public class Modules {

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
      Engine engine = Engines.create(args);
      ModuleHandle module = createModule(unitClasses, engine);
      validateModule(module);
      moduleHolder().set(module);
      loadEngine(engine, module);
      return module;
    }
  }

  /**
   * Unloads the module.
   *
   * @param module the module.
   */
  public static void unload(Module module) {
    synchronized (Modules.class) {
      module.stop();
      moduleHolder().set(null);
    }
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
    return moduleHolder().get();
  }

  private Modules() {}
}
