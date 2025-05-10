package tech.intellispaces.reflections.framework.engine.impl;

import java.util.List;

/**
 * The system module loader.
 */
class ModuleLoader {

  static tech.intellispaces.reflections.framework.system.Module loadModule(List<Class<?>> unitClasses, String[] args) {
    Module module = ModuleFactory.createModule(unitClasses);
    ModuleValidator.validate(module);
    return module;
  }
}
