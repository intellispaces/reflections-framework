package tech.intellispaces.reflections.framework;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import tech.intellispaces.core.Module;
import tech.intellispaces.reflections.framework.system.Modules;

/**
 * Reflections framework.
 */
public class ReflectionsFramework {

  /**
   * Creates and loads a new system module.
   *
   * @param moduleClass the module class.
   * @return the loaded module.
   */
  public static Module loadModule(Class<?> moduleClass) {
    return Modules.load(List.of(moduleClass), new String[0], Map.of());
  }

  /**
   * Creates and loads a new system module.
   *
   * @param moduleClass the module class.
   * @param args command line arguments.
   * @return the loaded module.
   */
  public static Module loadModule(Class<?> moduleClass, String[] args) {
    return loadModule(List.of(moduleClass), args, Map.of());
  }

  /**
   * Creates and loads a new system module.
   *
   * @param moduleClass the module class.
   * @param engineAttribute engine attributes.
   * @return the loaded module.
   */
  public static Module loadModule(Class<?> moduleClass, Map<String, Object> engineAttribute) {
    return Modules.load(List.of(moduleClass), new String[0], engineAttribute);
  }

  /**
   * Creates and loads a new system module.
   *
   * @param unitClasses unit classes.
   * @return the loaded module.
   */
  public static Module loadModule(Class<?>... unitClasses) {
    return Modules.load(Arrays.stream(unitClasses).toList(), new String[0], Map.of());
  }

  /**
   * Creates and loads a new system module.
   *
   * @param unitClasses unit classes.
   * @param args command line arguments.
   * @param engineAttribute engine attributes.
   * @return the loaded module.
   */
  public static Module loadModule(
      List<Class<?>> unitClasses,
      String[] args,
      Map<String, Object> engineAttribute
  ) {
    return Modules.load(unitClasses, args, engineAttribute);
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
