package tech.intellispaces.reflections.framework.reflection;

import java.util.List;

import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.channel.Channel0;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.channel.ChannelFunction0;
import tech.intellispaces.reflections.framework.channel.ChannelFunction1;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.traverse.MappingOfMovingTraverse;

/**
 * The movable reflection.<p/>
 *
 * Movable reflection being moved can move in the notion space.
 *
 * @param <D> the domain type.
 */
public interface MovableReflection<D> extends Reflection<D> {

  @Override
  default boolean isMovable() {
    return true;
  }

  <Q> MovableReflection<D> moveThru(Rid cid, Q qualifier) throws TraverseException;

  MovableReflection<D> moveThru(Class<? extends Channel0> channelClass) throws TraverseException;

  MovableReflection<D> moveThru(ChannelFunction0<D, D> channelFunction) throws TraverseException;

  <Q> MovableReflection<D> moveThru(Class<? extends Channel1> channelClass, Q qualifier) throws TraverseException;

  <Q> MovableReflection<D> moveThru(ChannelFunction1<D, D, Q> channelFunction, Q qualifier) throws TraverseException;

  <R, Q> R mapOfMovingThru(Rid cid, Q qualifier) throws TraverseException;

  <R> R mapOfMovingThru(Class<? extends Channel0> channelClass) throws TraverseException;

  <R> R mapOfMovingThru(ChannelFunction0<D, R> channelFunction) throws TraverseException;

  <R, Q> R mapOfMovingThru(ChannelFunction1<D, R, Q> channelFunction, Q qualifier) throws TraverseException;

  <R, Q, C extends Channel1 & MappingOfMovingTraverse> R mapOfMovingThru(Class<C> channelClass, Q qualifier) throws TraverseException;

  @Override
  List<MovableReflection<?>> underlyingReflections();

  @Override
  MovableReflection<?> overlyingReflection();
}
