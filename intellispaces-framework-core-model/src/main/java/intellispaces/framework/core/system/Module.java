package intellispaces.framework.core.system;

import intellispaces.common.base.type.Type;
import intellispaces.framework.core.guide.n0.Mapper0;
import intellispaces.framework.core.guide.n0.MapperOfMoving0;
import intellispaces.framework.core.guide.n0.Mover0;
import intellispaces.framework.core.guide.n1.Mapper1;
import intellispaces.framework.core.guide.n1.MapperOfMoving1;
import intellispaces.framework.core.guide.n1.Mover1;
import intellispaces.framework.core.guide.n2.Mapper2;
import intellispaces.framework.core.guide.n2.MapperOfMoving2;
import intellispaces.framework.core.guide.n2.Mover2;
import intellispaces.framework.core.guide.n3.Mapper3;
import intellispaces.framework.core.guide.n3.MapperOfMoving3;
import intellispaces.framework.core.guide.n3.Mover3;
import intellispaces.framework.core.space.channel.Channel0;
import intellispaces.framework.core.space.channel.Channel1;
import intellispaces.framework.core.space.channel.Channel2;
import intellispaces.framework.core.space.channel.Channel3;

import java.util.Collection;
import java.util.List;

/**
 * System module.
 */
public interface Module {

  default void start() {
    start(new String[] {});
  }

  void start(String[] args);

  void stop();

  <T> T getProjection(String name, Class<T> targetClass);

  <T> List<T> getProjections(Class<T> targetClass);

  Collection<ModuleProjection> allProjections();

  <S, T> T mapThruChannel0(S source, String cid);

  @SuppressWarnings("rawtypes")
  <S, T> T mapThruChannel0(S source, Class<? extends Channel0> channelClass);

  <S, T, Q> T mapThruChannel1(S source, String cid, Q qualifier);

  @SuppressWarnings("rawtypes")
  <S, T, Q> T mapThruChannel1(S source, Class<? extends Channel1> channelClass, Q qualifier);

  <S, R> R moveThruChannel0(S source, String cid);

  <S, R, Q> R moveThruChannel1(S source, String cid, Q qualifier);

  <S, T> Mapper0<S, T> autoMapperThruChannel0(Type<S> sourceType, String cid);

  <S, T, Q> Mapper1<S, T, Q> autoMapperThruChannel1(Type<S> sourceType, String cid);

  <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruChannel2(Type<S> sourceType, String cid);

  <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruChannel3(Type<S> sourceType, String cid);

  <S> Mover0<S> autoMoverThruChannel0(Type<S> sourceType, String cid);

  <S, Q> Mover1<S, Q> autoMoverThruChannel1(Class<S> sourceClass, String cid);

  <S, Q> Mover1<S, Q> autoMoverThruChannel1(Type<S> sourceType, String cid);

  <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruChannel2(Type<S> sourceType, String cid);

  <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruChannel3(Type<S> sourceType, String cid);

  <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruChannel0(Type<S> sourceType, String cid);

  <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruChannel1(Type<S> sourceType, String cid);

  <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruChannel2(Type<S> sourceType, String cid);

  <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruChannel3(Type<S> sourceType, String cid);

  @SuppressWarnings("rawtypes")
  <S, T> Mapper0<S, T> autoMapperThruChannel0(Type<S> sourceType, Class<? extends Channel0> channelClass);

  @SuppressWarnings("rawtypes")
  <S, T, Q> Mapper1<S, T, Q> autoMapperThruChannel1(Type<S> sourceType, Class<? extends Channel1> channelClass);

  @SuppressWarnings("rawtypes")
  <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruChannel2(Type<S> sourceType, Class<? extends Channel2> channelClass);

  @SuppressWarnings("rawtypes")
  <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruChannel3(Type<S> sourceType, Class<? extends Channel3> channelClass);

  @SuppressWarnings("rawtypes")
  <S> Mover0<S> autoMoverThruChannel0(Type<S> sourceType, Class<? extends Channel0> channelClass);

  @SuppressWarnings("rawtypes")
  <S, Q> Mover1<S, Q> autoMoverThruChannel1(Type<S> sourceType, Class<? extends Channel1> channelClass);

  @SuppressWarnings("rawtypes")
  <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruChannel2(Type<S> sourceType, Class<? extends Channel2> channelClass);

  @SuppressWarnings("rawtypes")
  <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruChannel3(Type<S> sourceType, Class<? extends Channel3> channelClass);

  @SuppressWarnings("rawtypes")
  <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruChannel0(Type<S> sourceType, Class<? extends Channel0> channelClass);

  @SuppressWarnings("rawtypes")
  <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruChannel1(Type<S> sourceType, Class<? extends Channel1> channelClass);

  @SuppressWarnings("rawtypes")
  <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruChannel2(Type<S> sourceType, Class<? extends Channel2> channelClass);

  @SuppressWarnings("rawtypes")
  <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruChannel3(Type<S> sourceType, Class<? extends Channel3> channelClass);
}
