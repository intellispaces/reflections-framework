package intellispaces.framework.core.system;

import intellispaces.framework.core.system.kernel.InnerObjectHandle;

public interface ObjectHandleWrapper {

  void $init(InnerObjectHandle handle);

  InnerObjectHandle $innerHandle();
}
