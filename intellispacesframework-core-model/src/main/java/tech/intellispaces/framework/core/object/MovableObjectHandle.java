package tech.intellispaces.framework.core.object;

import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.transition.TransitionMethod1;

/**
 * Movable object handle.<p/>
 *
 * Movable object handle can move related object.
 *
 * @param <D> object domain type.
 */
public interface MovableObjectHandle<D> extends ObjectHandle<D> {

  @Override
  default boolean isMovable() {
    return true;
  }

  <B, Q> B moveThru(String tid, Q qualifier) throws TraverseException;

  <B, Q> B moveThru(TransitionMethod1<D, D, Q> transitionMethod, Q qualifier) throws TraverseException;
}
