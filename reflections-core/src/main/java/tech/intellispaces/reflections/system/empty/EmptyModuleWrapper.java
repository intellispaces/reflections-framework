package tech.intellispaces.reflections.system.empty;

import tech.intellispaces.reflections.annotation.Wrapper;
import tech.intellispaces.reflections.engine.UnitBroker;
import tech.intellispaces.reflections.system.UnitWrapper;

@Wrapper(EmptyModule.class)
public class EmptyModuleWrapper extends EmptyModule implements UnitWrapper {

  @Override
  public UnitBroker $broker() {
    throw new RuntimeException("Not implemented");
  }
}
