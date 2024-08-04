package tech.intellispaces.core.object;

import tech.intellispaces.core.exception.TraverseException;
import tech.intellispaces.core.space.transition.Transition0;
import tech.intellispaces.core.space.transition.Transition1;
import tech.intellispaces.core.transition.TransitionMethod0;
import tech.intellispaces.core.transition.TransitionMethod1;

/**
 * The handle of the movable object.<p/>
 *
 * Movable object being moved can move in space.
 *
 * @param <D> object domain type.
 */
public interface MovableObjectHandle<D> extends ObjectHandle<D> {

  @Override
  default boolean isMovable() {
    return true;
  }

  <B, Q> B moveThru(String tid, Q qualifier) throws TraverseException;

  <B> B moveThru(TransitionMethod0<? super D, B> transitionMethod) throws TraverseException;

  <B, Q> B moveThru(TransitionMethod1<? super D, B, Q> transitionMethod, Q qualifier) throws TraverseException;

  <B> B moveThru(Class<? extends Transition0> transitionClass) throws TraverseException;

  <B, Q> B moveThru(Class<? extends Transition1> transitionClass, Q qualifier) throws TraverseException;
}
