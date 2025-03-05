package tech.intellispaces.jaquarius.object.reference;

import tech.intellispaces.commons.base.exception.UnexpectedExceptions;

public interface ObjectHandles {

  static <H> ObjectHandle<?> handle(H handle) {
    if (handle == null) {
      return null;
    }
    if (handle instanceof ObjectHandle<?>) {
      return (ObjectHandle<?>) handle;
    }
    throw UnexpectedExceptions.withMessage("Not a object handle");
  }

  @SuppressWarnings("unchecked")
  static <D, H> ObjectHandle<D> handle(H handle, Class<D> domainClass) {
    if (handle == null) {
      return null;
    }
    if (handle instanceof ObjectHandle<?>) {
      return (ObjectHandle<D>) handle;
    }
    throw UnexpectedExceptions.withMessage("Not a object handle");
  }

  @SuppressWarnings("unchecked")
  static <D, H> MovableObjectHandle<D> movableHandle(H handle, Class<D> domainClass) {
    if (handle == null) {
      return null;
    }
    if (handle instanceof ObjectHandle<?>) {
      return (MovableObjectHandle<D>) handle;
    }
    throw UnexpectedExceptions.withMessage("Not a movable object handle");
  }
}
