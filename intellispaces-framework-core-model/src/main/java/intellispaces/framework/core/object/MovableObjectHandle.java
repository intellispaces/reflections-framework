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

  <Q> MovableObjectHandle<D> moveThru(String tid, Q qualifier) throws TraverseException;

  @SuppressWarnings("rawtypes")
  MovableObjectHandle<D> moveThru(Class<? extends Transition0> transitionClass) throws TraverseException;

  <D$ extends D> MovableObjectHandle<D> moveThru(TransitionMethod0<D$, D$> transitionMethod) throws TraverseException;

  @SuppressWarnings("rawtypes")
  <Q> MovableObjectHandle<D> moveThru(Class<? extends Transition1> transitionClass, Q qualifier) throws TraverseException;

  <D$ extends D, Q> MovableObjectHandle<D> moveThru(TransitionMethod1<D$, D$, Q> transitionMethod, Q qualifier) throws TraverseException;

  <R, Q> R mapOfMovingThru(String tid, Q qualifier) throws TraverseException;

  <R> R mapOfMovingThru(TransitionMethod0<? super D, R> transitionMethod) throws TraverseException;

  <R, Q> R mapOfMovingThru(TransitionMethod1<? super D, R, Q> transitionMethod, Q qualifier) throws TraverseException;

  @SuppressWarnings("rawtypes")
  <R> R mapOfMovingThru(Class<? extends Transition0> transitionClass) throws TraverseException;

  @SuppressWarnings("rawtypes")
  <R, Q> R mapOfMovingThru(Class<? extends Transition1> transitionClass, Q qualifier) throws TraverseException;
}
