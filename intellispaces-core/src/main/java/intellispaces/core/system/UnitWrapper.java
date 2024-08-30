package intellispaces.core.system;

import intellispaces.core.system.kernel.KernelUnit;

public interface UnitWrapper {

  void $init(KernelUnit unit);

  KernelUnit $unit();
}
