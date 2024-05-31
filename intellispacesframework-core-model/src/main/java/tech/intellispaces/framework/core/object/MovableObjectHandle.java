package tech.intellispaces.framework.core.object;

import tech.intellispaces.framework.core.exception.TraverseException;
import tech.intellispaces.framework.core.transition.TransitionMethod1;

/**
 * Handle to movable object.
 *
 * @param <D> object domain type.
 */
public interface MovableObjectHandle<D> extends ObjectHandle<D> {

  <Q> MovableObjectHandle<D> moveThru(String tid, Q qualifier) throws TraverseException;

  <Q> MovableObjectHandle<D> moveThru(TransitionMethod1<D, D, Q> transitionMethod, Q qualifier) throws TraverseException;
}
