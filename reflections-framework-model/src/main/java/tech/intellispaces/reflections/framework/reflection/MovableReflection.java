package tech.intellispaces.reflections.framework.reflection;

import java.util.List;

import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.channel.Channel0;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.exception.TraverseException;
import tech.intellispaces.reflections.framework.traverse.MappingOfMovingTraverse;

/**
 * The movable reflection.<p/>
 *
 * Movable reflection being moved can move in the notion space.
 */
public interface MovableReflection extends SystemReflection {

  @Override
  default boolean isMovable() {
    return true;
  }

  <Q> MovableReflection moveThru(Rid cid, Q qualifier) throws TraverseException;

  MovableReflection moveThru(Class<? extends Channel0> channelClass) throws TraverseException;

//  MovableReflection moveThru(ChannelFunction0<?, ?> channelFunction) throws TraverseException;

  <Q> MovableReflection moveThru(Class<? extends Channel1> channelClass, Q qualifier) throws TraverseException;

//  <Q> MovableReflection moveThru(ChannelFunction1<?, ?, Q> channelFunction, Q qualifier) throws TraverseException;

  <R, Q> R mapOfMovingThru(Rid cid, Q qualifier) throws TraverseException;

  <R> R mapOfMovingThru(Class<? extends Channel0> channelClass) throws TraverseException;

//  <R> R mapOfMovingThru(ChannelFunction0<?, R> channelFunction) throws TraverseException;

//  <R, Q> R mapOfMovingThru(ChannelFunction1<?, R, Q> channelFunction, Q qualifier) throws TraverseException;

  <R, Q, C extends Channel1 & MappingOfMovingTraverse> R mapOfMovingThru(Class<C> channelClass, Q qualifier) throws TraverseException;

  @Override
  List<MovableReflection> underlyingReflections();

  @Override
  MovableReflection overlyingReflection();
}
