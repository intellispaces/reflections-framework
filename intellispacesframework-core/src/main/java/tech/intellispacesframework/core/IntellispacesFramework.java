package tech.intellispacesframework.core;

import tech.intellispacesframework.commons.exception.UnexpectedViolationException;
import tech.intellispacesframework.commons.reflection.ReflectionFunctions;
import tech.intellispacesframework.core.system.ModuleFactories;
import tech.intellispacesframework.core.system.Modules;
import tech.intellispacesframework.core.system.Module;

public interface IntellispacesFramework {

  /**
   * Creates system module.
   *
   * @param moduleClass the module class.
   * @return system module.
   */
  static Module createModule(Class<?> moduleClass) {
    return createModule(moduleClass, new String[] {});
  }

  /**
   * Creates system module.
   *
   * @param moduleClass the module class.
   * @param args the command line arguments.
   * @return system module.
   */
  static Module createModule(Class<?> moduleClass, String[] args) {
    Module module = ModuleFactories.get().createModule(moduleClass, args);
    storeCurrentModule(module);
    return module;
  }

  static private void storeCurrentModule(Module module) {
    try {
      ReflectionFunctions.setStaticField(Modules.class, "CURRENT", module);
    } catch (Exception e) {
      throw UnexpectedViolationException.withCauseAndMessage(e, "Failed to store reference to current module");
    }
  }
}
