package tech.intellispaces.jaquarius.system.empty;

import tech.intellispaces.jaquarius.annotation.Wrapper;
import tech.intellispaces.jaquarius.engine.UnitBroker;
import tech.intellispaces.jaquarius.system.UnitWrapper;

@Wrapper(EmptyModule.class)
public class EmptyModuleWrapper extends EmptyModule implements UnitWrapper {

  @Override
  public UnitBroker $broker() {
    throw new RuntimeException("Not implemented");
  }
}
