package tech.intellispaces.jaquarius.system;

import tech.intellispaces.entity.exception.UnexpectedExceptions;
import tech.intellispaces.jaquarius.engine.JaquariusEngines;
import tech.intellispaces.jaquarius.system.kernel.KernelFunctions;
import tech.intellispaces.jaquarius.system.kernel.KernelModule;

import java.util.Arrays;
import java.util.List;

/**
 * Intellispaces system modules provider.
 */
public interface Modules {

  /**
   * Loads Intellispaces system module into application.<p/>
   *
   * Only one module can be loaded in application.
   * If the module is already loaded in application, it will be unloaded.
   *
   * @param moduleClass module class.
   * @param args command line arguments.
   * @return loaded module.
   */
  static Module get(Class<?> moduleClass, String[] args) {
    return JaquariusEngines.get().createModule(List.of(moduleClass), args);
  }

  /**
   * Loads Intellispaces system module into application.<p/>
   *
   * Only one module can be loaded in application.
   * If the module is already loaded in application, it will be unloaded.
   *
   * @param units module unit classes.
   * @return loaded module.
   */
  static Module get(Class<?>... units) {
    return JaquariusEngines.get().createModule(Arrays.stream(units).toList(), new String[0]);
  }

  /**
   * Loads Intellispaces system module into application.<p/>
   *
   * Only one module can be loaded in application.
   * If the module is already loaded in application, it will be unloaded.
   *
   * @param units module unit classes.
   * @param args command line arguments.
   * @return loaded module.
   */
  static Module get(List<Class<?>> units, String[] args) {
    return JaquariusEngines.get().createModule(units, new String[0]);
  }

  /**
   * Returns current module loaded into application.<p/>
   *
   * If module is not loaded to application, then exception can be thrown.
   */
  static Module current() {
    KernelModule km = KernelFunctions.currentModuleSilently();
    if (km == null || km.module() == null) {
      throw UnexpectedExceptions.withMessage("Current module is not defined. " +
          "It is possible that the module is not loaded yet");
    }
    return km.module();
  }

  /**
   * Returns current module loaded into application.
   *
   * @return module or <code>null</code> is module is not loaded into application.
   */
  static Module currentSilently() {
    KernelModule km = KernelFunctions.currentModuleSilently();
    return km != null ? km.module() : null;
  }
}
