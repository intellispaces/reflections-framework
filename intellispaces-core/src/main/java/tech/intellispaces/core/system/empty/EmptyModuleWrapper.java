package tech.intellispaces.core.system.empty;

import tech.intellispaces.core.annotation.Wrapper;
import tech.intellispaces.core.system.Injection;
import tech.intellispaces.core.system.UnitWrapper;
import tech.intellispaces.core.system.shadow.ShadowUnit;

import java.util.List;

@Wrapper(EmptyModule.class)
public class EmptyModuleWrapper extends EmptyModule implements UnitWrapper {
  private ShadowUnit $unit;

  @Override
  public ShadowUnit $shadowUnit() {
    return $unit;
  }

  @Override
  public void $init(ShadowUnit shadowUnit) {
    this.$unit = shadowUnit;
  }

  @Override
  public List<Injection> getInjections() {
    return List.of();
  }
}
