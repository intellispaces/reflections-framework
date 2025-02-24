package tech.intellispaces.jaquarius.object.reference;

import tech.intellispaces.jaquarius.channel.Channel0;
import tech.intellispaces.jaquarius.channel.Channel1;
import tech.intellispaces.jaquarius.channel.ChannelFunction0;
import tech.intellispaces.jaquarius.channel.ChannelFunction1;
import tech.intellispaces.jaquarius.traverse.MappingOfMovingTraverse;
import tech.intellispaces.jaquarius.exception.TraverseException;

/**
 * The handle of the movable object.<p/>
 *
 * Movable object being moved can move in space.
 *
 * @param <D> the object domain type.
 */
public interface MovableObjectHandle<D> extends ObjectHandle<D> {

  @Override
  default boolean isMovable() {
    return true;
  }

  <Q> MovableObjectHandle<D> moveThru(String cid, Q qualifier) throws TraverseException;

  MovableObjectHandle<D> moveThru(Class<? extends Channel0> channelClass) throws TraverseException;

  MovableObjectHandle<D> moveThru(ChannelFunction0<D, D> channelFunction) throws TraverseException;

  <Q> MovableObjectHandle<D> moveThru(Class<? extends Channel1> channelClass, Q qualifier) throws TraverseException;

  <Q> MovableObjectHandle<D> moveThru(ChannelFunction1<D, D, Q> channelFunction, Q qualifier) throws TraverseException;

  <R, Q> R mapOfMovingThru(String cid, Q qualifier) throws TraverseException;

  <R> R mapOfMovingThru(Class<? extends Channel0> channelClass) throws TraverseException;

  <R> R mapOfMovingThru(ChannelFunction0<D, R> channelFunction) throws TraverseException;

  <R, Q> R mapOfMovingThru(ChannelFunction1<D, R, Q> channelFunction, Q qualifier) throws TraverseException;

  <R, Q, C extends Channel1 & MappingOfMovingTraverse> R mapOfMovingThru(Class<C> channelClass, Q qualifier) throws TraverseException;
}
