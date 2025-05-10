package tech.intellispaces.reflections.framework.system.empty;

import tech.intellispaces.reflections.framework.annotation.Wrapper;
import tech.intellispaces.reflections.framework.engine.UnitBroker;
import tech.intellispaces.reflections.framework.system.UnitWrapper;

@Wrapper(EmptyModule.class)
public class EmptyModuleWrapper extends EmptyModule implements UnitWrapper {

  @Override
  public UnitBroker $broker() {
    throw new RuntimeException("Not implemented");
  }
}
