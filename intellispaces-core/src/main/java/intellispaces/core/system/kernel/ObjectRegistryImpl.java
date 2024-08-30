package intellispaces.core.system.kernel;

import intellispaces.core.system.ObjectHandleWrapper;

class ObjectRegistryImpl implements ObjectRegistry {

  @Override
  public void add(ObjectHandleWrapper<?> handle) {
    KernelObjectHandle innerHandle = new ObjectHandleImpl();
    handle.$init(innerHandle);
  }
}
