package intellispaces.jaquarius.system;

import intellispaces.jaquarius.system.kernel.KernelUnit;

public interface UnitWrapper {

  void $init(KernelUnit unit);

  KernelUnit $unit();
}
