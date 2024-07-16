package tech.intellispaces.framework.core.system.empty;

import tech.intellispaces.framework.core.annotation.Wrapper;
import tech.intellispaces.framework.core.system.Injection;
import tech.intellispaces.framework.core.system.Modules;
import tech.intellispaces.framework.core.system.ProjectionRegistry;
import tech.intellispaces.framework.core.system.UnitWrapper;

import java.util.List;

@Wrapper(EmptyModule.class)
public class EmptyModuleWrapper extends EmptyModule implements UnitWrapper {

  public EmptyModuleWrapper() {
  }

  @Override
  public List<Injection> getInjections() {
    return List.of();
  }

  private ProjectionRegistry getProjectionRegistry() {
    return Modules.activeModule().projectionRegistry();
  }
}
