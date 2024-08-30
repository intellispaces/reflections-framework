package intellispaces.core.system.kernel;

import intellispaces.core.system.ObjectHandleWrapper;

public interface ObjectRegistry {

  void add(ObjectHandleWrapper<?> handle);
}
