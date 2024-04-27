package tech.intellispacesframework.core;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.core.system.ModuleFactories;
import tech.intellispacesframework.core.system.Modules;
import tech.intellispacesframework.core.system.SystemModule;

import java.lang.reflect.Method;

public interface IntellispacesFramework {

  /**
   * Creates system module.
   *
   * @param moduleClass the module class.
   * @return system module.
   */
  static SystemModule createSystemModule(Class<?> moduleClass) {
    return createSystemModule(moduleClass, new String[] {});
  }

  /**
   * Creates system module.
   *
   * @param moduleClass the module class.
   * @param args the command line arguments.
   * @return system module.
   */
  static SystemModule createSystemModule(Class<?> moduleClass, String[] args) {
    SystemModule module = ModuleFactories.get().createModule(moduleClass, args);
    defineCurrentModule(module);
    return module;
  }

  static private void defineCurrentModule(SystemModule module) {
    try {
      Method m = Modules.class.getDeclaredMethod("setCurrent", SystemModule.class);
      m.setAccessible(true);
      m.invoke(null, module);
      m.setAccessible(false);
    } catch (Exception e) {
      throw UnexpectedViolationException.withCauseAndMessage(e, "Failed to store reference to current module");
    }
  }
}
