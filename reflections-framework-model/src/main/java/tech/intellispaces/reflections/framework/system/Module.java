package tech.intellispaces.reflections.framework.system;

import java.util.List;

import tech.intellispaces.commons.type.Type;
import tech.intellispaces.reflections.framework.channel.Channel0;
import tech.intellispaces.reflections.framework.channel.Channel1;
import tech.intellispaces.reflections.framework.channel.Channel2;
import tech.intellispaces.reflections.framework.channel.Channel3;
import tech.intellispaces.reflections.framework.channel.Channel4;
import tech.intellispaces.reflections.framework.guide.n0.Mapper0;
import tech.intellispaces.reflections.framework.guide.n0.MapperOfMoving0;
import tech.intellispaces.reflections.framework.guide.n0.Mover0;
import tech.intellispaces.reflections.framework.guide.n1.Mapper1;
import tech.intellispaces.reflections.framework.guide.n1.MapperOfMoving1;
import tech.intellispaces.reflections.framework.guide.n1.Mover1;
import tech.intellispaces.reflections.framework.guide.n2.Mapper2;
import tech.intellispaces.reflections.framework.guide.n2.MapperOfMoving2;
import tech.intellispaces.reflections.framework.guide.n2.Mover2;
import tech.intellispaces.reflections.framework.guide.n3.Mapper3;
import tech.intellispaces.reflections.framework.guide.n3.MapperOfMoving3;
import tech.intellispaces.reflections.framework.guide.n3.Mover3;
import tech.intellispaces.reflections.framework.guide.n4.MapperOfMoving4;
import tech.intellispaces.reflections.framework.reflection.ReflectionForm;
import tech.intellispaces.reflections.framework.traverse.MappingOfMovingTraverse;
import tech.intellispaces.reflections.framework.traverse.MappingTraverse;

/**
 * The system module.
 */
public interface Module {

  void start();

  void start(String[] args);

  void stop();

  <S, T> T mapThruChannel0(S source, String cid);

  <S, T, C extends Channel0 & MappingTraverse> T mapThruChannel0(S source, Class<C> channelClass);

  <S, T, Q> T mapThruChannel1(S source, String cid, Q qualifier);

  <S, T, Q, C extends Channel1 & MappingTraverse> T mapThruChannel1(S source, Class<C> channelClass, Q qualifier);

  <S, R> R moveThruChannel0(S source, String cid);

  <S, R, Q> R moveThruChannel1(S source, String cid, Q qualifier);

  <S, R, Q, C extends Channel1 & MappingOfMovingTraverse> R mapOfMovingThruChannel1(S source, Class<C> channelClass, Q qualifier);

  <S, R, Q> R mapOfMovingThruChannel1(S source, String cid, Q qualifier);

  <S, T> Mapper0<S, T> autoMapperThruChannel0(Type<S> sourceType, String cid, ReflectionForm targetForm);

  <S, T, Q> Mapper1<S, T, Q> autoMapperThruChannel1(Type<S> sourceType, String cid, ReflectionForm targetForm);

  <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruChannel2(Type<S> sourceType, String cid, ReflectionForm targetForm);

  <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruChannel3(Type<S> sourceType, String cid, ReflectionForm targetForm);

  <S> Mover0<S> autoMoverThruChannel0(Type<S> sourceType, String cid, ReflectionForm targetForm);

  <S, Q> Mover1<S, Q> autoMoverThruChannel1(Class<S> sourceClass, String cid, ReflectionForm targetForm);

  <S, Q> Mover1<S, Q> autoMoverThruChannel1(Type<S> sourceType, String cid, ReflectionForm targetForm);

  <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruChannel2(Type<S> sourceType, String cid, ReflectionForm targetForm);

  <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruChannel3(Type<S> sourceType, String cid, ReflectionForm targetForm);

  <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruChannel0(Type<S> sourceType, String cid, ReflectionForm targetForm);

  <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruChannel1(Type<S> sourceType, String cid, ReflectionForm targetForm);

  <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruChannel2(Type<S> sourceType, String cid, ReflectionForm targetForm);

  <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruChannel3(Type<S> sourceType, String cid, ReflectionForm targetForm);

  <S, T, Q1, Q2, Q3, Q4> MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> autoMapperOfMovingThruChannel4(Type<S> sourceType, String cid, ReflectionForm targetForm);

  <S, T> Mapper0<S, T> autoMapperThruChannel0(Type<S> sourceType, Class<? extends Channel0> channelClass, ReflectionForm targetForm);

  <S, T, Q> Mapper1<S, T, Q> autoMapperThruChannel1(Type<S> sourceType, Class<? extends Channel1> channelClass, ReflectionForm targetForm);

  <S, T, Q1, Q2> Mapper2<S, T, Q1, Q2> autoMapperThruChannel2(Type<S> sourceType, Class<? extends Channel2> channelClass, ReflectionForm targetForm);

  <S, T, Q1, Q2, Q3> Mapper3<S, T, Q1, Q2, Q3> autoMapperThruChannel3(Type<S> sourceType, Class<? extends Channel3> channelClass, ReflectionForm targetForm);

  <S> Mover0<S> autoMoverThruChannel0(Type<S> sourceType, Class<? extends Channel0> channelClass, ReflectionForm targetForm);

  <S, Q> Mover1<S, Q> autoMoverThruChannel1(Type<S> sourceType, Class<? extends Channel1> channelClass, ReflectionForm targetForm);

  <S, Q1, Q2> Mover2<S, Q1, Q2> autoMoverThruChannel2(Type<S> sourceType, Class<? extends Channel2> channelClass, ReflectionForm targetForm);

  <S, Q1, Q2, Q3> Mover3<S, Q1, Q2, Q3> autoMoverThruChannel3(Type<S> sourceType, Class<? extends Channel3> channelClass, ReflectionForm targetForm);

  <S, T> MapperOfMoving0<S, T> autoMapperOfMovingThruChannel0(Type<S> sourceType, Class<? extends Channel0> channelClass, ReflectionForm targetForm);

  <S, T, Q> MapperOfMoving1<S, T, Q> autoMapperOfMovingThruChannel1(Type<S> sourceType, Class<? extends Channel1> channelClass, ReflectionForm targetForm);

  <S, T, Q1, Q2> MapperOfMoving2<S, T, Q1, Q2> autoMapperOfMovingThruChannel2(Type<S> sourceType, Class<? extends Channel2> channelClass, ReflectionForm targetForm);

  <S, T, Q1, Q2, Q3> MapperOfMoving3<S, T, Q1, Q2, Q3> autoMapperOfMovingThruChannel3(Type<S> sourceType, Class<? extends Channel3> channelClass, ReflectionForm targetForm);

  <S, T, Q1, Q2, Q3, Q4> MapperOfMoving4<S, T, Q1, Q2, Q3, Q4> autoMapperOfMovingThruChannel4(Type<S> sourceType, Class<? extends Channel4> channelClass, ReflectionForm targetForm);

  <G> G getGuide(String name, Class<G> guideClass);

  <G> G getAutoGuide(Class<G> guideClass);

  <T> T getProjection(String name, Class<T> targetObjectHandleClass);

  <T> List<T> getProjections(Class<T> targetObjectHandleClass);

  /**
   * Adds context projection.
   *
   * @param name the projection name.
   * @param targetObjectHandleClass the projection target class.
   * @param target the projection target.
   * @param <T> the projection target type.
   */
  <T> void addContextProjection(String name, Class<T> targetObjectHandleClass, T target);

  /**
   * Removes context projection.
   *
   * @param name the projection name.
   */
  void removeContextProjection(String name);
}
