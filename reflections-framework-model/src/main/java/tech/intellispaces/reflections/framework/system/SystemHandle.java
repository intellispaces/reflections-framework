package tech.intellispaces.reflections.framework.system;

import tech.intellispaces.core.Rid;
import tech.intellispaces.reflections.framework.channel.Channel0;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.engine.Engine;
import tech.intellispaces.reflections.framework.traverse.MappingOfMovingTraverse;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;

/**
 * The reflections system handle.
 */
public interface SystemHandle extends ReflectionSystem {

  ModuleHandle currentModule();

  /**
   * Returns the engine.
   */
  Engine engine();

  <S, T> T mapThruChannel0(S source, Rid cid);

  <S, T, C extends Channel0 & MappingTraverse> T mapThruChannel0(S source, Class<C> channelClass);

  <S, T, Q> T mapThruChannel1(S source, Rid cid, Q qualifier);

  <S, T, Q, C extends Channel1 & MappingTraverse> T mapThruChannel1(S source, Class<C> channelClass, Q qualifier);

  <S, R> R moveThruChannel0(S source, Rid cid);

  <S, R, Q> R moveThruChannel1(S source, Rid cid, Q qualifier);

  <S, R, Q, C extends Channel1 & MappingOfMovingTraverse> R mapOfMovingThruChannel1(S source, Class<C> channelClass, Q qualifier);

  <S, R, Q> R mapOfMovingThruChannel1(S source, Rid cid, Q qualifier);
}
