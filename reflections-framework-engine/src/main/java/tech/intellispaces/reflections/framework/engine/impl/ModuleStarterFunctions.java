package tech.intellispaces.reflections.framework.engine.impl;

import tech.intellispaces.reflections.framework.system.LocalProjectionRegistry;
import tech.intellispaces.reflections.framework.system.UnitHandle;

/**
 * Default system module starter.
 */
public interface ModuleStarterFunctions {

  static void startModule(ModuleImpl module) {
    loadProjections(module);
    invokeStartupAction(module);
  }

  private static void loadProjections(ModuleImpl module) {
    ((LocalProjectionRegistry) module.projectionRegistry()).onStartup();
  }

  private static void invokeStartupAction(ModuleImpl module) {
    UnitHandle mainUnit = module.mainUnit();
    if (mainUnit.startupAction().isPresent()) {
      mainUnit.startupAction().get().castToAction0().execute();
    }
  }
}
