package tech.intellispaces.core.system.empty;

import tech.intellispaces.core.annotation.Wrapper;
import tech.intellispaces.core.system.UnitWrapper;
import tech.intellispaces.core.system.shadow.ShadowUnit;


@Wrapper(EmptyModule.class)
public class EmptyModuleWrapper extends EmptyModule implements UnitWrapper {
  private ShadowUnit shadowUnit;

  @Override
  public void $init(ShadowUnit shadowUnit) {
    this.shadowUnit = shadowUnit;
  }

  @Override
  public ShadowUnit $shadowUnit() {
    return shadowUnit;
  }
}
