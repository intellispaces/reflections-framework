package tech.intellispaces.reflectionsj.object.reference;

import tech.intellispaces.commons.exception.UnexpectedExceptions;

public interface ObjectHandles {

  static ObjectHandle<?> handle(Object handle) {
    if (handle == null) {
      return null;
    }
    if (handle instanceof ObjectHandle<?>) {
      return (ObjectHandle<?>) handle;
    }
    throw UnexpectedExceptions.withMessage("Not a object handle");
  }

  @SuppressWarnings("unchecked")
  static <H extends ObjectHandle<?>> H handle(Object handle, Class<H> handleClass) {
    if (handle == null) {
      return null;
    }
    if (handleClass.isAssignableFrom(handle.getClass())) {
      return (H) handle;
    }
    throw UnexpectedExceptions.withMessage("Not a object handle");
  }

  @SuppressWarnings("unchecked")
  static <D> ObjectHandle<D> handleOf(Object handle, Class<D> domainClass) {
    if (handle == null) {
      return null;
    }
    if (handle instanceof ObjectHandle<?>) {
      return (ObjectHandle<D>) handle;
    }
    throw UnexpectedExceptions.withMessage("Not a object handle");
  }

  @SuppressWarnings("unchecked")
  static <D> MovableObjectHandle<D> movableHandleOf(Object handle, Class<D> domainClass) {
    if (handle == null) {
      return null;
    }
    if (handle instanceof ObjectHandle<?>) {
      return (MovableObjectHandle<D>) handle;
    }
    throw UnexpectedExceptions.withMessage("Not a movable object handle");
  }
}
