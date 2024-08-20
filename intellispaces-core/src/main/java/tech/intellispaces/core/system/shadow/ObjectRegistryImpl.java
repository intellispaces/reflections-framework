package tech.intellispaces.core.system.shadow;

import tech.intellispaces.core.system.ObjectHandleWrapper;
import tech.intellispaces.core.system.ObjectRegistry;

public class ObjectRegistryImpl implements ObjectRegistry {

  @Override
  public void add(ObjectHandleWrapper<?> handle) {
    ShadowObjectHandle innerHandle = new ObjectHandleImpl();
    handle.$init(innerHandle);
  }
}
