package intellispaces.framework.core.system.empty;

import intellispaces.framework.core.annotation.Wrapper;
import intellispaces.framework.core.system.UnitWrapper;
import intellispaces.framework.core.system.kernel.SystemUnit;


@Wrapper(EmptyModule.class)
public class EmptyModuleWrapper extends EmptyModule implements UnitWrapper {
  private SystemUnit unit;

  @Override
  public void $init(SystemUnit unit) {
    this.unit = unit;
  }

  @Override
  public SystemUnit $unit() {
    return unit;
  }
}
