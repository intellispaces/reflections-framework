package tech.intellispaces.jaquarius.object.reference;

/**
 * The object handle is focused object reference.
 *
 * @param <D> the object domain type.
 */
public interface ObjectHandle<D> extends ObjectReference<D> {

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
   * Stores projection.
   *
   * @param targetDomain target domain class.
   * @param target target object handle.
   * @param <TD> target domain type.
   * @param <TR> target object reference type.
   */
  <TD, TR> void addProjection(Class<TD> targetDomain, TR target);
}
