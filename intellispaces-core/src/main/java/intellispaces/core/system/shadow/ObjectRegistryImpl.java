package intellispaces.core.system.shadow;

import intellispaces.core.system.ObjectHandleWrapper;
import intellispaces.core.system.ObjectRegistry;

public class ObjectRegistryImpl implements ObjectRegistry {

  @Override
  public void add(ObjectHandleWrapper<?> handle) {
    ShadowObjectHandle innerHandle = new ObjectHandleImpl();
    handle.$init(innerHandle);
  }
}
