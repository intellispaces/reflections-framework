package tech.intellispaces.jaquarius.engine.impl;

import java.util.List;

/**
 * The system module loader.
 */
class ModuleLoader {

  static tech.intellispaces.jaquarius.system.Module loadModule(List<Class<?>> unitClasses, String[] args) {
    Module module = ModuleFactory.createModule(unitClasses);
    ModuleValidator.validate(module);
    return module;
  }
}
