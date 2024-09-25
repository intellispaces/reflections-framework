package intellispaces.framework.core.system;

import intellispaces.framework.core.system.kernel.KernelUnit;

public interface UnitWrapper {

  void $init(KernelUnit unit);

  KernelUnit $unit();
}
