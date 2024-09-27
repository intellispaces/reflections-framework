package intellispaces.framework.core.system;

import intellispaces.framework.core.system.kernel.KernelObjectHandle;

public interface ObjectHandleWrapper {

  void $init(KernelObjectHandle handle);

  KernelObjectHandle $handle();
}
