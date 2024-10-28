package intellispaces.jaquarius.system;

import intellispaces.jaquarius.system.kernel.InnerObjectHandle;

public interface ObjectHandleWrapper {

  void $init(InnerObjectHandle handle);

  InnerObjectHandle $innerHandle();
}
