package intellispaces.framework.core.system.empty;

import intellispaces.framework.core.annotation.Wrapper;
import intellispaces.framework.core.system.UnitWrapper;
import intellispaces.framework.core.system.kernel.KernelUnit;


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
