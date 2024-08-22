package intellispaces.core.system;

import intellispaces.core.object.ObjectHandle;
import intellispaces.core.system.shadow.ShadowObjectHandle;

public interface ObjectHandleWrapper<D> extends ObjectHandle<D> {

  void $init(ShadowObjectHandle shadowHandle);

  ShadowObjectHandle $shadowHandle();
}
