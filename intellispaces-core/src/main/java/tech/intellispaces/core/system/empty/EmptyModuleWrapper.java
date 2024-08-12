package tech.intellispaces.core.system.empty;

import tech.intellispaces.core.annotation.Wrapper;
import tech.intellispaces.core.system.Injection;
import tech.intellispaces.core.system.UnitWrapper;

import java.util.List;

@Wrapper(EmptyModule.class)
public class EmptyModuleWrapper extends EmptyModule implements UnitWrapper {

  public EmptyModuleWrapper() {
  }

  @Override
  public List<Injection> getInjections() {
    return List.of();
  }
}
