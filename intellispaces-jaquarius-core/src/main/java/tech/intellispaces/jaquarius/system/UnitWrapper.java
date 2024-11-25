package tech.intellispaces.jaquarius.system;

import tech.intellispaces.jaquarius.system.kernel.KernelUnit;

public interface UnitWrapper {

  void $init(KernelUnit unit);

  KernelUnit $unit();
}
