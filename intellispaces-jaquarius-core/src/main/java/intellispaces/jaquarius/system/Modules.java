package intellispaces.jaquarius.system;

import intellispaces.common.base.exception.UnexpectedExceptions;
import intellispaces.jaquarius.engine.JaquariusEngines;

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
    return JaquariusEngines.get().loadModule(List.of(moduleClass), args);
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
    return JaquariusEngines.get().loadModule(Arrays.stream(units).toList(), new String[0]);
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
    return JaquariusEngines.get().loadModule(units, new String[0]);
  }

  /**
   * Returns current module loaded into application.<p/>
   *
   * If module is not loaded to application, then exception can be thrown.
   */
  static Module current() {
    Module module = JaquariusEngines.get().getCurrentModule();
    if (module == null) {
      throw UnexpectedExceptions.withMessage("Current module is not defined. " +
          "It is possible that the module is not loaded yet");
    }
    return module;
  }

  /**
   * Returns current module loaded into application.
   *
   * @return module or <code>null</code> is module is not loaded into application.
   */
  static Module currentSilently() {
    return JaquariusEngines.get().getCurrentModule();
  }
}
