package tech.intellispaces.core.system;

import tech.intellispaces.core.object.ObjectHandle;
import tech.intellispaces.core.system.shadow.ShadowObjectHandle;

public interface ObjectHandleWrapper<D> extends ObjectHandle<D> {

  void $init(ShadowObjectHandle shadowHandle);

  ShadowObjectHandle $shadowHandle();
}
