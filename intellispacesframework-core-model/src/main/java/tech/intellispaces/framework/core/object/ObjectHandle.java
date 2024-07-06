package tech.intellispaces.framework.core.object;

/**
 * Handle of object.<p/>
 *
 * The handle implements interaction with the object.<p/>
 *
 * The interaction of the system with the object is performed through the object handle.<p/>
 *
 * @param <D> object domain type.
 */
public interface ObjectHandle<D> {

  boolean isMovable();

  MovableObjectHandle<D> asMovableOrElseThrow();
}
