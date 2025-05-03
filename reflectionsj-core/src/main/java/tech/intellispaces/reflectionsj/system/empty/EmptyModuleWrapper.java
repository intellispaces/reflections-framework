package tech.intellispaces.reflectionsj.system.empty;

import tech.intellispaces.reflectionsj.annotation.Wrapper;
import tech.intellispaces.reflectionsj.engine.UnitBroker;
import tech.intellispaces.reflectionsj.system.UnitWrapper;

@Wrapper(EmptyModule.class)
public class EmptyModuleWrapper extends EmptyModule implements UnitWrapper {

  @Override
  public UnitBroker $broker() {
    throw new RuntimeException("Not implemented");
  }
}
