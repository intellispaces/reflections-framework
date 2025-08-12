package tech.intellispaces.reflections.framework.system;

import java.util.List;
import java.util.Map;

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
   * @param args command line arguments.
   * @param unitClasses unit classes.
   * @return the created module.
   */
  public static ModuleHandle load(List<Class<?>> unitClasses, String[] args, Map<String, Object> engineAttributes) {
    synchronized (Modules.class) {
      Engine engine = Engines.create(args, engineAttributes);
      ModuleHandle module = createModule(unitClasses, engine);
      validateModule(module);
      moduleHolder().set(module);
      loadEngine(engine, module);
      engine.onModuleLoad(module);
      return module;
    }
  }

  /**
   * Unloads the module.
   *
   * @param module the module.
   */
  public static void unload(ReflectionModule module) {
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
