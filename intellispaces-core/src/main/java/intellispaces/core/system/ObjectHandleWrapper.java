package intellispaces.core.system;

import intellispaces.core.object.ObjectHandle;
import intellispaces.core.system.kernel.KernelObjectHandle;

public interface ObjectHandleWrapper<D> extends ObjectHandle<D> {

  void $init(KernelObjectHandle handle);

  KernelObjectHandle $handle();
}
