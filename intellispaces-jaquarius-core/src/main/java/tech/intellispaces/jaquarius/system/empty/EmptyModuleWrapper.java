package tech.intellispaces.jaquarius.system.empty;

import tech.intellispaces.jaquarius.annotation.Wrapper;
import tech.intellispaces.jaquarius.system.UnitWrapper;
import tech.intellispaces.jaquarius.system.kernel.KernelUnit;


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
