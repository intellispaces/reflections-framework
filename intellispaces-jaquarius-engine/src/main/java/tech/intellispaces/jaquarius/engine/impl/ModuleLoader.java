package tech.intellispaces.jaquarius.engine.impl;

import java.util.List;

/**
 * The system module loader.
 */
public class ModuleLoader {
  private static final ModuleFactory FACTORY = new ModuleFactory();
  private static final ModuleValidator MODULE_VALIDATOR = new ModuleValidator();

  public static tech.intellispaces.jaquarius.system.Module load(List<Class<?>> unitClasses, String[] args) {
    Module module = FACTORY.createModule(unitClasses);
    MODULE_VALIDATOR.validate(module);
    return module;
  }
}
