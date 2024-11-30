package tech.intellispaces.jaquarius.system;

import tech.intellispaces.jaquarius.engine.descriptor.ObjectHandleInstance;
import tech.intellispaces.jaquarius.system.kernel.InnerObjectHandle;

public interface ObjectHandleWrapper {

  void $init(InnerObjectHandle handle);

  InnerObjectHandle $innerHandle();

  ObjectHandleInstance $objectHandleInstance();
}
