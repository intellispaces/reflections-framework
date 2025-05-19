package tech.intellispaces.reflections.framework.engine;

import tech.intellispaces.reflections.framework.system.Module;
import tech.intellispaces.reflections.framework.system.UnitFunctions;
import tech.intellispaces.reflections.framework.system.UnitHandle;

import java.util.List;

public class EngineLoader {

  public static void loadEngine(Engine engine, Module module) {
    loadProjectionDefinitions(engine, module);
    loadLocalGuides(engine, module);
  }

  private static void loadProjectionDefinitions(Engine engine, Module module) {
    module.units().stream()
        .map(UnitHandle::projectionDefinitions)
        .flatMap(List::stream)
        .forEach(engine::addProjection);
  }

  static void loadLocalGuides(Engine engine, Module module) {
    module.units().stream()
        .filter(u -> UnitFunctions.isGuideUnit(u.unitClass()))
        .forEach(u -> {
          engine.addGuide(u.unitClass(), u.unitInstance());
          u.guides().forEach(engine::addGuide);
        });
  }

  private EngineLoader() {}
}
