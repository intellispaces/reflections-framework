package tech.intellispaces.reflections.framework.system.empty;

import java.util.List;

import tech.intellispaces.reflections.framework.annotation.Wrapper;
import tech.intellispaces.reflections.framework.system.UnitHandle;
import tech.intellispaces.reflections.framework.system.UnitType;
import tech.intellispaces.reflections.framework.system.UnitTypes;
import tech.intellispaces.reflections.framework.system.UnitWrapper;

@Wrapper(EmptyModule.class)
public class EmptyModuleWrapper extends EmptyModule implements UnitWrapper {
  private final UnitHandle $handle;

  public EmptyModuleWrapper(UnitHandle handle) {
    this.$handle = handle;
  }

  @Override
  public UnitType $unitType() {
    return UnitTypes.create(EmptyModule.class, List.of());
  }

  @Override
  public UnitHandle $handle() {
    return this.$handle;
  }
}
