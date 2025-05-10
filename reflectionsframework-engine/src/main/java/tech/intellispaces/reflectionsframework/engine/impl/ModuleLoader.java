package tech.intellispaces.reflectionsframework.engine.impl;

import java.util.List;

/**
 * The system module loader.
 */
class ModuleLoader {

  static tech.intellispaces.reflectionsframework.system.Module loadModule(List<Class<?>> unitClasses, String[] args) {
    Module module = ModuleFactory.createModule(unitClasses);
    ModuleValidator.validate(module);
    return module;
  }
}
