package intellispaces.core.system.empty;

import intellispaces.core.annotation.Wrapper;
import intellispaces.core.system.UnitWrapper;
import intellispaces.core.system.kernel.KernelUnit;


@Wrapper(EmptyModule.class)
public class EmptyModuleWrapper extends EmptyModule implements UnitWrapper {
  private KernelUnit unit;

  @Override
  public void $init(KernelUnit unit) {
    this.unit = unit;
  }

  @Override
  public KernelUnit $unit() {
    return unit;
  }
}
