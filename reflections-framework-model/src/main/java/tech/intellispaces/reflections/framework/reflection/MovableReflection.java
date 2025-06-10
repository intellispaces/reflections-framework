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
 */
public interface MovableReflection extends SystemReflection {

  @Override
  default boolean isMovable() {
    return true;
  }

  <Q> MovableReflection moveThru(Rid cid, Q qualifier) throws TraverseException;

  MovableReflection moveThru(Class<? extends Channel0> channelClass) throws TraverseException;

  <S> MovableReflection moveThru(Class<S> sourceDomain, ChannelFunction0<S, S> channelFunction) throws TraverseException;

  <Q> MovableReflection moveThru(Class<? extends Channel1> channelClass, Q qualifier) throws TraverseException;

  <S, QD, QR> MovableReflection moveThru(Class<S> sourceDomain, ChannelFunction1<S, S, QD> channelFunction, Class<QD> qualifierDomain, QR qualifier) throws TraverseException;

  <S> MovableReflection moveThru(Class<S> sourceDomain, ChannelFunction1<S, S, String> channelFunction, String qualifier) throws TraverseException;

  <S> MovableReflection moveThru(Class<S> sourceDomain, ChannelFunction1<S, S, Integer> channelFunction, int qualifier) throws TraverseException;

  <R, Q> R mapOfMovingThru(Rid cid, Q qualifier) throws TraverseException;

  <R> R mapOfMovingThru(Class<? extends Channel0> channelClass) throws TraverseException;

  <S, R> R mapOfMovingThru(Class<S> sourceDomain, ChannelFunction0<S, ?> channelFunction) throws TraverseException;

  <S, R, QD, QR> R mapOfMovingThru(Class<S> sourceDomain, ChannelFunction1<?, ?, ?> channelFunction, Class<QD> qualifierDomain, QR qualifier) throws TraverseException;

  <R, Q, C extends Channel1 & MappingOfMovingTraverse> R mapOfMovingThru(Class<C> channelClass, Q qualifier) throws TraverseException;

  @Override
  List<MovableReflection> underlyingReflections();

  @Override
  MovableReflection overlyingReflection();
}
