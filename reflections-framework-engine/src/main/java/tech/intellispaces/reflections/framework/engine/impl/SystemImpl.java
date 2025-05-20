package tech.intellispaces.reflections.framework.engine.impl;

import tech.intellispaces.reflections.framework.channel.Channel0;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.system.System;
import tech.intellispaces.reflections.framework.traverse.MappingOfMovingTraverse;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;

class SystemImpl implements System {
  private final DefaultEngine engine;

  SystemImpl(DefaultEngine engine) {
    this.engine = engine;
  }

  @Override
  public <S, T> T mapThruChannel0(S source, String cid) {
    return engine.mapThruChannel0(source, cid);
  }

  @Override
  public <S, T, C extends Channel0 & MappingTraverse> T mapThruChannel0(S source, Class<C> channelClass) {
    return engine.mapThruChannel0(source, channelClass);
  }

  @Override
  public <S, T, Q> T mapThruChannel1(S source, String cid, Q qualifier) {
    return engine.mapThruChannel1(source, cid, qualifier);
  }

  @Override
  public <S, T, Q, C extends Channel1 & MappingTraverse> T mapThruChannel1(
      S source, Class<C> channelClass, Q qualifier
  ) {
    return engine.mapThruChannel1(source, channelClass, qualifier);
  }

  @Override
  public <S, R> R moveThruChannel0(S source, String cid) {
    return engine.moveThruChannel0(source, cid);
  }

  @Override
  public <S, R, Q> R moveThruChannel1(S source, String cid, Q qualifier) {
    return engine.moveThruChannel1(source, cid, qualifier);
  }

  @Override
  public <S, R, Q, C extends Channel1 & MappingOfMovingTraverse> R mapOfMovingThruChannel1(
      S source, Class<C> channelClass, Q qualifier
  ) {
    return engine.mapOfMovingThruChannel1(source, channelClass, qualifier);
  }

  @Override
  public <S, R, Q> R mapOfMovingThruChannel1(S source, String cid, Q qualifier) {
    return engine.mapOfMovingThruChannel1(source, cid, qualifier);
  }
}
