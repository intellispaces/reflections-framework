package tech.intellispaces.reflections.framework.system.empty;

import java.util.List;

import tech.intellispaces.reflections.framework.annotation.Wrapper;
import tech.intellispaces.reflections.framework.engine.UnitBroker;
import tech.intellispaces.reflections.framework.system.UnitType;
import tech.intellispaces.reflections.framework.system.UnitTypes;
import tech.intellispaces.reflections.framework.system.UnitWrapper;

@Wrapper(EmptyModule.class)
public class EmptyModuleWrapper extends EmptyModule implements UnitWrapper {

  @Override
  public UnitType unitType() {
    return UnitTypes.create(EmptyModule.class, List.of());
  }

  @Override
  public UnitBroker $broker() {
    throw new RuntimeException("Not implemented");
  }
}
