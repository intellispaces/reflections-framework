package intellispaces.framework.core.object;

import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.space.transition.Transition0;
import intellispaces.framework.core.space.transition.Transition1;
import intellispaces.framework.core.transition.TransitionMethod0;
import intellispaces.framework.core.transition.TransitionMethod1;

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

  <R, Q> R moveThru(String tid, Q qualifier) throws TraverseException;

  <R> R moveThru(TransitionMethod0<? super D, R> transitionMethod) throws TraverseException;

  <R, Q> R moveThru(TransitionMethod1<? super D, R, Q> transitionMethod, Q qualifier) throws TraverseException;

  @SuppressWarnings("rawtypes")
  <R> R moveThru(Class<? extends Transition0> transitionClass) throws TraverseException;

  @SuppressWarnings("rawtypes")
  <R, Q> R moveThru(Class<? extends Transition1> transitionClass, Q qualifier) throws TraverseException;
}
