package intellispaces.jaquarius.system.empty;

import intellispaces.jaquarius.annotation.Wrapper;
import intellispaces.jaquarius.system.UnitWrapper;
import intellispaces.jaquarius.system.kernel.KernelUnit;


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
