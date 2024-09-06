package intellispaces.framework.core.system;

import intellispaces.framework.core.object.ObjectHandle;
import intellispaces.framework.core.system.kernel.KernelObjectHandle;

public interface ObjectHandleWrapper<D> extends ObjectHandle<D> {

  void $init(KernelObjectHandle handle);

  KernelObjectHandle $handle();
}
