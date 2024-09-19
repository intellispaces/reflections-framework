package intellispaces.framework.core.system;

import intellispaces.framework.core.object.ObjectHandle;
import intellispaces.framework.core.system.kernel.SystemObjectHandle;

public interface ObjectHandleWrapper<D> extends ObjectHandle<D> {

  void $init(SystemObjectHandle handle);

  SystemObjectHandle $handle();
}
