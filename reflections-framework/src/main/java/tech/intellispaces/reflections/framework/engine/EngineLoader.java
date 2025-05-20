package tech.intellispaces.reflections.framework.engine;

import java.util.List;

import tech.intellispaces.reflections.framework.system.ModuleHandle;
import tech.intellispaces.reflections.framework.system.UnitFunctions;
import tech.intellispaces.reflections.framework.system.UnitHandle;
import tech.intellispaces.reflections.framework.system.projection.DirectProjectionDefinition;
import tech.intellispaces.reflections.framework.system.projection.DirectProjectionDefinitions;

public class EngineLoader {

  public static void loadEngine(Engine engine, ModuleHandle module) {
    loadProjectionDefinitions(engine, module);
    loadLocalGuides(engine, module);
  }

  private static void loadProjectionDefinitions(Engine engine, ModuleHandle module) {
    module.unitHandles().stream()
        .map(UnitHandle::projectionDefinitions)
        .flatMap(List::stream)
        .forEach(engine::addProjection);
    module.unitHandles().stream()
        .filter(u -> UnitFunctions.isGuideUnit(u.unitClass()))
        .map(EngineLoader::buildGuideProjectionDefinition)
        .forEach(engine::addProjection);
  }

  private static DirectProjectionDefinition buildGuideProjectionDefinition(UnitHandle unit) {
    return DirectProjectionDefinitions.get(
        unit.unitClass().getCanonicalName(),
        unit.unitClass(),
        unit.unitInstance()
    );
  }

  static void loadLocalGuides(Engine engine, ModuleHandle module) {
    module.unitHandles().stream()
        .filter(u -> UnitFunctions.isGuideUnit(u.unitClass()))
        .forEach(u -> u.guides().forEach(engine::addGuide));
  }

  private EngineLoader() {}
}
