package intellispaces.framework.core.object;

import intellispaces.framework.core.exception.TraverseException;
import intellispaces.framework.core.space.channel.Channel0;
import intellispaces.framework.core.space.channel.Channel1;
import intellispaces.framework.core.space.channel.ChannelMethod0;
import intellispaces.framework.core.space.channel.ChannelMethod1;

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

  <Q> MovableObjectHandle<D> moveThru(String cid, Q qualifier) throws TraverseException;

  MovableObjectHandle<D> moveThru(Class<? extends Channel0> channelClass) throws TraverseException;

  MovableObjectHandle<D> moveThru(ChannelMethod0<? super D, ? super D> channelMethod) throws TraverseException;

  <Q> MovableObjectHandle<D> moveThru(Class<? extends Channel1> channelClass, Q qualifier) throws TraverseException;

  <Q> MovableObjectHandle<D> moveThru(ChannelMethod1<? super D, ? super D, Q> channelMethod, Q qualifier) throws TraverseException;

  <R, Q> R mapOfMovingThru(String cid, Q qualifier) throws TraverseException;

  <R> R mapOfMovingThru(ChannelMethod0<? super D, R> channelMethod) throws TraverseException;

  <R, Q> R mapOfMovingThru(ChannelMethod1<? super D, R, Q> channelMethod, Q qualifier) throws TraverseException;

  <R> R mapOfMovingThru(Class<? extends Channel0> channelClass) throws TraverseException;

  <R, Q> R mapOfMovingThru(Class<? extends Channel1> channelClass, Q qualifier) throws TraverseException;
}
