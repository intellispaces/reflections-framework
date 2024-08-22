package intellispaces.core.system.empty;

import intellispaces.core.annotation.Wrapper;
import intellispaces.core.system.UnitWrapper;
import intellispaces.core.system.shadow.ShadowUnit;


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
