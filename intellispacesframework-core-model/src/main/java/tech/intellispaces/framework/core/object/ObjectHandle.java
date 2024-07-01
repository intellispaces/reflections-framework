package tech.intellispaces.framework.core.object;

/**
 * Object handle.<p/>
 *
 * Object handle is the handle that connects system with object.<p/>
 *
 * The main purpose of object handle is to provide the access mechanism to object.
 *
 * @param <D> object domain type.
 */
public interface ObjectHandle<D> {

  boolean isMovable();

  MovableObjectHandle<D> asMovableOrElseThrow();
}
