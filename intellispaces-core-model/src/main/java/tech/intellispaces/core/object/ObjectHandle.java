package tech.intellispaces.core.object;

import tech.intellispaces.javastatements.type.Type;

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

  Type<D> domain();

  Class<D> domainClass();

  boolean isMovable();

  MovableObjectHandle<D> asMovableOrElseThrow();
}
