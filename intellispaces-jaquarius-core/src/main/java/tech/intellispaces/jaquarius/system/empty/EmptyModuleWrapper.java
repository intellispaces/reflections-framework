package tech.intellispaces.jaquarius.system.empty;

import tech.intellispaces.jaquarius.annotation.Wrapper;
import tech.intellispaces.jaquarius.engine.UnitAgent;
import tech.intellispaces.jaquarius.system.UnitWrapper;

@Wrapper(EmptyModule.class)
public class EmptyModuleWrapper extends EmptyModule implements UnitWrapper {

  @Override
  public UnitAgent $agent() {
    throw new RuntimeException("Not implemented");
  }
}
