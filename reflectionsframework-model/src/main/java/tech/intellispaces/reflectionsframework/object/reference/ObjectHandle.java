package tech.intellispaces.reflectionsframework.object.reference;

import java.util.List;

/**
 * The focused object reference.
 *
 * @param <D> the object domain type.
 */
public interface ObjectHandle<D> extends ObjectReference<D> {

//  <H extends ObjectHandle<D>> H as(Class<H> objectHandleClass);

  /**
   * Movable object handle can move an object in space.
   *
   * @return <code>true</code> if this object handle can move object and <code>false</code> otherwise.
   */
  boolean isMovable();

  /**
   * Returns movable representation of this object handle or throws exception.
   */
  MovableObjectHandle<D> asMovableOrElseThrow();

  /**
   * Sets projection.
   *
   * @param targetDomain target domain class.
   * @param target target object handle.
   * @param <TD> target domain type.
   * @param <T> target object reference type.
   */
  <TD, T> void addProjection(Class<TD> targetDomain, T target);

  /**
   * Returns nearest known underlying object handles or <code>null</code> if its are unknown.
   */
  List<? extends ObjectHandle<?>> underlyingHandles();

  /**
   * Returns nearest known overlying object handle or <code>null</code> if it is unknown.
   */
  ObjectHandle<?> overlyingHandle();
}
