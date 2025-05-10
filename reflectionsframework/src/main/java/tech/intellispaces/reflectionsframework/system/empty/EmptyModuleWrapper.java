package tech.intellispaces.reflectionsframework.system.empty;

import tech.intellispaces.reflectionsframework.annotation.Wrapper;
import tech.intellispaces.reflectionsframework.engine.UnitBroker;
import tech.intellispaces.reflectionsframework.system.UnitWrapper;

@Wrapper(EmptyModule.class)
public class EmptyModuleWrapper extends EmptyModule implements UnitWrapper {

  @Override
  public UnitBroker $broker() {
    throw new RuntimeException("Not implemented");
  }
}
